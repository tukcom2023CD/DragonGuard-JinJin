//
//  AllOrganizationRankingModel.swift
//  ios
//
//  Created by 정호진 on 2023/04/04.
//

import Foundation

struct AllOrganizationRankingDecodingModel: Codable{
    let id: Int
    let name: String
    let organizationType: String
    let emailEndpoint: String
    let tokenSum: Int
}

struct AllOrganizationRankingModel{
    let id: Int
    let name: String
    let organizationType: String
    let emailEndpoint: String
    let tokenSum: Int
}
