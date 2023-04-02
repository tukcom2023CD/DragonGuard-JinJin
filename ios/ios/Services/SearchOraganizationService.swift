//
//  SearchOraganizationService.swift
//  ios
//
//  Created by 정호진 on 2023/03/23.
//

import Foundation
import Alamofire
import RxSwift

// MARK: 조직 검색하는 클래스
final class SearchOraganizationService {
    
    
    func getOrganizationListService(name: String, type: String, page: Int, size: Int) -> Observable<[SearchOrganizationListModel]>{
        
        let url = APIURL.apiUrl.searchOrganizationList(ip: APIURL.ip,
                                                       name: name,
                                                       type: type,
                                                       page: page,
                                                       size: size)
        
        let encodedString = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
        
        var result: [SearchOrganizationListModel] = []
        return Observable.create(){ observer in
            AF.request(encodedString,
                       method: .get,
                       encoding: JSONEncoding.default,
                       headers: ["Authorization": "Bearer \(Environment.jwtToken ?? "")"])
            .validate(statusCode: 200..<201)
            .responseData { response in
                switch response.result {
                case .success(let data):
                    do {
                        if let jsonArray = try JSONSerialization.jsonObject(with: data, options: []) as? [Any],
                           let singleObjectArray = jsonArray.first as? [String: Any] {
                                // NSSingleObjectArrayI에서 객체를 가져옴
                                let jsonData = try JSONSerialization.data(withJSONObject: singleObjectArray)
                                let decoder = JSONDecoder()
                                let object = try decoder.decode(SearchOrganizationListDecodingModel.self, from: jsonData)
                                print(object)
                            }
                    } catch {
                        print(error.localizedDescription)
                    }
                case .failure(let error):
                    print(error.localizedDescription)
                }
            
            
//            .responseDecodable (of: SearchOrganizationListDecodingModel.self){ response in
//
//                switch response.result {
//                case .success(let data):
//                    print(data)
//                    result.append(SearchOrganizationListModel(id: data.id,
//                                                              name: data.name,
//                                                              type: data.type,
//                                                              emailEndpoint: data.emailEndpoint))
//                case .failure(let error):
//                    print("getOrganizationListService error!\n \(error)")
//                }
//
//                observer.onNext(result)

            }
            
            return Disposables.create()
        }
    }
    
    
}
