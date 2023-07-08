//
//  DetailInfoViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/07/06.
//

import Foundation
import RxSwift

final class DetailInfoViewModel{
    let service = DetailInfoService()
    private let disposeBag = DisposeBag()
    
    func getData() -> Observable<DetailInfoModel>{
        return Observable.create { observer in
            self.service.getData()
                .subscribe(onNext:{ data in
                    observer.onNext(data)
                })
                .disposed(by: self.disposeBag)
            
            
            return Disposables.create()
        }
    }
    
}
