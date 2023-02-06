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
    var timerThread: Timer?
    var checkData:Bool = false
    
    
    func getSearchResult(searchWord: String,page: Int) {
        resultArray = []
        let url = APIURL.testUrl(ip: ip, page: page, searchWord: searchWord)
        checkData = false
        
        timerThread = Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { timer in
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
            
            if self.checkData{
                timer.invalidate()
            }
            Thread.sleep(forTimeInterval: 1)
        })
        
        
        
    }
    
}
