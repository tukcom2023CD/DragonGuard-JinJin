//
//  CompareViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/27.
//

import Foundation
import RxSwift
import RxCocoa

final class CompareViewModel{
    var firstRepo = ""
    var secondRepo = ""
    var repositryInfo: BehaviorSubject<[String]> = BehaviorSubject(value: [])
    var firstUserInfo: BehaviorSubject<[FirstRepoResult]> = BehaviorSubject(value: [])
    var secondUserInfo: BehaviorSubject<[SecondRepoResult]> = BehaviorSubject(value: [])
    let disposeBag = DisposeBag()
    
    func callAPI(){
        repositryInfo.subscribe(onNext: {
            self.firstRepo = $0[0]
            self.secondRepo = $0[1]
        })
        .disposed(by: disposeBag)
        print(self.firstRepo)
        print(self.secondRepo)
        CompareService.compareService.beforeSendingInfo(firstRepo: self.firstRepo, secondRepo: self.secondRepo)
    }
    
    func bringUserInfo(){
        if CompareService.compareService.firstRepoUserInfo.count != 0 && CompareService.compareService.secondRepoUserInfo.count != 0 {
            self.firstUserInfo.onNext(CompareService.compareService.firstRepoUserInfo)
            self.secondUserInfo.onNext(CompareService.compareService.secondRepoUserInfo)
        }
    }
}
