//
//  BlockchainViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/06/22.
//

import Foundation
import RxSwift

final class BlockchainViewModel{
    private let service = BlockChainService()
    private let disposeBag = DisposeBag()
    
    func getData() -> Observable<[BlockChainListModel]>{
        
        return Observable.create { observer in
            self.service.getData().subscribe(onNext: { list in
                observer.onNext(list)
            })
            .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
        
    }
    
}
