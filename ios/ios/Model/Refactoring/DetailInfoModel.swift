//
//  RepoInfoModel.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation

struct DetailInfoModel{
    let gitOrganizations: [Organ_InfoModel]?
    let gitRepos: [String]?
    let memberProfileImage: String?
}

struct Organ_InfoModel{
    let imgPath: String?
    let title: String?
}


struct DetailInfoCodableModel: Codable{
    let gitOrganizations: [Organ_InfoCodableModel]?
    let gitRepos: [String]?
    let memberProfileImage: String?
}

struct Organ_InfoCodableModel: Codable{
    let imgPath: String?
    let title: String?
}
