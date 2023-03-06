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
    let service = RepoContributorInfoService()
    let disposeBag = DisposeBag()
    
    // Repository 상세 정보 가져오는 함수
    func getContributorInfo(selectName: String) -> Observable<[RepoContributorInfoModel]> {
        return Observable.create(){ observer in
            self.service.getRepoContriInfo(selectedName: selectName)
                .subscribe(onNext: { contributorInfo in
                    observer.onNext(contributorInfo)
                })
                .disposed(by: self.disposeBag)
            return Disposables.create()
        }
    }
}
