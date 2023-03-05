//
//  UserInfoViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/05.
//

import Foundation
import RxSwift
import RxCocoa

final class ALLUserInfoViewModel{
    
    // UserInfoService 통신 데이터
    
    var sortedDoneUserArray: [UserInfoModel]?
    var allRankingobservable: BehaviorSubject<[UserInfoModel]> = BehaviorSubject(value: [])
    var pageCount = 0
    // 커밋에 대해서 내림차순 정렬
    private func sortedAboutCommits(){
        var userInfoArray = ALLUserInfoService.sharedData.resultArray
        sortedDoneUserArray = userInfoArray.sorted { return $0.tokens > $1.tokens }
    }
    
    func getDataRanking(){
        ALLUserInfoService.sharedData.getMemberInfo(page: pageCount, size: 20)
        pageCount += 1
    }
    
    func userInfoIntoObeservable(){
        allRankingobservable = BehaviorSubject(value: [])
        sortedAboutCommits()
        guard let sortedDoneUserArray = sortedDoneUserArray else { return }
        allRankingobservable.onNext(sortedDoneUserArray)
    }
    
    
    
}
