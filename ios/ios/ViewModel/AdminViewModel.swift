//
//  AdminViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/05/05.
//

import Foundation
import RxSwift

final class AdminViewModel {
    static let admin = AdminViewModel()
    let checkService = AdminCheckService()
    let disposeBag = DisposeBag()
    
    // MARK: 관리자 확인하는 API
    func checkAdmin() -> Observable<Bool>{
        return Observable.create ({ observer in
            self.checkService.adminCheck()
                .subscribe(onNext: { check in
                    observer.onNext(check)
                })
                .disposed(by: self.disposeBag)
            
            return Disposables.create()
        })
    }
    
    
    
    
}
