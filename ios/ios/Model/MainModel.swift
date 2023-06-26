//
//  MainModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/12.
//

import Foundation

struct MainModel{
    var id: String
    var name: String
    var githubId: String
    var commits: Int
    var issues: Int
    var pullRequests: Int
    var reviews: Int
    var tier: String
    var authStep: String
    var profileImage: String?
    var rank: Int?
    var organizationRank: Int?
    var tokenAmount: Int?
    var organization: String?
    var blockchainUrl: String?
    var isLast: Bool?
    var memberGithubIds: [String]?

}

struct MainDecodingModel: Codable{
    var id: String
    var name: String
    var githubId: String
    var commits: Int
    var issues: Int
    var pullRequests: Int
    var reviews: Int
    var tier: String
    var authStep: String
    var profileImage: String?
    var rank: Int
    var organizationRank: Int
    var tokenAmount: Int
    var organization: String?
    var blockchainUrl: String?
    var isLast: Bool?
    var memberGithubIds: [String]?
}
