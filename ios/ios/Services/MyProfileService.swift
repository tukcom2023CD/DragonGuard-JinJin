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
    func getMyProfileInfo() -> Observable<MyProfileInfoModel>{
        let url = APIURL.apiUrl.myProfile(ip: APIURL.ip)
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
                    observer.onNext(MyProfileInfoModel(id: data.id,
                                                       name: data.name,
                                                       githubId: data.githubId,
                                                       commits: data.commits,
                                                       issues: data.issues,
                                                       pullRequests: data.pullRequests,
                                                       comments: data.comments,
                                                       tier: data.tier,
                                                       authStep: data.authStep,
                                                       profileImage: data.profileImage,
                                                       rank: data.rank,
                                                       organizationRank: data.organizationRank,
                                                       tokenAmount: data.tokenAmount,
                                                       organization: data.organization))
                case .failure(let error):
                    print("getMyProfileInfo error!\n\(error)")
                }
            }
            
            
            return Disposables.create ()
        }
    }
}
