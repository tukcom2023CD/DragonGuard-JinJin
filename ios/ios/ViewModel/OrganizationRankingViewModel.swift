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
    private let typeOrganizationRankingService = TypeOrganizationRankingService()
    let disposeBag = DisposeBag()
    /// 타입 필터링 랭킹
    private var typePage: Int = 0
    private var typeSize: Int = 20

    
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
    
    // MARK: 타입 별 랭킹 가져오는 함수
    func getTypeRanking(type: String) -> Observable<[AllOrganizationRankingModel]> {
        
        return Observable.create{ observer in
            self.typeOrganizationRankingService.getTypeOrganizationRanking(type: type,
                                                                           page: self.typePage,
                                                                           size: self.typeSize)
            .subscribe { rankingList in
                observer.onNext(rankingList)
            }
            .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
    
    
    
}
