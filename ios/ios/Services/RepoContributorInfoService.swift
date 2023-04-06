//
//  RepoContributorInfoService.swift
//  ios
//
//  Created by 정호진 on 2023/02/08.
//

import Foundation
import Alamofire
import RxSwift

final class RepoContributorInfoService{
    let ip = APIURL.ip
    
    /// 레포지토리 내부 Contributor 정보
    func getRepoContriInfo(selectedName: String) -> Observable<[RepoContributorInfoModel]> {
        let url = APIURL.apiUrl.getRepoContributorInfo(ip: ip, name: selectedName)
        var resultData = [RepoContributorInfoModel]()
        print(url)
        let access = UserDefaults.standard.string(forKey: "Access")

        return Observable.create(){ observer in
            Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { timer in
            
            AF.request(url,
                           method: .get,
                       headers: ["Authorization": "Bearer \(access ?? "")"])
                .responseString { response in
                    switch response.result {
                       case .success(let data):
                           if let jsonData = data.data(using: .utf8) {
                               do {
                                   let decodedData = try JSONDecoder().decode([RepoContriInfoDecodingModel].self, from: jsonData)
                                   print(decodedData)
                                   timer.invalidate()
                                   for data in decodedData{
                                       resultData.append(RepoContributorInfoModel(githubId: data.githubId, commits: data.commits, additions: data.additions, deletions: data.deletions))
                                       observer.onNext(resultData)
                                   }   
                               } catch {
                                   print("Error decoding data: \(error)")
                               }
                           }
                       case .failure(let error):
                           print("Request failed with error: \(error)")
                       }
                }
            
            })
            return Disposables.create()
        }
    }
}
