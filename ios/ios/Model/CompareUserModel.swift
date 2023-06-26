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
    var first_member: [FirstRepoResult]
    var second_member: [SecondRepoResult]
}

struct FirstRepoResult: Codable{
    var github_id: String?
    var profile_url: String?
    var commits: Int?
    var additions: Int?
    var deletions: Int?
    var is_service_member: Bool?
}

struct SecondRepoResult: Codable{
    var github_id: String?
    var profile_url: String?
    var commits: Int?
    var additions: Int?
    var deletions: Int?
    var is_service_member: Bool?
}


/*
 Decoding Model
 */
struct CompareUserDecodingModel: Codable{
    var firstResult: [FirstResult]
    var secondResult: [SecondResult]
}

struct FirstResult: Codable{
    var githubId: String?
    var profileUrl: String?
    var commits: Int?
    var additions: Int?
    var deletions: Int?
    var isServiceMember: Bool?
}

struct SecondResult: Codable{
    var githubId: String?
    var profileUrl: String?
    var commits: Int?
    var additions: Int?
    var deletions: Int?
    var isServiceMember: Bool?
}
