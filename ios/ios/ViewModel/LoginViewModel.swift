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
    func goKlip(){
        LoginService.loginService.klipPrepare()
    }
    
    func getWallet(){
        LoginService.loginService.klipResult()
    }
    
    
}
