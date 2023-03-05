//
//  MainService.swift
//  ios
//
//  Created by 정호진 on 2023/02/12.
//

import Foundation
import Alamofire


final class MainService{
    static let mainService = MainService()
    let ip = APIURL.ip
    var result: MainModel?
    var data = PostService.postService.data
    private init() {}
    
    
    /// 사용자 정보 받아옴
    func getUserInfo(){
        let url = APIURL.apiUrl.getMembersInfo(ip: ip, id: data)
        
        AF.request(url)
            .validate(statusCode: 200..<201)
            .responseDecodable(of: MainDecodingModel.self) { response in
                guard let data = response.value else {return}
                
                self.result = MainModel(id: data.id,
                                        name: data.name ?? "unknown",
                                        githubId: data.githubId,
                                        commits: data.commits ?? 0,
                                        tier: data.tier,
                                        authStep: data.authStep,
                                        profileImage: data.profileImage ?? "",
                                        rank: data.rank,
                                        tokenAmount: data.tokenAmount ?? 0)
            }
    }
}


