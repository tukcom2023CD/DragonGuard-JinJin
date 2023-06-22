//
//  BlockChainService.swift
//  ios
//
//  Created by 정호진 on 2023/06/22.
//

import Foundation
import RxSwift
import Alamofire

final class BlockChainService{
    
    func getData() -> Observable<[BlockChainListModel]>{
        let url = APIURL.apiUrl.getBlockChain(ip: APIURL.ip)
        let access = UserDefaults.standard.string(forKey: "Access")
        var result: [BlockChainListModel] = []
        return Observable.create { observer in
            AF.request(url,
                       method: .get,
                       headers: ["Content-type": "application/json",
                                     "Authorization": "Bearer \(access ?? "")"])
            .responseJSON { response in
                guard let res = response.data else {return}
                do {
                    let decoder = JSONDecoder()
                    let json = try decoder.decode([BlockChainListCodableModel].self, from: res)
                    print(json)
                    json.forEach { data in
                        result.append(BlockChainListModel(contributeType: data.contributeType,
                                                          amount: data.amount,
                                                          githubId: data.githubId,
                                                          createdAt: data.createdAt,
                                                          transactionHashUrl: data.transactionHashUrl))
                    }
                    observer.onNext(result)
                } catch {
                    print("error!\(error)")
                }
                
            }
//            .responseDecodable(of: [BlockChainListCodableModel].self) { response in
//                switch response.result{
//
//                case .success(let data):
//                    data.forEach { a in
//                        result.append(BlockChainListModel(contributeType: a.contributeType,
//                                                          amount: a.amount,
//                                                          githubId: a.githubId,
//                                                          createdAt: a.createdAt,
//                                                          transactionHashUrl: a.transactionHashUrl))
//                    }
//                    observer.onNext(result)
//
//
//                case .failure(let error):
//                    print("BlockChainService error!\n\(error)")
//                }
//
//            }
            
            return Disposables.create()
        }
        
    }
    
}
