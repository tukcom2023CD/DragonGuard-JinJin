//
//  AllRankingViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import RxSwift

final class AllRankingViewModel{
    static let shared = AllRankingViewModel()
    private var topTierData: [AllUserRankingModel] = []
    private var userTierData: [AllUserRankingModel] = []
    
    var firstRankSubject: PublishSubject<AllUserRankingModel> = PublishSubject()
    var secondRankSubject: PublishSubject<AllUserRankingModel> = PublishSubject()
    var thirdRankSubject: PublishSubject<AllUserRankingModel> = PublishSubject()
    
    // MARK: 데이터 가져옴
    func loadData(){
        
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo1", num: 344, link: "s"))
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo2", num: 22, link: "s"))
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo3", num: 333, link: "s"))
        
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo4", num: 333, link: "s"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo5", num: 333, link: "s"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo6", num: 333, link: "s"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo7", num: 333, link: "s"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo8", num: 333, link: "s"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo9", num: 333, link: "s"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo10", num: 333, link: "s"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo11", num: 333, link: "s"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo12", num: 333, link: "s"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo13", num: 333, link: "s"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo14", num: 333, link: "s"))
        
        firstRankSubject.onNext(topTierData[0])
        secondRankSubject.onNext(topTierData[1])
        thirdRankSubject.onNext(topTierData[2])
    }
    
    
}
