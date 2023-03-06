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
        
        return Observable.create(){ observer in
            Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { timer in
                print("adfjlsfjdslf")
                AF.request(url, method: .get)
                    .validate(statusCode: 200..<501)
                    .responseDecodable(of: [RepoContriInfoDecodingModel].self) { response in
                        print("repoContributor \(response)")
                        guard let responseResult = response.value else {return}

                        if responseResult.count > 0 && resultData.count == 0 {
                            timer.invalidate()
                            for data in responseResult{
                                resultData.append(RepoContributorInfoModel(githubId: data.githubId, commits: data.commits, additions: data.additions, deletions: data.deletions))
                                observer.onNext(resultData)
                            }
                        }
                    }
            })
            return Disposables.create()
        }
    }
}
