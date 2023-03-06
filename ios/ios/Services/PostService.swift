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
    
    /// githubID를 backend로 보냄
    func postMyGithubId() -> Observable<Int> {
        let url = APIURL.apiUrl.inputDBMembers(ip: ip)
        let myGithubId = ["githubId" : "HJ39"]
        
        return Observable.create(){ observer in
            AF.request(url,
                       method: .post,
                       parameters: myGithubId,
                       encoding: JSONEncoding(options: []),
                       headers: ["Content-type": "application/json"])
            .responseDecodable(of: Int.self){ response in
                switch response.result{
                case .success(let data):
                    observer.onNext(data)
                case .failure(let error):
                    print("삐리삐리 에러발생 \(error)")
                    
                }
            }
            return Disposables.create()
        }
    }
    
    /// KLIP 지갑 주소를 backend로 전송
    /// - Parameter walletAddress: 사용자 Klip 지갑 주소
    func sendMyWalletAddress(id: Int, walletAddress: String) -> Observable<Int> {
        let url = APIURL.apiUrl.inputWalletAddress(ip: ip)
        let body: Parameters = [
            "id": id,
            "walletAddress" : "\(walletAddress)"
        ]
        //        let savedId = UserDBId(id: self.data, address: walletAddress)
        //        if let encoded = try? JSONEncoder().encode(savedId){
        //            UserDefaults.standard.setValue(encoded, forKey: "UserDBId")
        //            print(self.data)
        //            print(walletAddress)
        //        }
        
        return Observable.create(){ observer in
            AF.request(url,
                       method: .post,
                       parameters: body,
                       encoding: JSONEncoding.default,
                       headers: ["Content-type": "application/json"])
            .validate(statusCode: 200..<404)
            .response { response in
                switch response.result{
                case .success(_):
                    print("success")
                    observer.onNext(id)
                case .failure(let error):
                    print("sendMyWalletAddress Error!!\n \(error)")
                }
            }
            return Disposables.create()
        }
    }
}
