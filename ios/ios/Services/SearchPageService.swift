//
//  SearchPageService.swift
//  ios
//
//  Created by 홍길동 on 2023/01/27.
//

import Foundation
import Alamofire
import SwiftyJSON
import RxCocoa
import RxSwift

class SearchPageService {
    
    var ip = "192.168.0.14"
    var resultArray = [SearchPageResultModel]()
    
    func getPage(searchWord: String) {
        resultArray = []
        let url = "http://\(ip)/scrap/search?page=1&name=\(searchWord)&type=repositories"
        DispatchQueue.global().async {
            AF.request(url)
                .validate(statusCode: 200..<300)
                .responseDecodable(of: SearchPageDecodingModel.self) { response in
                    guard let responseResult = response.value?.result else {return}
                    for i in responseResult {
                        print("i.name:  \(i.name)")
                        let j = SearchPageResultModel(name: i.name)
                        self.resultArray.append(j)
                    }
                }
        }
        
    }
}
