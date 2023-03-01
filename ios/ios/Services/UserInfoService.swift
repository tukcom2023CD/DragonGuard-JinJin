//
//  UserInfoService.swift
//  ios
//
//  Created by 정호진 on 2023/02/05.
//

import Foundation
import Alamofire


// 유저 정보 통신
final class UserInfoService{
    static let sharedData = UserInfoService()
    let ip = APIURL.ip
    var resultArray = [UserInfoModel]()
    
    
    private init() { }
    
    func getMemberInfo(page: Int, size: Int){
        let url = APIURL.apiUrl.getUserInfo(ip: ip, page: page, size: size)
        self.resultArray = []
        DispatchQueue.main.async {
            AF.request(url)
                .validate(statusCode: 200..<201)
                .responseDecodable(of: [UserInfoDecodingData].self) { response in
                    guard let responseResult = response.value else {return}
                    if(responseResult.count != 0 && self.resultArray.count == 0){
                        for data in responseResult {
                            let dataBundle = UserInfoModel(id: data.id, name: data.name ?? "unknown", githubId: data.githubId, tokens: data.tokens ?? 0, tier: data.tier)
                            self.resultArray.append(dataBundle)
                        }
                    }
                }
        }
    }
}
