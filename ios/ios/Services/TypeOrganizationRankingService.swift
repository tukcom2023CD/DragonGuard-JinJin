//
//  TypeOrganizationRankingService.swift
//  ios
//
//  Created by 정호진 on 2023/04/05.
//

import Foundation
import Alamofire
import RxSwift

// MARK: 타입 별 조직 랭킹 가져옴
final class TypeOrganizationRankingService{
     
    func getTypeOrganizationRanking(type: String, page: Int, size: Int) -> Observable<[AllOrganizationRankingModel]>{
        let url = APIURL.apiUrl.typeFilterOrganiRanking(ip: APIURL.ip,
                                                        type: type,
                                                        page: page,
                                                        size: size)
        var result: [AllOrganizationRankingModel] = []
        return Observable.create{ observer in
          
            AF.request(url,
                       method: .get,
                       headers: [
                        "Content-Type": "application/json",
                        "Authorization" : "Bearer \(Environment.jwtToken ?? "")"
                       ])
            .responseDecodable(of: [AllOrganizationRankingDecodingModel].self) { response in
                print(response)
                switch response.result{
                case .success(let data):
                    data.forEach { data in
                        result.append(AllOrganizationRankingModel(id: data.id,
                                                                  name: data.name,
                                                                  organizationType: data.organizationType,
                                                                  emailEndpoint: data.emailEndpoint,
                                                                  tokenSum: data.tokenSum))
                    }
                    
                case .failure(let error):
                    print("getTypeOrganizationRanking error!\n \(error)")
                }
                
                observer.onNext(result)
            }
            return Disposables.create()
        }
    }
    
}
