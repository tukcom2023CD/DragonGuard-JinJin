//
//  SearchPageService.swift
//  ios
//
//  Created by 홍길동 on 2023/01/27.
//

import Foundation
import Alamofire
import RxSwift

final class SearchPageService {
    let ip = APIURL.ip
    
    /// 검색 API 통신하는 함수
    /// - Parameters:
    ///   - searchWord: 사용자가 검색한 단어
    ///   - page: 검색 결과 페이지
    func getSearchResult(searchWord: String,
                         page: Int,
                         type: String,
                         filtering: String) -> Observable<[SearchPageResultModel]>{
        let url = APIURL.apiUrl.getSearchResult(ip: ip, title: searchWord,page: page, type: type, filtering: filtering)
        let access = UserDefaults.standard.string(forKey: "Access")

        print("SearchUrl \(url)")
        return Observable.create(){ observer in
            AF.request(url, method: .get, headers: ["Authorization": "Bearer \(access ?? "")"])
                .validate(statusCode: 200..<201)
                .responseDecodable(of: [SearchPageDecodingData].self) { response in
                    guard let responseResult = response.value else {return}
                    var resultArray = [SearchPageResultModel]() // 결과 저장할 변수
                    
                    if(responseResult.count != 0 && resultArray.count == 0){
                        for data in responseResult {
                            let dataBundle = SearchPageResultModel(name: data.name,id: data.id)
                            resultArray.append(dataBundle)
                        }
                        observer.onNext(resultArray)
                    }
                }
            
            return Disposables.create()
        }
    }
}
