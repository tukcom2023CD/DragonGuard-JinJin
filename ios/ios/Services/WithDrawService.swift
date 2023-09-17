//
//  WithDrawService.swift
//  ios
//
//  Created by 정호진 on 2023/09/15.
//

import Foundation
import Alamofire
import RxSwift

final class WithDrawService {
    
    func withDraw() -> Observable<Bool> {
        let url = APIURL.apiUrl.withDrawMember(ip: APIURL.ip)
        let access = UserDefaults.standard.string(forKey: "Access")
        
        return Observable.create { observer in
            AF.request(url,
                       method: .delete,
                       headers: ["Authorization": "Bearer \(access ?? "")"])
            .validate(statusCode: 204..<205)
            .responseString { res in
                print(res)
                switch res.result{
                case .success(_):
                    observer.onNext(true)
                case .failure(let error):
                    observer.onNext(false)
                    print("withDraw error!\n\(error)")
                }
            }
            
            return Disposables.create()
        }
        
    }
}
