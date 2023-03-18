//
//  PostService.swift
//  ios
//
//  Created by 정호진 on 2023/02/15.
//

import Foundation
import Alamofire
import RxSwift

///  Main화면 로딩되기 전에 통신해야되는 Service
class PostService {
    let ip = APIURL.ip
    
    /// KLIP 지갑 주소를 backend로 전송
    /// - Parameter walletAddress: 사용자 Klip 지갑 주소
    func sendMyWalletAddress(token: String, walletAddress: String) -> Observable<String> {
        let url = APIURL.apiUrl.inputWalletAddress(ip: ip)
        let body: Parameters = ["walletAddress" : "\(walletAddress)"]
        
        return Observable.create(){ observer in
            AF.request(url,
                       method: .post,
                       parameters: body,
                       encoding: JSONEncoding.default,
                       headers: ["Content-type": "application/json",
                                 "Authorization": "Bearer \(token)"])
            .validate(statusCode: 200..<201)
            .response { response in
                switch response.result{
                case .success(_):
                    print("success send wallet address")
                    observer.onNext("done")
                case .failure(let error):
                    print("sendMyWalletAddress Error!!\n \(error)")
                }
            }
            return Disposables.create()
        }
    }
}
