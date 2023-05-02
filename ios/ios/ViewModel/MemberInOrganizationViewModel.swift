//
//  MemberInOrganizationViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/04/05.
//

import Foundation
import RxSwift

final class MemberInOrganizationViewModel{
    private let rankingInOrganizationService = RankingInOrganizationService()
    let disposeBag = DisposeBag()
    /// 사용자가 속한 조직 내부 랭킹
    private var memberInOrganizationPage: Int = 0
    private var memberInOrganizationSize: Int = 20
    
    
    // MARK: 사용자 Id 가져오는 함수
    func getOrganizationId(name: String) -> Observable<Int>{
        
        return Observable.create{ observer in
            self.rankingInOrganizationService.getOrganizationId(name: name)
                .subscribe(onNext: { id in
                    observer.onNext(id)
                })
                .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
    
    // MARK: 사용자가 속한 조직 멤버 가져오는 함수
    /// - Parameters:
    ///   - id: 사용자 조직 Id
    /// - Returns: 멤버 리스트
    func getMemberInOrganization(id: Int) -> Observable<[MemberInOrganizationModel]>{
        
        return Observable.create{ observer in
            self.rankingInOrganizationService.getMemberInOrganization(id: id,
                                                                      page: self.memberInOrganizationPage,
                                                                      size: self.memberInOrganizationSize)
            .subscribe(onNext: { data in
                observer.onNext(data)
                
            })
            .disposed(by: self.disposeBag)
            
            self.memberInOrganizationPage += 1
            
            return Disposables.create()
        }
    }
}
