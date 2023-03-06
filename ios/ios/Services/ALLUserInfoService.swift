//
//  UserInfoService.swift
//  ios
//
//  Created by 정호진 on 2023/02/05.
//

import Foundation
import Alamofire
import RxSwift

// 전체 유저 정보 통신
final class ALLUserInfoService{
    let ip = APIURL.ip
    
    /// 전체 랭킹 API 통신
    /// - Parameters:
    ///   - page: 유저 리스트 페이지
    ///   - size: 한번에 받아올 버퍼 크기
    func getMemberInfo(page: Int, size: Int) -> Observable<[UserInfoModel]>{
        let url = APIURL.apiUrl.getUserInfo(ip: ip, page: page, size: size)
        var resultArray = [UserInfoModel]()
        
        return Observable.create(){ observer in
            AF.request(url)
                .validate(statusCode: 200..<201)
                .responseDecodable(of: [UserInfoDecodingData].self) { response in
                    guard let responseResult = response.value else {return}
                    if(responseResult.count != 0 && resultArray.count == 0){
                        for data in responseResult {
                            let dataBundle = UserInfoModel(id: data.id, name: data.name ?? "unknown", githubId: data.githubId, tokens: data.tokens ?? 0, tier: data.tier)
                            resultArray.append(dataBundle)
                        }
                        print(resultArray)
                        observer.onNext(resultArray)
                    }
                }
            return Disposables.create()
        }
    }
    
}
