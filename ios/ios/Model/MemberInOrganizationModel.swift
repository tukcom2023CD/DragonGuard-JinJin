//
//  MemberInOrganizationModel.swift
//  ios
//
//  Created by 정호진 on 2023/04/05.
//

import Foundation

struct MemberInOrganizationDecodingModel: Codable{
    let id: String
    let name: String
    let githubId: String
    let tokens: Int
    let tier: String
}

struct MemberInOrganizationModel{
    let id: String
    let name: String
    let githubId: String
    let tokens: Int
    let tier: String
}
