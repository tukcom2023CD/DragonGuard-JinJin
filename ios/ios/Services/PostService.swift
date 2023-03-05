//
//  PostService.swift
//  ios
//
//  Created by 정호진 on 2023/02/15.
//

import Foundation
import Alamofire

///  Main화면 로딩되기 전에 통신해야되는 Service
class PostService {
    static let postService = PostService()
    let ip = APIURL.ip
    var data = 0
    private init(){}
    
    /// githubID를 backend로 보냄
    func postMyGithubId(){
        let url = APIURL.apiUrl.inputDBMembers(ip: ip)
        let myGithubId = ["githubId" : "HJ39"]
        
        AF.request(url,
                   method: .post,
                   parameters: myGithubId,
                   encoding: JSONEncoding(options: []),
                   headers: ["Content-type": "application/json"])
        .responseDecodable(of: Int.self){ response in
            switch response.result{
            case .success(let data):
                self.data = data
            case .failure(let error):
                print("삐리삐리 에러발생 \(error)")

            }
        }
        
    }
    
    /// KLIP 지갑 주소를 backend로 전송
    /// - Parameter walletAddress: 사용자 Klip 지갑 주소
    func sendMyWalletAddress(walletAddress: String){
        let url = APIURL.apiUrl.inputWalletAddress(ip: ip)
        let body: Parameters = [
            "id": self.data,
            "walletAddress" : "\(walletAddress)"
        ]
        Thread.sleep(forTimeInterval: 0.5)
        print("body \(body)")
        AF.request(url,
                   method: .post,
                   parameters: body,
                   encoding: JSONEncoding.default,
                   headers: ["Content-type": "application/json"])
        .validate(statusCode: 200..<404)
        .responseData { response in
            print("walletPost \(response)")
        }
    }
    
}
