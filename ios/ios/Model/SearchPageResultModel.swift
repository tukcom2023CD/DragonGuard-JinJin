//
//  SearchPageResultModel.swift
//  ios
//
//  Created by 홍길동 on 2023/02/02.
//

import Foundation


struct SearchPageDecodingData: Codable {
    var id: Int
    var name: String
}


final class SearchPageResultModel {
    var id: Int
    var name: String
    
    init(name: String,id: Int) {
        self.id = id
        self.name = name
    } 
}
