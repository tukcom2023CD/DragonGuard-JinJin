//
//  CompareUserModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/27.
//

import Foundation

/*
 Model
 */
struct CompareUserModel: Codable{
    let first_member: [FirstRepoResult]
    let second_member: [SecondRepoResult]
}

struct FirstRepoResult: Codable{
    let github_id: String?
    let profile_url: String?
    let commits: Int?
    let additions: Int?
    let deletions: Int?
    let is_service_member: Bool?
}

struct SecondRepoResult: Codable{
    let github_id: String?
    let profile_url: String?
    let commits: Int?
    let additions: Int?
    let deletions: Int?
    let is_service_member: Bool?
}

struct AllMemberInfoModel{
    let github_id: String?
    let profile_url: String?
    let commits: Int?
    let additions: Int?
    let deletions: Int?
    let is_service_member: Bool?
}
