//
//  RepoContributorInfoService.swift
//  ios
//
//  Created by 정호진 on 2023/02/08.
//

import Foundation
import Alamofire

final class RepoContributorInfoService{
    static let repoShared = RepoContributorInfoService()
    let ip = APIURL.ip
    var selectedName = ""
    var checkData = false
    var resultData = [RepoContributorInfoModel]()
    
    func getRepoContriInfo(){
        let url = APIURL.apiUrl.getRepoContributorInfo(ip: ip, name: selectedName)
        resultData = []
        
        AF.request(url, method: .get)
            .validate(statusCode: 200..<201)
            .responseDecodable(of: [RepoContriInfoDecodingModel].self) { response in
                guard let responseResult = response.value else {return}
                if(responseResult.count > 0 && self.resultData.count == 0){
                    self.checkData = true
                    for data in responseResult{
                        self.resultData.append(RepoContributorInfoModel(githubId: data.githubId, commits: data.commits, additions: data.additions, deletions: data.deletions))
                    }
                }
            }
    }
}
