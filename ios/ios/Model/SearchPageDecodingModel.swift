//
//  SearchPageDecodingModel.swift
//  ios
//
//  Created by 홍길동 on 2023/02/01.
//

import Foundation

struct SearchPageDecodingModel: Codable {
    var result: [SearchPageDecodingData]
}

struct SearchPageDecodingData: Codable {
    var name: String
}
