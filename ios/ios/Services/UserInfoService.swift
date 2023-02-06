//
//  UserInfoService.swift
//  ios
//
//  Created by 정호진 on 2023/02/05.
//

import Foundation
import Alamofire


// 유저 정보 통신
class UserInfoService{
    static let sharedData = UserInfoService()
    let ip = APIURL.ip
    var resultArray = [UserInfoModel]()
    
    private init() { }
    
    func getMemberInfo(page: Int, size: Int?){
        let url = APIURL.getUserInfo(page: page, size: size ?? 10)
        self.resultArray = []
    
        DispatchQueue.global().async {
            
            AF.request(url)
                .validate(statusCode: 200..<300)
                .responseDecodable(of: [UserInfoDecodingData].self) { response in
                    print(response)
                    guard let responseResult = response.value else {return}
                    if(responseResult.count != 0 && self.resultArray.count == 0){
                        for data in responseResult {
                            let dataBundle = UserInfoModel(id: data.id, name: data.name, githubId: data.githubId, commits: data.commits, tier: data.tier)
                            print("data2 \(dataBundle)")
                            self.resultArray.append(dataBundle)
                        }
                    }
                    return
                }
            print("done")
            Thread.sleep(forTimeInterval: 5)
            print("gogo")
            
            AF.request(url)
                .validate(statusCode: 200..<300)
                .responseDecodable(of: [UserInfoDecodingData].self) { response in
                    print(response)
                    guard let responseResult = response.value else {return}
                    if(responseResult.count != 0 && self.resultArray.count == 0){
                        for data in responseResult {
                            let dataBundle = UserInfoModel(id: data.id, name: data.name, githubId: data.githubId, commits: data.commits, tier: data.tier)
                            print("data \(dataBundle)")
                            self.resultArray.append(dataBundle)
                        }
                    }
                }
            
        }
        
    }
    
    
}
