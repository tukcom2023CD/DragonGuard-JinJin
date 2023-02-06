//
//  UserInfoViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/05.
//

import Foundation
import RxSwift
import RxCocoa

final class UserInfoViewModel{
    
    // UserInfoService 통신 데이터
    var userInfoArray = UserInfoService.sharedData.resultArray
    var sortedDoneUserArray: [UserInfoModel]?
    var allRankingobservable: BehaviorSubject<[UserInfoModel]> = BehaviorSubject(value: [])
    
    // 커밋에 대해서 내림차순 정렬
    private func sortedAboutCommits(){
        sortedDoneUserArray = userInfoArray.sorted { return $0.commits > $1.commits }
    }
    
    func userInfoIntoObeservable(){
        print("sorted Array: \(sortedDoneUserArray)")
        guard let sortedDoneUserArray = sortedDoneUserArray else { return }
        allRankingobservable.onNext(sortedDoneUserArray)
    }
    
    
    
    
    
}
