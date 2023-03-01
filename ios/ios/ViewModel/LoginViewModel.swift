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
    
    var userDBId : BehaviorSubject<Int> = BehaviorSubject(value: 0)
    
    func goKlip(){
        KlipLoginService.klipLoginService.klipPrepare()
    }
    
    func getWallet(){
        KlipLoginService.klipLoginService.klipResult()
    }
    
    func sendUserGithubId(){
        PostService.postService.postMyGithubId()
    }
    
    func getUserDBId(){
        print("id num \(PostService.postService.data)")
        if PostService.postService.data != 0{
            userDBId.onNext(PostService.postService.data)
        }
    }
    
    func sendUserWalletAddress(walletAddress: String){
        PostService.postService.sendMyWalletAddress(walletAddress: walletAddress )
    }
    
    
}
