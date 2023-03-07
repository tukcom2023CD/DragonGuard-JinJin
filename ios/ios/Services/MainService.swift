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
    func getUserInfo(id: Int) -> Observable<MainModel>{
        let url = APIURL.apiUrl.getMembersInfo(ip: ip, id: id)
        
        //        if let savedData = UserDefaults.standard.object(forKey: "UserDBId") as? Data{
        //            if let savedObject = try? JSONDecoder().decode(UserDBId.self, from: savedData){
        //                self.data = savedObject.id ?? 0
        //            }
        //        }
        
        return Observable.create(){ observer in
            AF.request(url)
                .validate(statusCode: 200..<201)
                .responseDecodable(of: MainDecodingModel.self) { response in
                    guard let data = response.value else {return}
                    let info = MainModel(id: data.id,
                                         name: data.name ?? "unknown",
                                         githubId: data.githubId,
                                         commits: data.commits ?? 0,
                                         tier: data.tier,
                                         authStep: data.authStep,
                                         profileImage: data.profileImage ?? "",
                                         rank: data.rank,
                                         tokenAmount: data.tokenAmount ?? 0)
                    observer.onNext(info)
                }
            return Disposables.create()
        }
    }
}


