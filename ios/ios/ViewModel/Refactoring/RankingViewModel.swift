//
//  UserInfoViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/05.
//

import Foundation
import RxSwift
import RxCocoa

final class RankingViewModel{
    let service = RankingService()
    let disposeBag = DisposeBag()
    private var pageCount = 0
    private var size = 30
    
    // MARK: 전체 사용자 랭킹
    /// - Parameter check: 첫 호출인지 판별
    func getAllRanking(check: Bool) -> Observable<[AllUserRankingModel]>{
        if check {
            self.pageCount = 0
        }
        return Observable.create(){ observer in
            self.service.getMemberInfo(page: self.pageCount, size: self.size)
                .subscribe(onNext: { rankingList in
                    observer.onNext(rankingList)
                })
                .disposed(by: self.disposeBag)
            self.pageCount += 1
            return Disposables.create()
        }
    }
    
    // MARK: 타입별 랭킹
    ///   - type: 타입
    ///   - check: 첫 호출인지 판별
    func rankingOfType(type: String, check: Bool) -> Observable<[TypeRankingModel]>{
        if check {
            self.pageCount = 0
        }
        return Observable.create(){ observer in
            self.service.rankingOfType(page: self.pageCount, size: self.size, type: type)
                .subscribe(onNext: { list in
                    observer.onNext(list)
                })
                .disposed(by: self.disposeBag)
            self.pageCount += 1
            return Disposables.create()
        }
    }
    
    func allRankingOfType(check: Bool) -> Observable<[TypeRankingModel]>{
        if check {
            self.pageCount = 0
        }
        return Observable.create(){ observer in
            self.service.allRankingOfType(page: self.pageCount, size: self.size)
                .subscribe(onNext: { list in
                    observer.onNext(list)
                })
                .disposed(by: self.disposeBag)
            self.pageCount += 1
            return Disposables.create()
        }
    }
    
    
}
