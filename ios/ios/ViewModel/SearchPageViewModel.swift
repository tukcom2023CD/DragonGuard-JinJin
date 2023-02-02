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
    var searchPageService = SearchPageService()
    let disposeBag = DisposeBag()
    var searchWord = ""
    var searchResult: ReplaySubject<SearchPageResultModel> = ReplaySubject.create(bufferSize: 10)
    var searchInput: BehaviorSubject<String> = BehaviorSubject(value: "")
    
    func switchData(){
        print("aaa \(self.searchPageService.resultArray.count)")
        
        for data in self.searchPageService.resultArray {
            self.searchResult.onNext(data)
            print("data \(data.name)")
        }
    }
    
    func getAPIData(){
        searchInput.subscribe(onNext: {
            self.searchWord = $0
        }).disposed(by: disposeBag)
        
        self.searchPageService.getPage(searchWord: searchWord)
    }
    
    
}


