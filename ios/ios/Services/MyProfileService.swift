//
//  MyProfileService.swift
//  ios
//
//  Created by 정호진 on 2023/04/14.
//

import Foundation
import Alamofire
import RxSwift

final class MyProfileService{
    
    // MARK: 자기 자신 정보 받아오는 함수
    func getMyProfileInfo(githubId: String) -> Observable<MyProfileInfoModel>{
        let url = APIURL.apiUrl.myProfile(ip: APIURL.ip, githubId: githubId)
        let access = UserDefaults.standard.string(forKey: "Access")
        return Observable.create { observer in
         
            AF.request(url,
                       method: .get,
                       encoding: JSONEncoding.default,
                       headers: [
                        "Content-type": "application/json",
                        "Authorization" : "Bearer \(access ?? "")"
                       ])
            .responseDecodable(of: MyProfileInfoDecodingModel.self) { response in
                switch response.result{
                case .success(let data):
                    print(data)
                    observer.onNext(MyProfileInfoModel(id: data.id ?? "",
                                                       name: data.name ?? "",
                                                       githubId: data.githubId ?? "",
                                                       commits: data.commits ?? 0,
                                                       issues: data.issues ?? 0,
                                                       pullRequests: data.pullRequests ?? 0,
                                                       comments: data.comments ?? 0,
                                                       tier: data.tier ?? "",
                                                       authStep: data.authStep ?? "",
                                                       profileImage: data.profileImage ?? "",
                                                       rank: data.rank ?? 0,
                                                       organizationRank: data.organizationRank ?? 0,
                                                       tokenAmount: data.tokenAmount ?? 0,
                                                       organization: data.organization ?? "",
                                                       gitOrganizations: data.gitOrganizations ?? [],
                                                       gitRepos: data.gitRepos ?? []))
                    
                case .failure(let error):
                    print("getMyProfileInfo error!\n\(error)")
                }
            }
            
            
            return Disposables.create ()
        }
    }
}
