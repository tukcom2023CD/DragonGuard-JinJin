//
//  MainService.swift
//  ios
//
//  Created by 정호진 on 2023/02/12.
//

import Foundation
import Alamofire
import RxSwift


final class MainService{
    let ip = APIURL.ip
    
    /// 사용자 정보 받아옴
    func getUserInfo(token: String) -> Observable<MainModel>{
        let url = APIURL.apiUrl.getMembersInfo(ip: ip)
        
        return Observable.create(){ observer in
            AF.request(url, headers: ["Authorization": "Bearer \(token)"])
                .validate(statusCode: 200..<201)
                .responseDecodable(of: MainDecodingModel.self) { response in
                    print("main service")
                    print("used accessToken \(Environment.jwtToken )")
                    print(response)
                    
                    switch response.result{
                    case.success(let data):
                        let info = MainModel(id: data.id,
                                             name: data.name ?? "unknown",
                                             githubId: data.githubId,
                                             commits: data.commits ?? 0,
                                             tier: data.tier,
                                             authStep: data.authStep,
                                             profileImage: data.profileImage ?? "",
                                             rank: data.rank,
                                             organizationRank: data.organizationRank ?? 0,
                                             tokenAmount: data.tokenAmount ?? 0,
                                             organization: data.organization ?? "")
                        observer.onNext(info)
                    case .failure(let error):
                        print("삐리삐리 에러발생 !! \(error)")
                    }
                    
                   
                }
            return Disposables.create()
        }
    }
}


