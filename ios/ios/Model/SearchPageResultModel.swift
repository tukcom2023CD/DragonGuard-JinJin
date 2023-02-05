//
//  SearchPageResultModel.swift
//  ios
//
//  Created by 홍길동 on 2023/02/02.
//

import Foundation

class SearchPageResultModel {
    var id: String
    var name: String
    
    init(name: String,id: String) {
        self.id = id
        self.name = name
    }
}
