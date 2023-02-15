//
//  RepoContributorInfoViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/08.
//

import Foundation
import RxCocoa
import RxSwift

final class RepoContributorInfoViewModel{
    var checkData = false
    var repoResultBehaviorSubject: BehaviorSubject<[RepoContributorInfoModel]> = BehaviorSubject(value: [])
    
    
    // API 호출
    func getRepoContributorInfo(){
        repoResultBehaviorSubject = BehaviorSubject(value: [])
        RepoContributorInfoService.repoShared.getRepoContriInfo()
    }
    
    // api 결과값을 view로 전달
    func serviceToView(){
        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true, block: { timer in
            if RepoContributorInfoService.repoShared.checkData {
                self.repoResultBehaviorSubject.onNext(RepoContributorInfoService.repoShared.resultData)
                self.checkData = true
                timer.invalidate()
            }
        })
    }
    
}
