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
    static let viewModel = CompareViewModel()
    var firstRepo = ""
    var secondRepo = ""
    var repositryInfo: BehaviorSubject<[String]> = BehaviorSubject(value: [])
    var repoUserInfo: BehaviorSubject<CompareUserModel> = BehaviorSubject(value: CompareUserModel(firstResult: [], secondResult: []))
    var repo1Info: BehaviorSubject<[FirstRepoModel]> = BehaviorSubject(value: [])
    var repo2Info: BehaviorSubject<[secondRepoModel]> = BehaviorSubject(value: [])
    let service = CompareService()
    let disposeBag = DisposeBag()
    var sendData: BehaviorSubject<CompareUserModel> = BehaviorSubject(value: CompareUserModel(firstResult: [], secondResult: []))
    var compareUser: CompareUserModel?
    
//    func callAPI(){
//        repositryInfo.subscribe(onNext: {
//            self.firstRepo = $0[0]
//            self.secondRepo = $0[1]
//        })
//        .disposed(by: disposeBag)
//        CompareService.compareService.beforeSendingInfo(firstRepo: self.firstRepo, secondRepo: self.secondRepo)
//    }
//
//    func bringUserInfo(){
//        if CompareService.compareService.repoUserInfo?.firstResult.count != 0{
//            self.repoUserInfo.onNext(CompareUserModel(firstResult: CompareService.compareService.firstRepoUserInfo, secondResult: CompareService.compareService.secondRepoUserInfo))
//        }
//    }
//
//    func bringRepoInfo(){
//        repo1Info = BehaviorSubject(value: [])
//        repo2Info = BehaviorSubject(value: [])
//        if CompareService.compareService.firstRepoInfo.count != 0 && CompareService.compareService.secondRepoInfo.count != 0{
//            self.repo1Info.onNext(CompareService.compareService.firstRepoInfo)
//            self.repo2Info.onNext(CompareService.compareService.secondRepoInfo)
//        }
//    }
//
    
    func getContributorInfo(firstRepoName: String, secondRepoName: String) -> Observable<CompareUserModel>{
        
        return Observable.create(){ observer in
            self.service.beforeSendingInfo(firstRepo: firstRepoName, secondRepo: secondRepoName)
                .subscribe(onNext: { contributorInfo in
                    observer.onNext(contributorInfo)
                    self.compareUser = contributorInfo
                    self.sendData.onNext(contributorInfo)
                })
                .disposed(by: self.disposeBag)
            return Disposables.create()
        }
    }
    
    func getUserInfo() -> Observable<CompareUserModel> {
        
        return Observable.create(){ observer in
//            guard let compareUser = self.compareUser else {return}
//            observer.onNext(compareUser)
            
            self.sendData.subscribe(onNext: { data in
                print("Called")
                print(data)
                observer.onNext(data)
            }).disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
    
}
