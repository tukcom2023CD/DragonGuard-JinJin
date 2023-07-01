//
//  RepoDetailService.swift
//  ios
//
//  Created by 정호진 on 2023/06/20.
//

import Foundation
import Alamofire
import RxSwift

final class RepoDetailService{
    private let disposeBag = DisposeBag()
    
    func getData(title: String) -> Observable<DetailRepoModel>{
        let url = APIURL.apiUrl.getRepoContributorInfo(ip: APIURL.ip, name: title)
        print("repo detail \(url)")
        let access = UserDefaults.standard.string(forKey: "Access")
        
        return Observable.create { observer in
            Timer.scheduledTimer(withTimeInterval: 3, repeats: true, block: { timer in
                AF.request(url,
                           method: .get,
                           headers: ["Authorization": "Bearer \(access ?? "")"])
                .validate(statusCode: 200..<201)
                .responseDecodable(of: DetailRepoModel.self) { res in
                    print("RepoDetailService\n\(res)")
                    switch res.result{
                    case .success(let data):
                        if !(data.git_repo_members?.isEmpty ?? true) && !(data.spark_line?.isEmpty ?? true){
                            observer.onNext(data)
                            timer.invalidate()
                        }
                    case .failure(let error):
                        print("RepoDetailService error!\(error)")
                    }
                }
            })
            return Disposables.create()
        }
    }
    
    
}
