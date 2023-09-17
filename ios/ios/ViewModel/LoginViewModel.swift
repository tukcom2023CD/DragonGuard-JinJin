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
    let withDrawService = WithDrawService()
    private let disposeBag = DisposeBag()
    private var walletAddress = "" // 사용자 지갑 주소
    private var requestKey = "" // 사용자 Klip request Key
    
    /// 새로운 멤머인지 확인 true: 클립인증 필요, false: 불필요
    var checkNewMemberInKlip = BehaviorRelay(value: false)
    
    var githubAuthSubject = BehaviorRelay(value: false)   // github OAuth 완료 시 true 전송
    var jwtTokenSubject = BehaviorSubject(value: "")    // jwt Token 전송
    // Github OAuth 완료했는지 확인 하는 용도
    private var checkGithubAuth = false {
        willSet{
            githubAuthSubject.accept(newValue)
        }
    }
    
    // klip 지갑 연동 완료했는지 확인하는 용도
    var klipAuthSubject = BehaviorRelay(value: false)     // klip 지갑 토큰 완료한 경우 true 전송
    private var checkKlipAuth = false{
        willSet{
            klipAuthSubject.accept(newValue)
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
        let access = UserDefaults.standard.string(forKey: "Access")
        
        post.sendMyWalletAddress(token: access ?? "", walletAddress: self.walletAddress)
            .subscribe(onNext: { msg in
                self.post.updateMyDB()
                print(msg)
            })
            .disposed(by: disposeBag)
    }
    
    /// MARK: 로그인한 유저인지 확인
    func checkLoginUser() -> Observable<Bool> {
        return Observable.create {  observer in
            self.post.checkLoginUser()
                .subscribe(onNext:{  [weak self] check in
                    print("checkLoginUser \(check)")
                    if !check{
                        self?.checkNewMemberInKlip.accept(true)
                    }
                    observer.onNext(check)
                })
                .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
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
    
    func deleteMemberInfo() -> Observable<Bool> {
        return Observable.create { observer in
            self.withDrawService.withDraw()
                .subscribe(onNext:{ check in
                    if check{
                        self.checkKlipAuth = false
                        self.checkGithubAuth = false
                        UserDefaults.standard.removeObject(forKey: "Access")
                        UserDefaults.standard.removeObject(forKey: "Refresh")
                        observer.onNext(true)
                    }
                })
                .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
    
}


