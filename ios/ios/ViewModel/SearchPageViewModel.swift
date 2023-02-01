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
    
    var searchResult: BehaviorSubject<[SearchPageResultModel]> = BehaviorSubject (value: [])
    
    
}


