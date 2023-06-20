//
//  RepoDetailService.swift
//  ios
//
//  Created by 정호진 on 2023/06/20.
//

import Foundation
import Alamofire
import RxSwift

final class RepoDetailService{
    private let disposeBag = DisposeBag()
    
    func getData(title: String) -> Observable<DetailRepoModel>{
        let url = APIURL.apiUrl.getRepoContributorInfo(ip: APIURL.ip, name: title)
        print("repo detail \(url)")
        let access = UserDefaults.standard.string(forKey: "Access")
        
        return Observable.create { observer in
            AF.request(url,
                       method: .get,
                       headers: ["Authorization": "Bearer \(access ?? "")"])
            .validate(statusCode: 200..<500)

            .responseDecodable(of: DetailRepoCodableModel.self) { res in
                print("RepoDetailService\n\(res)")
                switch res.result{
                case .success(let data):
                    var memberList: [GitRepoMembers] = []
                    data.gitRepoMembers?.forEach({ member in
                        memberList.append(GitRepoMembers(githubId: member.githubId,
                                                         profileUrl: member.profileUrl,
                                                         commits: member.commits,
                                                         additions: member.additions,
                                                         deletions: member.deletions,
                                                         isServiceMember: member.isServiceMember))
                    })
                    let data = DetailRepoModel(sparkLine: data.sparkLine, gitRepoMembers: memberList)
                    observer.onNext(data)
                case .failure(let error):
                    print("RepoDetailService error!\(error)")
                }
            }
            return Disposables.create()
        }
    }
    
    
}
