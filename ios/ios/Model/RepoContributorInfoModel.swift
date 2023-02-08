//
//  RepoContributorInfoModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/08.
//

import Foundation

struct RepoContriInfoDecodingModel: Codable{
    var githubId: String
    var commits: Int
    var additions: Int
    var deletions: Int
}


class RepoContributorInfoModel{
    var githubId: String
    var commits: Int
    var additions: Int
    var deletions: Int
        
    init(githubId: String, commits: Int, additions: Int, deletions: Int) {
        self.githubId = githubId
        self.commits = commits
        self.additions = additions
        self.deletions = deletions
    }
}
