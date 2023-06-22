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
//    var firstRepo = ""
//    var secondRepo = ""
//    var repositryInfo: BehaviorSubject<[String]> = BehaviorSubject(value: [])
//    var repoUserInfo: BehaviorSubject<CompareUserModel> = BehaviorSubject(value: CompareUserModel(firstResult: [], secondResult: []))
//    var repo1Info: BehaviorSubject<[FirstRepoModel]> = BehaviorSubject(value: [])
//    var repo2Info: BehaviorSubject<[secondRepoModel]> = BehaviorSubject(value: [])
    
    private let service = CompareService()
    private let disposeBag = DisposeBag()
    let checkChooseUser1: BehaviorSubject<Bool> = BehaviorSubject(value: false)
    private var checkUser1 = false {
        willSet{
            checkChooseUser1.onNext(newValue)
        }
    }
        
    let checkChooseUser2: BehaviorSubject<Bool> = BehaviorSubject(value: false)
    private var checkUser2 = false {
        willSet{
            checkChooseUser2.onNext(newValue)
        }
    }
    
//    var sendData: BehaviorSubject<CompareUserModel> = BehaviorSubject(value: CompareUserModel(firstResult: [], secondResult: []))
//    var compareUser: CompareUserModel?
//    var compareRepo: CompareRepoModel?
    
    // 유저 정보 가져오는 함수
    func getContributorInfo(firstRepoName: String, secondRepoName: String) -> Observable<CompareUserModel>{
        return Observable.create(){ observer in
            self.service.beforeSendingInfo(firstRepo: firstRepoName, secondRepo: secondRepoName)
                .subscribe(onNext: { contributorInfo in
                    observer.onNext(contributorInfo)
                })
                .disposed(by: self.disposeBag)
            return Disposables.create()
        }
    }
    
//    // 받아온 유저 정보를 CompareUserController 파일로 보냄
//    func getUserInfo() -> Observable<CompareUserModel> {
//        return Observable.create(){ observer in
//            self.sendData.subscribe(onNext: { data in
//                observer.onNext(data)
//            }).disposed(by: self.disposeBag)
//
//            return Disposables.create()
//        }
//    }
    
//    func start(){
//        self.service.getCompareInfo(firstRepo: self.firstRepo, secondRepo: self.secondRepo)
//            .subscribe(onNext: { repoInfo in
//                print("called")
//                print(repoInfo)
////                    observer.onNext(repoInfo)
//                self.compareRepo = repoInfo
//            })
//            .disposed(by: self.disposeBag)
//    }
    
    
    // MARK: 레포지토리 정보 받음
    func getRepositoryInfo(firstRepo: String, secondRepo: String)-> Observable<CompareRepoModel>{
        return Observable.create { observer in
            self.service.getCompareInfo(firstRepo: firstRepo, secondRepo: secondRepo)
                .subscribe(onNext: { repoInfo in
                    observer.onNext(repoInfo)
                })
                .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
    
    
//    func getRepoInfo() -> Observable<CompareRepoModel>{
//
//        return Observable.create(){ observer in
//            self.service.getCompareInfo(firstRepo: self.firstRepo, secondRepo: self.secondRepo)
//                .subscribe(onNext: { repoInfo in
//                    print("called")
//                    print(repoInfo)
//                    observer.onNext(repoInfo)
//                })
//                .disposed(by: self.disposeBag)
//
////            if self.compareRepo != nil{
////                print("not nil here")
////                observer.onNext(self.compareRepo!)
////            }
//
//            return Disposables.create()
//        }
//    }
    
    
}
