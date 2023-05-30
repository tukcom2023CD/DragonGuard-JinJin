//
//  SearchResultModel.swift
//  ios
//
//  Created by 정호진 on 2023/05/29.
//

import Foundation

struct SearchResultModel{
    let create: String
    let language: String
    let title: String
}

struct SearchResultDecodingModel: Codable{
    let create: String
    let language: String
    let title: String
}
