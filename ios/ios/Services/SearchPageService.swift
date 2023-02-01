//
//  SearchPageService.swift
//  ios
//
//  Created by 홍길동 on 2023/01/27.
//

import Foundation
import Alamofire

class SearchPageService {
    var repo1 = "DragonGuard-JinJin"
    func getPage() {
        let url = "https://api.visitjeju.net/vsjApi/contents/searchList?apiKey=&${rrq71a2rotyj9tqm}&locale=kr&page="
        AF.request(url, method: .get, parameters: nil, encoding: URLEncoding.default, headers: ["Content-Type":"application/json", "Accept":"application/json"])
            .validate(statusCode: 200..<300)
            .responseJSON { json in
//            print(json)
//                print("hello")
            }
    }
}



