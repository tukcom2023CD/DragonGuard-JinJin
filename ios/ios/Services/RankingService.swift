//
//  UserInfoService.swift
//  ios
//
//  Created by 정호진 on 2023/02/05.
//

import Foundation
import Alamofire
import RxSwift

// MARK: 랭킹 API 통신
final class RankingService{
    
    /// 전체 랭킹 API 통신
    /// - Parameters:
    ///   - page: 유저 리스트 페이지
    ///   - size: 한번에 받아올 버퍼 크기
    func getMemberInfo(page: Int, size: Int) -> Observable<[AllUserRankingModel]>{
        let url = APIURL.apiUrl.getUserInfo(ip: APIURL.ip,
                                            page: page,
                                            size: size)
        let access = UserDefaults.standard.string(forKey: "Access")

        return Observable.create(){ observer in
            AF.request(url,
                       headers: ["Authorization": "Bearer \(access ?? "")"])
                .validate(statusCode: 200..<201)
                .responseDecodable(of: [AllUserRankingModel].self) { response in
                    print("getMemberInfo\n")
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
    
    /// 타입별 랭킹 API
    /// - Parameters:
    ///   - page: 타입 별 랭킹 리스트 페이지
    ///   - size: 한번에 받아올 버퍼 크기
    ///   - type: UNIVERSITY, HIGH_SCHOLL, COPMANY, ETC
    func rankingOfType(page: Int, size: Int, type: String) -> Observable<[TypeRankingModel]>{
        let url = APIURL.apiUrl.typeFilterOrganiRanking(ip: APIURL.ip,
                                                        type: type,
                                                        page: page,
                                                        size: size)
        let access = UserDefaults.standard.string(forKey: "Access")
        
        return Observable.create { observer in
            AF.request(url,
                       method: .get,
                       headers: ["Authorization": "Bearer \(access ?? "")"])
            .validate(statusCode: 200..<201)
            .responseDecodable(of: [TypeRankingModel].self) { res in
                print("rankingOfType \n\(res)")
                
                switch res.result{
                case .success(let list):
                    observer.onNext(list)
                case .failure(let error):
                    print("rankingOfType error!\n\(error)")
                }
            }
            
            return Disposables.create()
        }
    }
    
    /// 모든 타입 랭킹 API
    /// - Parameters:
    ///   - page: 모든 타입 랭킹 리스트 페이지
    ///   - size: 한번에 받아올 버퍼 크기
    func allRankingOfType(page: Int, size: Int) -> Observable<[TypeRankingModel]>{
        let url = APIURL.apiUrl.allOrganizationRanking(ip: APIURL.ip,
                                                       page: page,
                                                       size: size)
        let access = UserDefaults.standard.string(forKey: "Access")
        
        return Observable.create { observer in
            AF.request(url,
                       method: .get,
                       headers: ["Authorization": "Bearer \(access ?? "")"])
            .validate(statusCode: 200..<201)
            .responseDecodable(of: [TypeRankingModel].self) { res in
                print("rankingOfType \n\(res)")
                
                switch res.result{
                case .success(let list):
                    observer.onNext(list)
                case .failure(let error):
                    print("rankingOfType error!\n\(error)")
                }
            }
            
            return Disposables.create()
        }
    }
    
}
