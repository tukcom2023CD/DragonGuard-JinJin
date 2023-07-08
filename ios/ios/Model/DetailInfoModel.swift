//
//  RepoInfoModel.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation

struct DetailInfoModel: Codable{
    let git_organizations: [Organ_InfoModel]?
    let git_repos: [String]?
    let member_profile_image: String?
}

struct Organ_InfoModel: Codable{
    let profile_image: String?
    let name: String?
}
