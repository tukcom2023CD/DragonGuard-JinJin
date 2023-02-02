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
    var searchWord = "" //검색단어
    var searchResult: BehaviorSubject<[SearchPageResultModel]> = BehaviorSubject(value: []) //API 결과값
    var searchInput: BehaviorSubject<String> = BehaviorSubject(value: "")   // view에서 검색단어 바인딩으로 받아옴
    var pageCount = 1   //페이지 수
    
    // API 결과값 view로 전달
    func switchData(){
        self.searchResult.onNext(searchPageService.resultArray) // 결과값 바인딩
    }
    
    func getAPIData(){
        pageCount += 1  //페이지 개수 증가
        searchResult = BehaviorSubject(value: [])   // searchResult 초기화
        
        // 검색하는 단어 받아옴
        searchInput.subscribe(onNext: {
            self.searchWord = $0
        }).disposed(by: disposeBag)
        
        // 검색하는 단어, 페이지 이름 전달
        self.searchPageService.getPage(searchWord: searchWord,page: pageCount)
    }
    
    
}


