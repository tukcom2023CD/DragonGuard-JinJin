//
//  DetailRepoModel.swift
//  ios
//
//  Created by 정호진 on 2023/06/20.
//

import Foundation

struct DetailRepoModel: Codable {
    let spark_line: [Int]?
    let git_repo_members: [GitRepoMembers]?
}

struct GitRepoMembers: Codable{
    let github_id: String
    let profile_url: String
    let commits: Int
    let additions: Int
    let deletions: Int
    let is_service_member: Bool
}
