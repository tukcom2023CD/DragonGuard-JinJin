//
//  YourProfileService.swift
//  ios
//
//  Created by 정호진 on 2023/07/02.
//

import Foundation
import RxSwift
import Alamofire

final class YourProfileService{
    
    func getData(githubId: String) -> Observable<YourProfileModel>{
        let url = APIURL.apiUrl.getOtherPersonProfile(ip: APIURL.ip, githubId: githubId)
        let access = UserDefaults.standard.string(forKey: "Access")
        
        return Observable.create { observer in
            AF.request(url,
                       method: .get,
                       encoding: JSONEncoding.default,
                       headers: [
                        "Content-type": "application/json",
                        "Authorization" : "Bearer \(access ?? "")"
                       ])
            .validate(statusCode: 200..<201)
            .responseDecodable(of: YourProfileModel.self) { res in
                switch res.result{
                case .success(let data):
                    observer.onNext(data)
                case .failure(let error):
                    print("YourProfileService error!\n\(error)")
                }
            }
            return Disposables.create()
        }
    }
    
}
