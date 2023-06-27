//
//  AllUserRankingModel.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation

struct AllUserRankingModel: Codable{
    let id: String?
    let profile_image: String?
    let name: String?
    let github_id: String?
    let tokens: Int?
    let tier: String?
}


struct AllUserRankingCodableModel: Codable{
    let id: String?
    let profileImg: String?
    let name: String?
    let githubId: String?
    let tokens: Int?
    let tier: String?
}
