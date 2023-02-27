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
    var firstRepoUserInfo: [FirstRepoResult] = []   // 첫 번째 레포 내부 유저 리스트
    var secondRepoUserInfo: [SecondRepoResult] = []  // 두 번째 레포 내부 유저 리스트
    var repoUserInfo: CompareUserModel?
    var firstRepoInfo: [FirstRepoModel] = []     // 첫 번째 레포 정보 리스트
    var secondRepoInfo: [secondRepoModel] = []  // 첫 번째 레포 정보 리스트
    
    private init(){ }
    
    func beforeSendingInfo(firstRepo: String, secondRepo: String){
        firstRepoUserInfo = []
        secondRepoUserInfo = []
        self.firstRepo = firstRepo
        self.secondRepo = secondRepo
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
                print("response \(responseResult)")
                if responseResult.firstResult.count > 0 && responseResult.secondResult.count > 0 {
                    timer.invalidate()
                    
                    for data in responseResult.firstResult{
                        self.firstRepoUserInfo.append(FirstRepoResult(githubId: data.githubId, commits: data.commits, additions: data.additions, deletions: data.deletions))
                    }
                    
                    for data in responseResult.secondResult{
                        self.secondRepoUserInfo.append(SecondRepoResult(githubId: data.githubId, commits: data.commits, additions: data.additions, deletions: data.deletions))
                    }
                    self.repoUserInfo?.firstResult = self.firstRepoUserInfo
                    self.repoUserInfo?.secondResult = self.secondRepoUserInfo
                    self.getCompareInfo()
                }
            }
        })
    }
    
    func getCompareInfo(){
        guard let firstRepo = self.firstRepo else {return}
        guard let secondRepo = self.secondRepo else {return}
        
        firstRepoInfo = []
        secondRepoInfo = []
        let url = APIURL.apiUrl.compareRepoAPI(ip: ip)
        let body = ["firstRepo": firstRepo, "secondRepo": secondRepo]
        
        AF.request(url,
                   method: .post,
                   parameters: body,
                   encoding: JSONEncoding(options: []),
                   headers: ["Content-type": "application/json"])
        .validate(statusCode: 200..<201)
        .responseDecodable(of: CompareRepoDecodingModel.self) { response in
            guard let responseResult = response.value else {return}
            print("response: \(response)")
            if self.firstRepoInfo.count == 0{
                var language: [String] = []
                var count: [Int] = []
                for lang in responseResult.firstRepo.languages.language{
                    language.append(lang)
                }
                for cnt in responseResult.firstRepo.languages.count{
                    count.append(cnt)
                }
                
                self.firstRepoInfo.append(
                    FirstRepoModel(gitRepo: GitRepoModel(full_name: responseResult.firstRepo.gitRepo.full_name,
                                                         forks_count: responseResult.firstRepo.gitRepo.forks_count,
                                                         stargazers_count: responseResult.firstRepo.gitRepo.stargazers_count,
                                                         watchers_count: responseResult.firstRepo.gitRepo.watchers_count,
                                                         open_issues_count: responseResult.firstRepo.gitRepo.open_issues_count,
                                                         closed_issues_count: responseResult.firstRepo.gitRepo.closed_issues_count,
                                                         subscribers_count: responseResult.firstRepo.gitRepo.subscribers_count),
                                   statistics: StatisticsModel(commitStats: StatisticsStatsModel(count: responseResult.firstRepo.statistics.commitStats.count ?? 0,
                                                                                                 sum: responseResult.firstRepo.statistics.commitStats.sum ?? 0,
                                                                                                 min: responseResult.firstRepo.statistics.commitStats.min ?? 0,
                                                                                                 max: responseResult.firstRepo.statistics.commitStats.max ?? 0,
                                                                                                 average: responseResult.firstRepo.statistics.commitStats.average ?? 0),
                                                               additionStats: StatisticsStatsModel(count: responseResult.firstRepo.statistics.additionStats.count ?? 0,
                                                                                                   sum: responseResult.firstRepo.statistics.additionStats.sum ?? 0,
                                                                                                   min: responseResult.firstRepo.statistics.additionStats.min ?? 0,
                                                                                                   max: responseResult.firstRepo.statistics.additionStats.max ?? 0,
                                                                                                   average: responseResult.firstRepo.statistics.additionStats.average ?? 0),
                                                               deletionStats: StatisticsStatsModel(count: responseResult.firstRepo.statistics.deletionStats.count ?? 0,
                                                                                                   sum: responseResult.firstRepo.statistics.deletionStats.sum ?? 0,
                                                                                                   min: responseResult.firstRepo.statistics.deletionStats.min ?? 0,
                                                                                                   max: responseResult.firstRepo.statistics.deletionStats.max ?? 0,
                                                                                                   average: responseResult.firstRepo.statistics.deletionStats.average ?? 0)),
                                   languages: LanguagesModel(language: language, count: count),
                                   languagesStats: StatisticsStatsModel(count: responseResult.firstRepo.languagesStats.count ?? 0,
                                                                        sum: responseResult.firstRepo.languagesStats.sum ?? 0,
                                                                        min: responseResult.firstRepo.languagesStats.min ?? 0,
                                                                        max: responseResult.firstRepo.languagesStats.max ?? 0,
                                                                        average: responseResult.firstRepo.languagesStats.average ?? 0)))
            }
            
            
            if self.secondRepoInfo.count == 0{
                var language: [String] = []
                var count: [Int] = []
                for lang in responseResult.secondRepo.languages.language{
                    language.append(lang)
                }
                for cnt in responseResult.secondRepo.languages.count{
                    count.append(cnt)
                }
                
                self.secondRepoInfo.append(
                    secondRepoModel(gitRepo: GitRepoModel(full_name: responseResult.secondRepo.gitRepo.full_name,
                                                          forks_count: responseResult.secondRepo.gitRepo.forks_count,
                                                          stargazers_count: responseResult.secondRepo.gitRepo.stargazers_count,
                                                          watchers_count: responseResult.secondRepo.gitRepo.watchers_count,
                                                          open_issues_count: responseResult.secondRepo.gitRepo.open_issues_count,
                                                          closed_issues_count: responseResult.secondRepo.gitRepo.closed_issues_count,
                                                          subscribers_count: responseResult.secondRepo.gitRepo.subscribers_count),
                                    statistics: StatisticsModel(commitStats: StatisticsStatsModel(count: responseResult.secondRepo.statistics.commitStats.count ?? 0,
                                                                                                  sum: responseResult.secondRepo.statistics.commitStats.sum ?? 0,
                                                                                                  min: responseResult.secondRepo.statistics.commitStats.min ?? 0,
                                                                                                  max: responseResult.secondRepo.statistics.commitStats.max ?? 0,
                                                                                                  average: responseResult.secondRepo.statistics.commitStats.average ?? 0),
                                                                additionStats: StatisticsStatsModel(count: responseResult.secondRepo.statistics.additionStats.count ?? 0,
                                                                                                    sum: responseResult.secondRepo.statistics.additionStats.sum ?? 0,
                                                                                                    min: responseResult.secondRepo.statistics.additionStats.min ?? 0,
                                                                                                    max: responseResult.secondRepo.statistics.additionStats.max ?? 0,
                                                                                                    average: responseResult.secondRepo.statistics.additionStats.average ?? 0),
                                                                deletionStats: StatisticsStatsModel(count: responseResult.secondRepo.statistics.deletionStats.count ?? 0,
                                                                                                    sum: responseResult.secondRepo.statistics.deletionStats.sum ?? 0,
                                                                                                    min: responseResult.secondRepo.statistics.deletionStats.min ?? 0,
                                                                                                    max: responseResult.secondRepo.statistics.deletionStats.max ?? 0,
                                                                                                    average: responseResult.secondRepo.statistics.deletionStats.average ?? 0)),
                                    languages: LanguagesModel(language: language, count: count),
                                    languagesStats: StatisticsStatsModel(count: responseResult.secondRepo.languagesStats.count ?? 0,
                                                                         sum: responseResult.secondRepo.languagesStats.sum ?? 0,
                                                                         min: responseResult.secondRepo.languagesStats.min ?? 0,
                                                                         max: responseResult.secondRepo.languagesStats.max ?? 0,
                                                                         average: responseResult.secondRepo.languagesStats.average ?? 0)))
            }
            
            
            
        }
        
    }
    
    
    
    
    
    
    
}
