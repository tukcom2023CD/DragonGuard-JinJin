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
    
    
    func updateProfile() -> Observable<Bool> {
        let url = APIURL.apiUrl.updateMyDB(ip: ip)
        let access = UserDefaults.standard.string(forKey: "Access")
        
        return Observable.create{ observer in
            AF.request(url,
                       method: .post
                       ,headers: ["Authorization": "Bearer \(access ?? "")"])
            .validate(statusCode: 200..<201)
            .response{ res in
                switch res.result{
                case .success(_):
                    print("update success")
                    observer.onNext(true)
                case .failure(let error):
                    print("update error!\n\(error)")
                    observer.onNext(false)
                }
            }
            
            return Disposables.create()
        }
    }
    
    
    /// 사용자 정보 받아옴
    func getUserInfo() -> Observable<MainModel>{
        let url = APIURL.apiUrl.getMembersInfo(ip: ip)
        let access = UserDefaults.standard.string(forKey: "Access")
        
        return Observable.create(){ observer in
                AF.request(url, headers: ["Authorization": "Bearer \(access ?? "")"])
                    .validate(statusCode: 200..<201)
                    .responseDecodable(of: MainModel.self) { response in
                        print("main service")
                        print("used accessToken \(access ?? "") ")
                        
                        print(response)
                        switch response.result{
                        case.success(let data):
                            observer.onNext(data)
                        case .failure(let error):
                            print("삐리삐리 에러발생 !! \(error)")
                        }
                        
                        
                    }
            return Disposables.create()
            
        }
        
    }
}


