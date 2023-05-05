//
//  AdminService.swift
//  ios
//
//  Created by 정호진 on 2023/05/05.
//

import Foundation
import RxSwift
import Alamofire

final class AdminService{
    
    // MARK: 조직 ACCPETED, DENIED 할때 사용하는 함수
    func deciceOrganization(id: Int, decide: String) -> Observable<[AdminModel]>{
        let url = APIURL.apiUrl.requestAccept(ip: APIURL.ip)
        let access = UserDefaults.standard.string(forKey: "Access")
        let body: [String : Any] = [
            "id": id,
            "decide": decide
        ]
        var resultList: [AdminModel] = []
        
        return Observable.create { observer in
            AF.request(url,
                       method: .post,
                       parameters: body,
                       headers: [
                        "Content-type": "application/json",
                        "Authorization" : "Bearer \(access ?? "")"
                       ])
            .validate(statusCode: 200..<201)
            .responseDecodable(of: [AdminDecodingModel].self) { res in
                switch res.result{
                case .success(let data):
                    data.forEach { data in
                        resultList.append(AdminModel(id: data.id,
                                                     name: data.name,
                                                     type: data.type))
                    }
                    observer.onNext(resultList)
                case .failure(let error):
                    print("deciceOrganization error!\n\(error)")
                }
            }
            
            return Disposables.create()
        }
    }
    
    // MARK: Get REQUESTED, ACCEPTED, DENIED List
    func getOrganizationList(status: String) -> Observable<[AdminModel]>{
        let url = APIURL.apiUrl.getListAbout_REQUEST_ACCEPT_DENIED(ip: APIURL.ip, status: status)
        let access = UserDefaults.standard.string(forKey: "Access")
        var resultList: [AdminModel] = []
        
        return Observable.create { observer in
            AF.request(url,
                       method: .get,
                       headers: [
                        "Content-type": "application/json",
                        "Authorization" : "Bearer \(access ?? "")"
                       ])
                .validate(statusCode: 200..<201)
                .responseDecodable(of: [AdminDecodingModel].self) { res in
                    switch res.result{
                    case .success(let data):
                        data.forEach { data in
                            resultList.append(AdminModel(id: data.id,
                                                         name: data.name,
                                                         type: data.type))
                        }
                        observer.onNext(resultList)
                    case.failure(let error):
                        print("getOrganizationList error!\n\(error)")
                    }
                }
            
            return Disposables.create()
        }
    }
    
}
