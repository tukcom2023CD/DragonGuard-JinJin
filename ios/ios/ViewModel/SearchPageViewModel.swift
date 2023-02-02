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
    let searchPageService = SearchPageService()
    let disposeBag = DisposeBag()
    var searchWord = ""
    var searchResult: BehaviorSubject<[SearchPageResultModel]> = BehaviorSubject(value: [])
    var searchInput: BehaviorSubject<String> = BehaviorSubject(value: "")
    var resultCount = 0
    
    func switchData(){
        print("aaa \(self.searchPageService.resultArray.count)")
        self.searchResult.onNext(searchPageService.resultArray)
        resultCount = self.searchPageService.resultArray.count
    }
    
    func getAPIData(){
        
        searchInput.subscribe(onNext: {
            self.searchWord = $0
        }).disposed(by: disposeBag)
        
        self.searchPageService.getPage(searchWord: searchWord)
    }
    
    
}


