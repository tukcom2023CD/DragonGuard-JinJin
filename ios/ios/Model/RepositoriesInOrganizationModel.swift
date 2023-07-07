//
//  RepositoriesInOrganizationModel.swift
//  ios
//
//  Created by 정호진 on 2023/07/07.
//

import Foundation

struct RepositoriesInOrganizationModel: Codable{
    let profile_image: String?
    let git_repos: [String]?
}
