//
//  testViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/01/26.
//

import Foundation
import RxSwift

//테스트용 viewModel 클래스입니다.
class testViewModel{
    
    let testData = ["1","2","3","4","5","6"]
    
    var searchingData: BehaviorSubject<String> = BehaviorSubject(value: "")
    
    var tableViewData: BehaviorSubject<[String]> = BehaviorSubject(value: [])
    
    var checkValidId: Observable<String>{ searchingData.map({ $0 }) }
    
}
