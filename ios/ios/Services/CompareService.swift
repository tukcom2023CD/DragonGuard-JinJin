//
//  CompareService.swift
//  ios
//
//  Created by 정호진 on 2023/02/24.
//

import Foundation
import Alamofire

final class CompareService{
    static let compareService = CompareService()
    let ip = APIURL.ip
    var firstRepo: String?
    var secondRepo: String?
    var firstRepoUserInfo: [FirstRepoResult] = []
    var secondRepoUserInfo: [SecondRepoResult] = []
    var count = 0
    private init(){ }
    
    func beforeSendingInfo(){
        NotificationCenter.default.addObserver(self, selector: #selector(notificationData(notification:)), name: Notification.Name.compareRepo, object: nil)
        
        guard let firstRepo = self.firstRepo else {return}
        guard let secondRepo = self.secondRepo else {return}
        firstRepoUserInfo = []
        secondRepoUserInfo = []
        let url = APIURL.apiUrl.compareBeforeAPI(ip: ip)
        let body = ["firstRepo": firstRepo, "secondRepo": secondRepo]
        
        Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { timer in
            AF.request(url,
                       method: .post,
                       parameters: body,
                       encoding: JSONEncoding(options: []),
                       headers: ["Content-type": "application/json"])
            .validate(statusCode: 200..<201)
            .responseDecodable(of: CompareUserDecodingModel.self) { response in
                guard let responseResult = response.value else {return}
                print(responseResult)
                if(self.firstRepoUserInfo.count == 0 && responseResult.firstResult.count > 0 ){
                    timer.invalidate()
                    for data in responseResult.firstResult{
                        self.firstRepoUserInfo.append(FirstRepoResult(githubId: data.githubId, commits: data.commits, additions: data.additions, deletions: data.deletions))
                    }
                }
                if(self.secondRepoUserInfo.count == 0 && responseResult.secondResult.count > 0 ){
                    timer.invalidate()
                    for data in responseResult.secondResult{
                        self.secondRepoUserInfo.append(SecondRepoResult(githubId: data.githubId, commits: data.commits, additions: data.additions, deletions: data.deletions))
                    }
                    self.getCompareInfo()
                }
            }
        })
    }
    
    func getCompareInfo(){
        guard let firstRepo = self.firstRepo else {return}
        guard let secondRepo = self.secondRepo else {return}
        print("first: \(firstRepo), second: \(secondRepo)22")
        
        let url = APIURL.apiUrl.compareRepoAPI(ip: ip)
        let body = ["firstRepo": firstRepo, "secondRepo": secondRepo]
        
        AF.request(url,
                   method: .post,
                   parameters: body,
                   encoding: JSONEncoding(options: []),
                   headers: ["Content-type": "application/json"])
        .validate(statusCode: 200..<201)
        .responseDecodable(of: CompareRepoModel.self) { response in
            print("response: \(response)")
        }
        
    }
    
    @objc func notificationData(notification: Notification){
        guard let repo1 = notification.userInfo?[NotificationRepos.repo1] as? String else {return}
        guard let repo2 = notification.userInfo?[NotificationRepos.repo2] as? String else {return}
        
        print(firstRepo)
        print(secondRepo)
        self.firstRepo = repo1
        self.secondRepo = repo2
    }
    
    
    
    
    
}
