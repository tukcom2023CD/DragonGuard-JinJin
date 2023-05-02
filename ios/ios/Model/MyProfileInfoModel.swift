//
//  MyProfileInfoModel.swift
//  ios
//
//  Created by 정호진 on 2023/04/14.
//

import Foundation

struct MyProfileInfoModel{
    let id: String
    let name: String
    let githubId: String
    let commits: Int
    let issues: Int
    let pullRequests: Int
    let comments: Int
    let tier: String
    let authStep: String
    let profileImage: String
    let rank: Int
    let organizationRank: Int
    let tokenAmount: Int
    let organization: String
    let gitOrganizations: [String]
    let gitRepos: [String]
}

struct MyProfileInfoDecodingModel: Codable{
    let id: String?
    let name: String?
    let githubId: String?
    let commits: Int?
    let issues: Int?
    let pullRequests: Int?
    let comments: Int?
    let tier: String?
    let authStep: String?
    let profileImage: String?
    let rank: Int?
    let organizationRank: Int?
    let tokenAmount: Int?
    let organization: String?
    let gitOrganizations: [String]?
    let gitRepos: [String]?
}

