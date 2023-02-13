//
//  MainModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/12.
//

import Foundation

class MainModel{
    var id: Int
    var name: String
    var githubId: String
    var commits: Int
    var tier: String
    var authStep: String
    var profileImage: String
    var rank: Int
    
    init(id: Int, name: String, githubId: String, commits: Int, tier: String, authStep: String, profileImage: String,rank: Int) {
        self.id = id
        self.name = name
        self.githubId = githubId
        self.commits = commits
        self.tier = tier
        self.authStep = authStep
        self.profileImage = profileImage
        self.rank = rank
    }
}

struct MainDecodingModel: Codable{
    var id: Int
    var name: String
    var githubId: String
    var commits: Int
    var tier: String
    var authStep: String
    var profileImage: String
    var rank: Int
}
