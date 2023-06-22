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
struct CompareUserModel{
    var firstResult: [FirstRepoResult]
    var secondResult: [SecondRepoResult]
}

struct FirstRepoResult{
    var githubId: String
    var profileUrl: String
    var commits: Int
    var additions: Int
    var deletions: Int
    var isServiceMember: Bool
}

struct SecondRepoResult{
    var githubId: String
    var profileUrl: String
    var commits: Int
    var additions: Int
    var deletions: Int
    var isServiceMember: Bool
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
