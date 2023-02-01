//
//  SearchPageService.swift
//  ios
//
//  Created by 홍길동 on 2023/01/27.
//

import Foundation
import Alamofire
import SwiftyJSON

class SearchPageService {
    var repo1 = "DragonGuard-JinJin"
    var ip = ""
    var viewModel = SearchPageViewModel()
    var resultArray = [SearchPageResultModel]()
    
    func getPage() {
        let url = "http://\(ip)/scrap/search?page=1&name=\(repo1)&type=repositories"
        AF.request(url)
            .validate(statusCode: 200..<300)
            .responseDecodable(of: SearchPageDecodingModel.self) { response in
                guard let responseResult = response.value?.result else {return}
                for i in responseResult {
                    let j = SearchPageResultModel(name: i.name)
                    self.resultArray.append(j)
                }
                self.viewModel.searchResult.onNext(self.resultArray)
            }
    }
}



