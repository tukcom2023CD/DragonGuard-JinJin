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
    
    func postToKlip(){
        let url = APIURL.apiUrl.klipPostAPI()
        let body = ["bapp": ["name" :  "GitRank"],"type": "auth"] as [String : Any]
        
        AF.request(url,
                   method: .post,
                   parameters: body,
                   encoding: JSONEncoding(options: []),
                   headers: ["Content-type": "application/json"])
        .validate(statusCode: 200..<201)
        .responseString { response in
            switch response.result{
            case .success(let data):
                print(response)
                print(response.result)
                
            case .failure(let error):
                print("Error Code \(error)")
            
            }
        }
        
        
    }
    
}
