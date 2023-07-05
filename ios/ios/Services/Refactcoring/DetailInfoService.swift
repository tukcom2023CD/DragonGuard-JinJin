//
//  DetailInfoService.swift
//  ios
//
//  Created by 정호진 on 2023/07/06.
//

import Foundation
import RxSwift
import Alamofire

final class DetailInfoService{
    
    func getData() -> Observable<DetailInfoModel>{
        let url = APIURL.apiUrl.getMyOragnizationRepo(ip: APIURL.ip)
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
            .responseDecodable(of: DetailInfoModel.self) { res in
                switch res.result{
                case .success(let data):
                    print(data)
                    observer.onNext(data)
                case .failure(let error):
                    print("DetailInfoService error! \n\(error)")
                }
            }
            return Disposables.create()
        }
    }
    
}
