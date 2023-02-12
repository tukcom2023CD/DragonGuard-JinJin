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
    
    private init() {}
    
    func getUserInfo(id: Int){
        let url = APIURL.apiUrl.getMembersInfo(ip: ip, id: id)
        
        DispatchQueue.main.async {
            AF.request(url)
                .validate(statusCode: 200..<201)
                .responseDecodable(of: MainDecodingModel.self) { response in
                    guard let data = response.value else {return}
                    self.result = MainModel(id: data.id, name: data.name, githubId: data.githubId, commits: data.commits, tier: data.tier, authStep: data.authStep, profileImage: data.profileImage,rank: data.rank)
                }
        }
        
    }
}


