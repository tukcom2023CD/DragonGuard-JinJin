//
//  LoginService.swift
//  ios
//
//  Created by 정호진 on 2023/02/22.
//

import Foundation
import Alamofire
import RxSwift

final class KlipLoginService {
    let ip = APIURL.ip
    
    func klipPrepare() -> Observable<String> {
        let url = APIURL.apiUrl.klipPreparePostAPI()
        let body = ["bapp": ["name" :  "GitRank"],"type": "auth"] as [String : Any]
        
        return Observable<String>.create(){ observer in
            AF.request(url,
                       method: .post,
                       parameters: body,
                       encoding: JSONEncoding(options: []),
                       headers: ["Content-type": "application/json"])
            .validate(statusCode: 200..<201)
            .responseDecodable(of: KlipDecodingModel.self){ response in
                switch response.result{
                case .success(let data):
                    observer.onNext(data.request_key)
                case .failure(let error):
                    print("error! \(error)")
                }
            }
            return Disposables.create()
        }
    }
    
    // KLIP Result get하는 함수
    func klipResult(requestKey: String) -> Observable<String> {
        let url = APIURL.apiUrl.klipResultGetAPI(requestKey: requestKey)

        return Observable.create(){ observer in
            AF.request(url,
                       encoding: JSONEncoding.default,
                       headers: ["Content-Type": "application/json"])
                .validate(statusCode: 200..<201)
                .responseDecodable(of: KlipResultModel.self) { response in
                    switch response.result{
                    case .success(let data):
                        observer.onNext(data.result.klaytn_address)
                        print("address \(data.result.klaytn_address)")
                        
                    case .failure(let error):
                        print("error!! \(error)")
                    }
                }
            return Disposables.create()
        }
    }
    
    
    
}
