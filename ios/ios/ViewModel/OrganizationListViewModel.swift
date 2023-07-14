//
//  OrganizationListViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/07/15.
//

import Foundation
import RxSwift

final class OrganizationListViewModel{
    private let service = OrganizationListService()
    private let rankingOragnizationService = RankingInOrganizationService()
    private let disposeBag = DisposeBag()
    private var page = 0
    private let size = 20
    
    
    
    /// 조직 내부 멤버 가져오는 함수
    /// - Parameters:
    ///   - name: 조직 이름
    ///   - check: 첫 호출 = true, 추가 멤버 확인 = false
    func getMemberList(name: String, check: Bool) -> Observable<[AllUserRankingModel]>{
        if check{
            page = 0
        }
        return Observable.create { observer in
            self.rankingOragnizationService.getOrganizationId(name: name)
                .subscribe(onNext: { id in
                    
                    self.service.getMemberList(page: self.page, size: self.size, organizationId: id)
                        .subscribe(onNext:{ list in
                            observer.onNext(list)
                            
                            self.page += 1
                        })
                        .disposed(by: self.disposeBag)
                    
                })
                .disposed(by: self.disposeBag)
            return Disposables.create()
        }
    }
}
