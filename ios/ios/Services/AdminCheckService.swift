//
//  AdminCheckService.swift
//  ios
//
//  Created by 정호진 on 2023/05/05.
//

import Foundation
import Alamofire
import RxSwift

// MARK: 관리자인지 확인하는 클래스
final class AdminCheckService {
    
    func adminCheck() -> Observable<Bool>{
        let url = APIURL.apiUrl.checkAdmin(ip: APIURL.ip)
        let access = UserDefaults.standard.string(forKey: "Access")
        return Observable.create { observer in
            AF.request(url,
                       method: .get,
                       headers: [
                        "Content-type": "application/json",
                        "Authorization" : "Bearer \(access ?? "")"
                       ])
            .validate(statusCode: 200..<201)
            .response{ res in
                switch res.result{
                case .success(_):
                    observer.onNext(true)
                case .failure(let error):
                    print("adminCheck error!\n \(error)")
                    observer.onNext(false)
                }
            }
            
            return Disposables.create()
        }
    }
    
}
