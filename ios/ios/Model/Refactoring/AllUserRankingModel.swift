//
//  AllUserRankingModel.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation

struct AllUserRankingModel{
    var profileImg: String?
    var userName: String?
    var num: Int?
    var link: String?
    var tier: String?
}


struct AllUserRankingCodableModel: Codable{
    let id: String?
    let profileImg: String?
    let name: String?
    let githubId: String?
    let tokens: Int?
    let tier: String?
}
