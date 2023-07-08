//
//  ConnectingBackend.swift
//  ios
//
//  Created by 홍길동 on 2023/01/27.
//

import RxCocoa
import RxSwift
import Foundation

final class SearchPageViewModel {
    static let viewModel = SearchPageViewModel()
    let searchPageService = SearchPageService()
    let disposeBag = DisposeBag()
    var pageCount = 1   //페이지 수
    private var searchWord: String?
    private var type: String?
    private var filtering: String?
    
    // 검색 결과를 가져오는 함수
    func getSearchData(searchWord: String, type: String, filtering: String) -> Observable<[SearchResultModel]>{
        self.pageCount = 1
        self.searchWord = searchWord
        self.type = type
        self.filtering = filtering
        
        return Observable.create(){ observer in
            self.searchPageService.getSearchResult(searchWord: searchWord,
                                                   page: self.pageCount,
                                                   type: type,
                                                   filtering: filtering)
                .subscribe(onNext: { searchResultList in
                    
                    observer.onNext(searchResultList)
                })
                .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
    
    func updateData() -> Observable<[SearchResultModel]>{
        
        return Observable.create(){ observer in
            self.pageCount += 1
            
            self.searchPageService.getSearchResult(searchWord: self.searchWord ?? "",
                                                   page: self.pageCount,
                                                   type: self.type ?? "",
                                                   filtering: self.filtering ?? "")
                .subscribe(onNext: { searchResultList in
                    
                    observer.onNext(searchResultList)
                })
                .disposed(by: self.disposeBag)
            
            
            return Disposables.create()
        }
    }
    
}


