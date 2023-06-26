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
    
    func updateProfile() {
        let url = APIURL.apiUrl.updateMyDB(ip: ip)
        let access = UserDefaults.standard.string(forKey: "Access")
        
        AF.request(url,
                   method: .post
                   ,headers: ["Authorization": "Bearer \(access ?? "")"])
        .validate(statusCode: 200..<201)
        .responseString{ res in
            print(res)
        }
    }
    
    
    /// 사용자 정보 받아옴
    func getUserInfo() -> Observable<MainModel>{
        let url = APIURL.apiUrl.getMembersInfo(ip: ip)
        let access = UserDefaults.standard.string(forKey: "Access")
        
        return Observable.create(){ observer in
            
            Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { timer in
                AF.request(url, headers: ["Authorization": "Bearer \(access ?? "")"])
                    .validate(statusCode: 200..<201)
                    .responseDecodable(of: MainModel.self) { response in
                        print("main service")
                        print("used accessToken \(access ?? "") ")
                        print(response)
                        
                        switch response.result{
                        case.success(let data):
                            
                            if data.profile_image != "" && data.token_amount ?? 0 > -1 && data.tier != "" {
                                timer.invalidate()
                                observer.onNext(data)
                            }
                        case .failure(let error):
                            print("삐리삐리 에러발생 !! \(error)")
                        }
                        
                        
                    }
            })
            return Disposables.create()
            
        }
        
    }
}


