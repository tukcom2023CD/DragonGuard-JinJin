//
//  MainModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/12.
//

import Foundation

struct MainModel: Codable{
    
    var id: String
    var name: String
    var github_id: String
    var commits: Int
    var issues: Int
    var pull_requests: Int
    var reviews: Int
    var tier: String
    var auth_step: String
    var profile_image: String?
    var rank: Int?
    var organization_rank: Int?
    var token_amount: Int?
    var organization: String?
    var blockchain_url: String?
    var is_last: Bool?
    var member_github_ids: [String]?

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
