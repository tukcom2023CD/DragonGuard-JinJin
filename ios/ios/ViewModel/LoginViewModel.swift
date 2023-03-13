//
//  LoginViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/22.
//

import Foundation
import UIKit
import RxCocoa
import RxSwift
import SafariServices

final class LoginViewModel {
    static let loginService = LoginViewModel()
    let service = KlipLoginService()
    let post = PostService()
    let githubService = GithubLoginService()
    private let disposeBag = DisposeBag()
    private var id = 0  //DB Id
    private var walletAddress = "" // 사용자 지갑 주소
    private var requestKey = "" // 사용자 Klip request Key
    private var githubCode = ""
    private var githubUserToken = ""
    
    var githubAuthSubject = BehaviorSubject(value: false)   // github OAuth 완료 시 true 전송
    // Github OAuth 완료했는지 확인 하는 용도
    private var checkGithubAuth = false {
        willSet{
            githubAuthSubject.onNext(newValue)
        }
    }
    
    // klip 지갑 연동 완료했는지 확인하는 용도
    var klipAuthSubject = BehaviorSubject(value: false)     // klip 지갑 토큰 완료한 경우 true 전송
    private var checkKlipAuth = false{
        willSet{
            klipAuthSubject.onNext(newValue)
        }
    }
    
    /*
     KLIP 연동
     */
    
    func prepareKlip() -> Observable<String>{
        return Observable<String>.create(){ observer in
            self.service.klipPrepare()
                .subscribe(onNext: { requestKey in
                    self.requestKey = requestKey
                    let url = APIURL.apiUrl.klipDeepLinkAPI(requestKey: requestKey)
                    observer.onNext(url)
                })
                .disposed(by: self.disposeBag)
            return Disposables.create()
        }
    }
    
    // Klip wallet주소 가져오는 함수
    func getWallet() -> Observable<String> {
        return Observable<String>.create(){ observer in
            self.service.klipResult(requestKey: self.requestKey)
                .subscribe(onNext: { address in
                    self.walletAddress = address
                    observer.onNext(address)
                    self.checkKlipAuth = true
                    self.userWalletAddress()
                })
                .disposed(by: self.disposeBag)
            return Disposables.create()
        }
    }
    
    
    //  user github Id를 보내고 DB Id를 가져옴
    func userGithubId() -> Observable<Int>{
        return Observable.create(){ observer in
            self.post.postMyGithubId()
                .subscribe(onNext: { id in
                    self.id = id
                    observer.onNext(id)
                })
                .disposed(by: self.disposeBag)
            return Disposables.create()
        }
    }
    
    // 사용자 지갑 주소 서버로 전송
    func userWalletAddress(){
        post.sendMyWalletAddress(id: self.id, walletAddress: self.walletAddress)
            .subscribe(onNext: { id in })
            .disposed(by: disposeBag)
    }
    
    
    /*
     Github OAuth 연동 함수들
     */
    
    
    // github post 통신
    func getUserOAuthToken(code: String) {
        githubService.githubPost(clientId: Environment.clientId, secretCode: Environment.clientSecret, code: code)
            .subscribe(onNext: { data in
                self.githubUserToken = data
                self.checkGithubAuth = true
                print("github user token =\(self.githubUserToken)")
            })
            .disposed(by: disposeBag)
    }
    
}


