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
//        RepoContributorInfoService.repoShared.getRepoContriInfo()
        RepoContributorInfoService.repoShared.testInput()
    }
    
    // api 결과값을 view로 전달
    func serviceToView(){
        repoResultBehaviorSubject.onNext(RepoContributorInfoService.repoShared.resultData)
        
        Timer.scheduledTimer(withTimeInterval: 1, repeats: true, block: { timer in
            if RepoContributorInfoService.repoShared.checkData {
                print("viewmodel 타이머 실행 중!")
                self.checkData = true
                timer.invalidate()
            }
        })
    }
    
}
