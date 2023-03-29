//
//  AddOrganizationService.swift
//  ios
//
//  Created by 정호진 on 2023/03/29.
//

import Foundation
import Alamofire
import RxSwift

// MARK: 조직 등록하는 서비스 클래스
final class AddOrganizationService{
        
    func addOrganization(name: String, type: String, endPoint: String) -> Observable<Int>{
        let url = APIURL.apiUrl.addOrganization(ip: APIURL.ip)
        let body = [
            "name": name,
            "organizationType": type,
            "emailEndpoint": endPoint
        ]
        return Observable.create { observer in
            
            AF.request(url,
                       method: .post,
                       parameters: body,
                       headers: ["Authorization": "Bearer \(Environment.jwtToken ?? "")"])
            .responseDecodable(of: Int.self){ response in
                
                switch response.result{
                case .success(let data):
                    observer.onNext(data)
                case .failure(let error):
                    print("addOrganization error!! \(error)")
                }
                
            }
            
            return Disposables.create()
        }
    }
    
    
}
