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
    var firstResult: FirstRepoResult
    var secondResult: SecondRepoResult
}

struct FirstRepoResult{
    var githubId: String
    var commits: Int
    var additions: Int
    var deletions: Int
}

struct SecondRepoResult{
    var githubId: String
    var commits: Int
    var additions: Int
    var deletions: Int
}


/*
 Decoding Model
 */
struct CompareUserDecodingModel: Codable{
    var firstResult: [FirstResult]
    var secondResult: [SecondResult]
}

struct FirstResult: Codable{
    var githubId: String
    var commits: Int
    var additions: Int
    var deletions: Int
}

struct SecondResult: Codable{
    var githubId: String
    var commits: Int
    var additions: Int
    var deletions: Int
}
