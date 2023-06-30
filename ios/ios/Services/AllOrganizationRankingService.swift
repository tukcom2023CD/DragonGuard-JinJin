////
////  AllOrganizationRankingService.swift
////  ios
////
////  Created by 정호진 on 2023/04/04.
////
//
//import Foundation
//import Alamofire
//import RxSwift
//
//// MARK: 전체 조직 랭킹 가져오는 클래스
//final class AllOrganizationRankingService{
//
//    // MARK: 전체 조직 랭킹 가져오는 함수
//    func getAllOrganiRanking() -> Observable<[AllOrganizationRankingModel]>{
//        let url = APIURL.apiUrl.allOrganizationRanking(ip: APIURL.ip)
//        var result: [AllOrganizationRankingModel] = []
//        let access = UserDefaults.standard.string(forKey: "Access")
//
//        return Observable.create { observer in
//
//            AF.request(url,
//                       method: .get,
//                       encoding: JSONEncoding.default,
//                       headers: [
//                       "Content-Type" : "application/json",
//                       "Authorization" : "Bearer \(access ?? "")"
//                       ])
//            .responseDecodable(of: [AllOrganizationRankingDecodingModel].self) { response in
////                print(response)
//                switch response.result{
//                case .success(let data):
//                    data.forEach { data in
//                        result.append(AllOrganizationRankingModel(id: data.id,
//                                                                  name: data.name,
//                                                                  organizationType: data.organizationType,
//                                                                  emailEndpoint: data.emailEndpoint,
//                                                                  tokenSum: data.tokenSum))
//                    }
//                    observer.onNext(result)
//                case .failure(let error):
//                    print("getAllOrganiRanking error!\n \(error)")
//                }
//            }
//
//
//            return Disposables.create()
//        }
//    }
//
//
//}
