//
//  MainViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/06.
//

import Foundation
import RxSwift
import RxCocoa

// 메인에 사용되는 자신의 랭킹, 커밋 개수, 이슈 개수,
final class MainViewModel {
    let service = MainService()
    let disposeBag = DisposeBag()
    
    func getMyInformation() -> Observable<MainModel>{
        return Observable<MainModel>.create(){ observer in
            
            self.service.updateProfile().subscribe(onNext: { check in
                if check{
                    self.service.getUserInfo()
                        .subscribe(onNext: { info in
                            observer.onNext(info)
                        })
                        .disposed(by: self.disposeBag)
                }
            })
            .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
    
    
    
}
