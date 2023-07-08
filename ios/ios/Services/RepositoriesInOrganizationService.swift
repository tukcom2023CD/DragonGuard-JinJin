//
//  RepositoriesInOrganizationService.swift
//  ios
//
//  Created by 정호진 on 2023/07/07.
//

import Foundation
import Alamofire
import RxSwift

final class RepositoriesInOrganizationService{
    
    func getData(name: String) -> Observable<RepositoriesInOrganizationModel>{
        let url = APIURL.apiUrl.getRepositoriesListInOrganization(ip: APIURL.ip, name: name)
        let access = UserDefaults.standard.string(forKey: "Access")
        
        return Observable.create { observer in
            
            AF.request(url,
                       method: .get,
                       headers: ["Authorization": "Bearer \(access ?? "")"])
            .validate(statusCode: 200..<201)
            .responseDecodable(of: RepositoriesInOrganizationModel.self) { res in
                print(res)
                switch res.result{
                case .success(let data):
                    observer.onNext(data)
                case .failure(let error):
                    print("RepositoriesInOrganizationService error!\n\(error)")
                }
            }
            
            return Disposables.create()
        }
    }
}
