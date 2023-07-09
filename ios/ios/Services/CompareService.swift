//
//  CompareService.swift
//  ios
//
//  Created by 정호진 on 2023/02/24.
//

import Foundation
import Alamofire
import RxSwift

final class CompareService{
    let ip = APIURL.ip
    
    /// 선택된 레포지토리들을 보내 유저 정보들을 받아오는 함수
    /// - Parameters:
    ///   - firstRepo: 첫 번째 선택된 레포지토리
    ///   - secondRepo: 두 번째 선택된 레포지토리
    func beforeSendingInfo(firstRepo: String, secondRepo: String) -> Observable<CompareUserModel> {
        let url = APIURL.apiUrl.compareBeforeAPI(ip: ip)
        let body = ["first_repo": firstRepo, "second_repo": secondRepo]
        let access = UserDefaults.standard.string(forKey: "Access")
        
        print("beforeSendingInfo\n\(url)")
        print("body \(body)")
        return Observable.create(){ observer in
            Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { timer in
                AF.request(url,
                           method: .post,
                           parameters: body,
                           encoding: JSONEncoding(options: []),
                           headers: ["Content-type": "application/json",
                                     "Authorization": "Bearer \(access ?? "")"])
                .validate(statusCode: 200..<201)
                .responseDecodable(of: CompareUserModel.self) { res in

                    switch res.result{
                    case .success(let data):
                        if !data.first_result.isEmpty && !data.second_result.isEmpty{
                            observer.onNext(data)
                            timer.invalidate()
                        }
                    case .failure(let error):
                        print("beforeSendingInfo error!\n\(error)")
                    }

                }
            })
            return Disposables.create()
        }
        
    }
    
    
    
    /// 레포지토리 상세 정보를 요청하는 함수
    /// - Parameters:
    ///   - firstRepo: 첫 번쨰 선택된 레포 이름
    ///   - secondRepo: 두 번쨰 선택된 레포 이름
    func getCompareInfo(firstRepo: String, secondRepo: String) -> Observable<CompareRepoModel> {
        let url = APIURL.apiUrl.compareRepoAPI(ip: ip)
        let body = ["first_repo": firstRepo, "second_repo": secondRepo]
        let access = UserDefaults.standard.string(forKey: "Access")
        print("getCompareInfo\n\(url)")
        print("body \(body)")
        return Observable.create() { observer in
            Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { timer in
                AF.request(url,
                           method: .post,
                           parameters: body,
                           encoding: JSONEncoding.default,
                           headers: ["Content-type": "application/json",
                                     "Authorization": "Bearer \(access ?? "")"])
                .validate(statusCode: 200..<205)
                .responseDecodable(of: CompareRepoModel.self) { res in
                    print(res)
                    switch res.result{
                    case .success(let data):
                        if !data.first_repo.profile_urls.isEmpty && !data.second_repo.profile_urls.isEmpty{
                            observer.onNext(data)
                            timer.invalidate()
                        }
                    case .failure(let error):
                        print("getCompareInfo error!\n\(error)")
                    }
                }
            })
            return Disposables.create()
        }
        
    }
    
}
