//
//  SearchPageResultModel.swift
//  ios
//
//  Created by 홍길동 on 2023/02/02.
//

import Foundation


struct SearchPageDecodingData: Codable {
    var id: String
    var name: String
}


final class SearchPageResultModel {
    var id: String
    var name: String
    
    init(name: String,id: String) {
        self.id = id
        self.name = name
    }
}
