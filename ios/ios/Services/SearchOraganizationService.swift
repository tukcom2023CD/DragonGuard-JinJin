//
//  SearchOraganizationService.swift
//  ios
//
//  Created by 정호진 on 2023/03/23.
//

import Foundation
import Alamofire
import RxSwift

final class SearchOraganizationService {
    
    func getOrganizationListService(name: String, type: String, page: Int, size: Int) -> Observable<[SearchOrganizationListModel]>{
        
        let url = APIURL.apiUrl.searchOrganizationList(ip: APIURL.ip,
                                                       name: name,
                                                       type: type,
                                                       page: page,
                                                       size: size)
        var result: [SearchOrganizationListModel] = []
        
        return Observable.create(){ observer in
            AF.request(url,
                       method: .get,
                       headers: ["Authorization": "Bearer \(Environment.jwtToken ?? "")"])
            .validate(statusCode: 200..<201)
            .responseDecodable (of: SearchOrganizationListDecodingModel.self){ response in
                
                switch response.result {
                case .success(let data):
                    print(data)
                    result.append(SearchOrganizationListModel(name: data.name))
                case .failure(let error):
                    print("getOrganizationListService error!\n \(error)")
                }
                
                observer.onNext(result)
                
            }
            
            return Disposables.create()
        }
    }
    
    
}
