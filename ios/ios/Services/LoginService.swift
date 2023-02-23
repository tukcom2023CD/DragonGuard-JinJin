//
//  LoginService.swift
//  ios
//
//  Created by 정호진 on 2023/02/22.
//

import Foundation
import Alamofire

final class LoginService {
    static let loginService = LoginService()
    let ip = APIURL.ip
    var requestKey = ""
    var walletAddress = ""
    
    // KLIP Prepare post 하는 중
    func klipPrepare(){
        let url = APIURL.apiUrl.klipPreparePostAPI()
        let body = ["bapp": ["name" :  "GitRank"],"type": "auth"] as [String : Any]
        
        AF.request(url,
                   method: .post,
                   parameters: body,
                   encoding: JSONEncoding(options: []),
                   headers: ["Content-type": "application/json"])
        .validate(statusCode: 200..<201)
        .responseDecodable(of: KlipDecodingModel.self){ response in
            switch response.result{
            case .success(let data):
                self.requestKey = data.request_key
            case .failure(let error):
                print("error! \(error)")
            }
        }
    }
    
    
    // KLIP Result get하는 함수
    func klipResult(){
        let url = APIURL.apiUrl.klipResultGetAPI(requestKey: requestKey)
        
        AF.request(url)
            .validate(statusCode: 200..<201)
            .responseDecodable(of: KlipResultModel.self) { response in
                switch response.result{
                case .success(let data):
                    self.walletAddress = data.result.klaytn_address
                case .failure(let error):
                    print("error!! \(error)")
                }
            }
    }
    
    
    
}
