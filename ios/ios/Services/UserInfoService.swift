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
    var checkData = false
    
    private init() { }
    
    func getMemberInfo(page: Int, size: Int?){
        let url = APIURL.apiUrl.getUserInfo(ip: ip, page: 0, size: 20)
        self.resultArray = []
        checkData = false
        
        Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { timer in
            AF.request(url)
                .validate(statusCode: 200..<201)
                .responseDecodable(of: [UserInfoDecodingData].self) { response in
                    print(response.value)
                    
                    guard let responseResult = response.value else {return}
                    if(responseResult.count != 0 && self.resultArray.count == 0){
                        for data in responseResult {
                            self.checkData = true
                            let dataBundle = UserInfoModel(id: data.id, name: data.name, githubId: data.githubId, commits: data.commits, tier: data.tier)
                            print("data \(dataBundle)")
                            self.resultArray.append(dataBundle)
                        }
                    }
                }
            
            if self.checkData{
                timer.invalidate()
            }
            Thread.sleep(forTimeInterval: 1)
        })
        
        
    }
    
    
}
