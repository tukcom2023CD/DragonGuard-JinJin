//
//  OrganizationRankingViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/04/04.
//

import Foundation
import RxSwift

// MARK: 랭킹 관련된 ViewModel
final class OrganizationRankingViewModel{
    static let viewModel = OrganizationRankingViewModel()
    private let allOrganizationRankingService = AllOrganizationRankingService()
    let disposeBag = DisposeBag()
    
    // MARK: 전체 조직 랭킹 가져오는 함수
    func getAllOraganizationRanking() -> Observable<[AllOrganizationRankingModel]>{
        
        return Observable.create{ observer in
            self.allOrganizationRankingService.getAllOrganiRanking()
                .subscribe { allRanking in
                    observer.onNext(allRanking)
                }
                .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
    
    
}
