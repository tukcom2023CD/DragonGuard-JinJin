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
    let profileImg: String?
    let userName: String?
    let num: Int?
    let link: String?
    let tier: String?
}
