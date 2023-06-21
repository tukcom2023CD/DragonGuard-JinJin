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
    let service = ALLUserInfoService()
    let disposeBag = DisposeBag()
    var pageCount = 0
    
    func getAllRanking() -> Observable<[AllUserRankingModel]>{
        return Observable.create(){ observer in
            self.service.getMemberInfo(page: self.pageCount, size: 30)
                .subscribe(onNext: { rankingList in
                    observer.onNext(rankingList)
                })
                .disposed(by: self.disposeBag)
            self.pageCount += 1
            return Disposables.create()
        }
    }
}
