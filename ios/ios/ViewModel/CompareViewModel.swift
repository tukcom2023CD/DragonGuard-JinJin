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
    var repoUserInfo: BehaviorSubject<CompareUserModel> = BehaviorSubject(value: CompareUserModel(firstResult: [], secondResult: []))
    var repo1Info: BehaviorSubject<[FirstRepoModel]> = BehaviorSubject(value: [])
    var repo2Info: BehaviorSubject<[secondRepoModel]> = BehaviorSubject(value: [])
    let disposeBag = DisposeBag()
    
    func callAPI(){
        repositryInfo.subscribe(onNext: {
            self.firstRepo = $0[0]
            self.secondRepo = $0[1]
        })
        .disposed(by: disposeBag)
        CompareService.compareService.beforeSendingInfo(firstRepo: self.firstRepo, secondRepo: self.secondRepo)
    }
    
    func bringUserInfo(){
        if CompareService.compareService.repoUserInfo?.firstResult.count != 0{
            self.repoUserInfo.onNext(CompareUserModel(firstResult: CompareService.compareService.firstRepoUserInfo, secondResult: CompareService.compareService.secondRepoUserInfo))
        }
    }
    
    func bringRepoInfo(){
        if CompareService.compareService.firstRepoInfo.count != 0 && CompareService.compareService.secondRepoInfo.count != 0{
            self.repo1Info.onNext(CompareService.compareService.firstRepoInfo)
            self.repo2Info.onNext(CompareService.compareService.secondRepoInfo)
        }
    }
}
