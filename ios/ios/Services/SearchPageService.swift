//
//  SearchPageService.swift
//  ios
//
//  Created by 홍길동 on 2023/01/27.
//

import Foundation
import Alamofire

final class SearchPageService {
    let ip = APIURL.ip
    var resultArray = [SearchPageResultModel]() // 결과 저장할 변수
    var timerThread: Timer?
    var checkData:Bool = false

    func getSearchResult(searchWord: String,page: Int) {
        resultArray = []
        let url = APIURL.testUrl(ip: ip, page: page, searchWord: searchWord)
        checkData = false
        
        AF.request(url, method: .get)
            .validate(statusCode: 200..<201)
            .responseDecodable(of: [SearchPageDecodingData].self) { response in
                guard let responseResult = response.value else {return}
                if(responseResult.count != 0 && self.resultArray.count == 0){
                    self.checkData = true
                    for data in responseResult {
                        let dataBundle = SearchPageResultModel(name: data.name,id: data.id)
                        self.resultArray.append(dataBundle)
                    }
                }
            }
    }
    
}
