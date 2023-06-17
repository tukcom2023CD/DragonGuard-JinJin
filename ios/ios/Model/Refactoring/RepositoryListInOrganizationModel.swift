//
//  RepositoryListInOrganization.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation

struct RepositoryListInOrganizationModel{
    let gitRepos: [String]?
    let imgPath: String?
}


struct RepositoryListInOrganizationCodableModel: Codable{
    let gitRepos: [String]?
    let imgPath: String?
}
