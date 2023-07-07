//
//  SearchOraganizationService.swift
//  ios
//
//  Created by 정호진 on 2023/03/23.
//

import Foundation
import Alamofire
import RxSwift

// MARK: 조직 검색하는 클래스
final class SearchOraganizationService {
    
    
    func getOrganizationListService(name: String, type: String, page: Int, size: Int) -> Observable<[SearchOrganizationListModel]>{
        
        let url = APIURL.apiUrl.searchOrganizationList(ip: APIURL.ip,
                                                       name: name,
                                                       type: type,
                                                       page: page,
                                                       size: size)
        
        let encodedString = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
        print(url)
        let access = UserDefaults.standard.string(forKey: "Access")

        return Observable.create(){ observer in
            AF.request(encodedString,
                       method: .get,
                       encoding: JSONEncoding.default,
                       headers: ["Authorization": "Bearer \(access ?? "")"])
            .validate(statusCode: 200..<201)
            .responseDecodable(of:[SearchOrganizationListModel].self) { res in
                print(res)
                switch res.result{
                case .success(let data):
                    observer.onNext(data)
                case .failure(let error):
                    print("getOrganizationListService error!\n\(error)")
                }
            }
            
            return Disposables.create()
        }
    }
    
    
}
