//
//  GithubLoginService.swift
//  ios
//
//  Created by 정호진 on 2023/03/13.
//

import Foundation
import Alamofire
import RxSwift

// 안씀 삭제 예정
final class GithubLoginService{
    let ip = APIURL.ip
    
    func githubPost(clientId: String, secretCode: String, code: String) -> Observable<String>{
        
        let url = APIURL.apiUrl.githubPostAPI()
        let parameters = ["client_id": clientId,
                          "client_secret": secretCode,
                          "code": code]
        let headers: HTTPHeaders = ["Accept": "application/json",
                                    "Authorization": "Bearer \(Environment.jwtToken ?? "")"]
        
        return Observable.create(){ Observer in
            AF.request(url,
                       parameters: parameters,
                       headers: headers)
            .validate(statusCode: 200..<201)
            .responseDecodable(of: GithubTokenDecodingModel.self) { response in
                switch response.result{
                case .success(let data):
                    Observer.onNext(data.access_token);
                case .failure(let error):
                    print("github post 통신 에러\n \(error)")
                }
            }
            return Disposables.create()
        }
    }
}
