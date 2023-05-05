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
    let adminService = AdminService()
    let disposeBag = DisposeBag()
    
    private init(){}
    
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
    
    // MARK: 요청된 조직 리스트 가져오는 함수
    func getOrganizationList(status: String) -> Observable<[AdminModel]>{
        return Observable.create { obsever in
            self.adminService.getOrganizationList(status: status)
                .subscribe { data in
                    obsever.onNext(data)
                }
                .disposed(by: self.disposeBag)
            return Disposables.create()
        }
    }
    
}
