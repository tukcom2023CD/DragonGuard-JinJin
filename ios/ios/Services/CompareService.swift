//
//  CompareService.swift
//  ios
//
//  Created by 정호진 on 2023/02/24.
//

import Foundation
import Alamofire

final class CompareService{
    static let compareService = CompareService()
    let ip = APIURL.ip
    var firstRepo: String?
    var secondRepo: String?
    
    private init(){ }
    
    @objc func notificationData(notification: Notification){
        guard let repo1 = notification.userInfo?[NotificationRepos.repo1] as? String else {return}
        guard let repo2 = notification.userInfo?[NotificationRepos.repo2] as? String else {return}
        
//        print(firstRepo)
//        print(secondRepo)
        self.firstRepo = repo1
        self.secondRepo = repo2
    }
    
    func getCompareInfo(){
        NotificationCenter.default.addObserver(self, selector: #selector(notificationData(notification:)), name: Notification.Name.compareRepo, object: nil)
        
        guard let firstRepo = self.firstRepo else {return}
        guard let secondRepo = self.secondRepo else {return}
        
        print("first: \(firstRepo), second: \(secondRepo)")
        let url = APIURL.apiUrl.compareRepoAPI(ip: ip)
        let body = ["firstRepo": firstRepo, "secondRepo": secondRepo]
        
        AF.request(url,
                   method: .post,
                   parameters: body,
                   encoding: JSONEncoding(options: []),
                   headers: ["Content-type": "application/json"])
        .validate(statusCode: 200..<201)
        .responseDecodable(of: CompareRepoModel.self) { response in
            print("response: \(response)")
        }
        
    }
    
    
    
}
