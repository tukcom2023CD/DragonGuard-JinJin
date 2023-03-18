//
//  MainModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/12.
//

import Foundation

final class MainModel{
    var id: String
    var name: String
    var githubId: String
    var commits: Int
    var tier: String
    var authStep: String
    var profileImage: String
    var rank: Int
    var tokenAmount: Int
    
    init(id: String, name: String, githubId: String, commits: Int, tier: String, authStep: String, profileImage: String,rank: Int, tokenAmount: Int ) {
        self.id = id
        self.name = name
        self.githubId = githubId
        self.commits = commits
        self.tier = tier
        self.authStep = authStep
        self.profileImage = profileImage
        self.rank = rank
        self.tokenAmount = tokenAmount
    }
}

struct MainDecodingModel: Codable{
    var id: String
    var name: String?
    var githubId: String
    var commits: Int?
    var tier: String
    var authStep: String
    var profileImage: String?
    var rank: Int
    var tokenAmount: Int?
}
