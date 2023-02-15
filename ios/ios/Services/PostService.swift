//
//  PostService.swift
//  ios
//
//  Created by 정호진 on 2023/02/15.
//

import Foundation
import Alamofire

class PostService{
    static let postService = PostService()
    let url = APIURL.inputDBMembers()
    let myGithubId = ["githubId" : "HJ39"] as Dictionary
    var data = 0
    private init(){}
    
    func postMyInfo(){
        AF.request(url,
                   method: .post,
                   parameters: myGithubId,
                   encoding: JSONEncoding(options: []),
                   headers: ["Content-type": "application/json"])
        .responseString{ response in
            
            switch response.result{
            case .success(let data):
                self.data = Int(data) ?? 0
            case .failure(let error):
                print("삐리삐리 에러발생 \(error)")
                
            }
            
        }
    }
    
}
