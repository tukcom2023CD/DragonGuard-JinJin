//
//  LoginService.swift
//  ios
//
//  Created by 정호진 on 2023/02/22.
//

import Foundation
import Alamofire

final class KlipLoginService {
    static let klipLoginService = KlipLoginService()
    let ip = APIURL.ip
    var requestKey = ""
    var walletAddress = ""
    
    // KLIP Prepare post 하는 중
    func klipPrepare(){
        let url = APIURL.apiUrl.klipPreparePostAPI()
        let body = ["bapp": ["name" :  "GitRank"],"type": "auth"] as [String : Any]
        walletAddress = ""
        requestKey = ""
        
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
        
        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true, block: { timer in
            if !self.requestKey.isEmpty{
                self.klipDeepLink()
                timer.invalidate()
            }
        })
        
    }
    
    // KLIP DeepLink 함수
    func klipDeepLink() {
        let url = APIURL.apiUrl.klipDeepLinkAPI(requestKey: self.requestKey)
        NotificationCenter.default.post(name: Notification.Name.deepLink,object: nil,userInfo: [NotificationDeepLinkKey.link : url])
        
    }
    
    // KLIP Result get하는 함수
    func klipResult(){
        let url = APIURL.apiUrl.klipResultGetAPI(requestKey: requestKey)
        AF.request(url)
            .validate(statusCode: 200..<201)
            .responseDecodable(of: KlipResultModel.self) { response in
                switch response.result{
                case .success(let data):
                    NotificationCenter.default.post(name: Notification.Name.walletAddress,object: nil,userInfo: [NotificationWalletAddress.walletAddress : data.result.klaytn_address])
                    print("address \(data.result.klaytn_address)")
                case .failure(let error):
                    print("error!! \(error)")
                }
            }
    }
    
    
    
}
