//
//  UserInfoModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/05.
//

import Foundation

struct UserInfoModel{
    var id: Int     //db id
    var name: String    // user name
    var githubId: String    // github id
    var tokens: Int    // user commits
    var tier: String    // user tier
    
    init(id: Int, name: String, githubId: String, tokens: Int, tier: String) {
        self.id = id
        self.name = name
        self.githubId = githubId
        self.tokens = tokens
        self.tier = tier
    }
}



struct UserInfoDecodingData: Codable{
    var id: Int
    var name: String?
    var githubId: String
    var tokens: Int?
    var tier: String
    
}


