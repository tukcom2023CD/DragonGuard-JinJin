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
    private let disposeBag = DisposeBag()
    private var walletAddress = "" // 사용자 지갑 주소
    private var requestKey = "" // 사용자 Klip request Key
    
    var githubAuthSubject = BehaviorSubject(value: false)   // github OAuth 완료 시 true 전송
    var jwtTokenSubject = BehaviorSubject(value: "")    // jwt Token 전송
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

    // 사용자 지갑 주소 서버로 전송
    func userWalletAddress(){
        post.sendMyWalletAddress(token: Environment.jwtToken!, walletAddress: self.walletAddress)
            .subscribe(onNext: { msg in print(msg)})
            .disposed(by: disposeBag)
    }
        
    // MARK: 로그아웃 확인하는 함수
    func logOutDone() -> Observable<Bool>{
        return Observable.create { observer in
            self.checkKlipAuth = false
            self.checkGithubAuth = false
            observer.onNext(true)
            return Disposables.create()
        }
    }
}


