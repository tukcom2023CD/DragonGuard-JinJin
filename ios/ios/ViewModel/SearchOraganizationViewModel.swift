//
//  SearchOraganizationViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/03/22.
//

import Foundation
import RxSwift

final class SearchOraganizationViewModel{
    static let viewModel = SearchOraganizationViewModel()
    let service = SearchOraganizationService()
    let disposeBag = DisposeBag()
    var page = 0
    var size = 20
    
    // MARK: 사용자가 검색한 조직 리스트를 서버에서 받아 반환하는 함수
    /// - Parameters:
    ///   - name: 검색한 조직 이름
    ///   - type: UNIVERSITY, COMPANY, HIGH_SCHOOL, ETC
    ///   - check: 새로운 조직을 검색했을 때 false로 들어옴
    /// - Returns: 검색한 조직 리스트들 반환
    func getOrganizationList(name: String, type: String, check: Bool) -> Observable<[SearchOrganizationListModel]>{
        // check가 거짓인 경우 page 초기화
        if !check{
            self.page = 0
        }
        
        return Observable.create(){ observer in
            self.service.getOrganizationListService(name: name, type: type, page: self.page, size: self.size)
                .subscribe(onNext: { data in
                    print("SearchOraganizationViewModel data \n \(data)")
                    observer.onNext(data)
                    self.page += 1
                })
                .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
    
    
}
