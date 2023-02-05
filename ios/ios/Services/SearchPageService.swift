//
//  SearchPageService.swift
//  ios
//
//  Created by 홍길동 on 2023/01/27.
//

import Foundation
import Alamofire

class SearchPageService {
    let ip = APIURL.ip
    var resultArray = [SearchPageResultModel]() // 결과 저장할 변수
    
    
    func getSearchResult(searchWord: String,page: Int) {
        resultArray = []
        let url = APIURL.testUrl(ip: ip, page: page, searchWord: searchWord)
        
        DispatchQueue.global().async {
            AF.request(url)
                .validate(statusCode: 200..<300)
                .responseDecodable(of: SearchPageDecodingModel.self) { response in
                    guard let responseResult = response.value?.result else {return}
                    for data in responseResult {
                        let dataBundle = SearchPageResultModel(name: data.name)
                        self.resultArray.append(dataBundle)
                    }
                }
        }
        
    }
}
