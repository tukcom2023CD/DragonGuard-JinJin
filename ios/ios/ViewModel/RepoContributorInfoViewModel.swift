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
    var selectTitle = ""
    
    // API 호출
    func getRepoContributorInfo(){
        repoResultBehaviorSubject = BehaviorSubject(value: [])
        RepoContributorInfoService.repoShared.getRepoContriInfo()
    }
    
    // api 결과값을 view로 전달
    func serviceToView(){
        Timer.scheduledTimer(withTimeInterval: 0.3, repeats: true, block: { timer in
            if RepoContributorInfoService.repoShared.checkData &&  RepoContributorInfoService.repoShared.resultData.count > 0{
                self.repoResultBehaviorSubject.onNext(RepoContributorInfoService.repoShared.resultData)
                self.selectTitle = RepoContributorInfoService.repoShared.selectedName
                self.checkData = true
                timer.invalidate()
            }
        })
    }
    
}
