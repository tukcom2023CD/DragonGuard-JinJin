//
//  RankingInOrganizationService.swift
//  ios
//
//  Created by 정호진 on 2023/04/05.
//

import Foundation
import Alamofire
import RxSwift

// MARK: 조직 내부 사용자 랭킹 가져오는 함수
final class RankingInOrganizationService{
    
    // MARK: 사용자가 속한 조직 Id 가져오는 함수
    func getOrganizationId(name: String) -> Observable<Int>{
        let url = APIURL.apiUrl.getOrganizationId(ip: APIURL.ip,
                                                  name: name)
        
        let encodedString = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
        return Observable.create{ observer in
            
            AF.request(encodedString,
                       method: .get,
                       encoding: JSONEncoding.default,
                       headers: [
                        "Content-Type": "application/json",
                        "Authorization" : "Bearer \(Environment.jwtToken ?? "")"
                       ])
            .responseDecodable(of: OrganizatoinIdCodableModel.self){ response in
                switch response.result{
                case .success(let data):
                    observer.onNext(data.id)
                case .failure(let error):
                    print("getOrganizationId error!\n \(error)")
                }
            }
            
            return Disposables.create()
        }
    }
    
    // MARK: 사용자가 속한 조직 멤버 리스트 가져오는 함수
    func getMemberInOrganization(id: Int, page: Int, size: Int)-> Observable<[MemberInOrganizationModel]>{
        let url = APIURL.apiUrl.organizationInMyRanking(ip: APIURL.ip,
                                                        id: id,
                                                        page: page,
                                                        size: size)
        var result: [MemberInOrganizationModel] = []
        return Observable.create{ observer in
          
            AF.request(url,
                       method: .get,
                       encoding: JSONEncoding.default
                       ,headers: [
                        "Authorization" : "Bearer \(Environment.jwtToken ?? "")"
                       ])
            .responseDecodable(of: [MemberInOrganizationDecodingModel].self) { response in
                print(response)
                switch response.result{
                case .success(let data):
                    data.forEach { data in
                        result.append(MemberInOrganizationModel(id: data.id,
                                                                name: data.name,
                                                                githubId: data.githubId,
                                                                tokens: data.tokens,
                                                                tier: data.tier))
                    }
                    observer.onNext(result)
                case .failure(let error):
                    print("getMemberInOrganization error!\n \(error)")
                }
            }
            return Disposables.create()
        }
    }
    
}
