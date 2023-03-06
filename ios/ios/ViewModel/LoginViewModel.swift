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

final class LoginViewModel {
    let service = KlipLoginService()
    let post = PostService()
    private let disposeBag = DisposeBag()
    private var id = 0  //DB Id
    private var walletAddress = "" // 사용자 지갑 주소
    private var requestKey = "" // 사용자 Klip request Key
    
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
    
}


