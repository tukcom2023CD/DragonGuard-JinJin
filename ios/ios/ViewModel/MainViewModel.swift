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
class MainViewModel {
    var myInfoObservable = BehaviorSubject(value: MainModel(id: 0, name: "", githubId: "", commits: 0, tier: "", authStep: "", profileImage: "",rank: 0))
    
    // 서버로부터 api 데이터 받아옴
    func getMyInfo(){
        MainService.mainService.getUserInfo(id: APIURL.myDbId)
    }
    
    // view에 적용
    func myInfoIntoObservable(){
        let resultArray = MainService.mainService.result
        guard let result = resultArray else { return }
        self.myInfoObservable.onNext(result)
    }
    
}
