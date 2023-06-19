//
//  RepoDetailViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/06/20.
//

import Foundation
import RxSwift

final class RepoDetailViewModel{
    private let disposeBag = DisposeBag()
    let service = RepoDetailService()
    
    func getData(title: String) -> Observable<DetailRepoModel>{
        
        return Observable.create { observer in
            self.service.getData(title: title)
                .subscribe(onNext:{ data in
                    observer.onNext(data)
                })
                .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
    
}
