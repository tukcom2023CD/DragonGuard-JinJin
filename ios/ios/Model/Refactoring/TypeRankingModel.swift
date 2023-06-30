//
//  AllOrganizationRankingModel.swift
//  ios
//
//  Created by 정호진 on 2023/04/04.
//

import Foundation

struct TypeRankingModel: Codable{
    let id: Int?
    let name: String?
    let organization_type: String?
    let email_endpoint: String?
    let token_sum: Int?
}
