//
//  DetailRepoModel.swift
//  ios
//
//  Created by 정호진 on 2023/06/20.
//

import Foundation

struct DetailRepoModel {
    let sparkLine: [Int]?
    let gitRepoMembers: [GitRepoMembers]?
}

struct GitRepoMembers{
    let githubId: String
    let profileUrl: String
    let commits: Int
    let additions: Int
    let deletions: Int
    let isServiceMember: Bool
}


struct DetailRepoCodableModel: Codable {
    let sparkLine: [Int]?
    let gitRepoMembers: [GitRepoMembersCodable]?
}

struct GitRepoMembersCodable: Codable{
    let githubId: String
    let profileUrl: String
    let commits: Int
    let additions: Int
    let deletions: Int
    let isServiceMember: Bool
}
