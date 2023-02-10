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
        
        Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { timer in
            AF.request(url, method: .get)
                .validate(statusCode: 200..<201)
                .responseDecodable(of: [RepoContriInfoDecodingModel].self) { response in
                    guard let responseResult = response.value else {return}
                    if(responseResult.count != 0 && self.resultData.count == 0){
                        self.checkData = true
                        print("responseResult \(responseResult)" )
                    }
                }
            
            if self.checkData{
                timer.invalidate()
            }
            Thread.sleep(forTimeInterval: 1)
        })
    }
    
    func testInput(){
        resultData.append(RepoContributorInfoModel.init(githubId: "abc", commits: 100, additions: 200, deletions: 100))
        resultData.append(RepoContributorInfoModel.init(githubId: "fe", commits: 120, additions: 200, deletions: 100))
        resultData.append(RepoContributorInfoModel.init(githubId: "jj", commits: 130, additions: 200, deletions: 100))
        resultData.append(RepoContributorInfoModel.init(githubId: "bb", commits: 140, additions: 200, deletions: 100))
        self.checkData = true
    }
    
}
