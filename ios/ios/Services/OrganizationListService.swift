//
//  OrganizationListService.swift
//  ios
//
//  Created by 정호진 on 2023/07/15.
//

import Foundation
import RxSwift
import Alamofire

final class OrganizationListService{
    
    func getMemberList(page: Int, size: Int, organizationId: Int) -> Observable<[AllUserRankingModel]>{
        let url = APIURL.apiUrl.organizationInMyRanking(ip: APIURL.ip, id: organizationId, page: page, size: size)
        let access = UserDefaults.standard.string(forKey: "Access")
        
        return Observable.create { observer in
            AF.request(url,
                       headers: ["Authorization": "Bearer \(access ?? "")"])
                .validate(statusCode: 200..<201)
                .responseDecodable(of: [AllUserRankingModel].self) { response in
                    print("getMemberList\n")
                    print(response)
                    switch response.result{
                    case .success(let data):
                        observer.onNext(data)
                    case .failure(let error):
                        print("getMemberInfo\n\(error)")
                    }
                }
            return Disposables.create()
        }
    }
    
}
