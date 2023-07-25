//
//  SearchResultModel.swift
//  ios
//
//  Created by 정호진 on 2023/05/29.
//

import Foundation

struct SearchResultModel{
    let id: Int
    let name: String
    let language: String
    let description: String
    let created_at: String
    let is_service_member: Bool?
}

struct SearchResultDecodingModel: Codable{
    let id: Int
    let name: String?
    let language: String?
    let description: String?
    let created_at: String?
    let is_service_member: Bool?
}
