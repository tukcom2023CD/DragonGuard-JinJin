//
//  File.swift
//  ios
//
//  Created by 정호진 on 2023/07/02.
//

import Foundation

struct YourProfileModel: Codable{
    let commits: Int?
    let issues : Int?
    let pull_requests: Int?
    let reviews: Int?
    let profile_image: String?
    let git_repos: [String]?
    let organization: String?
    let rank: Int?
}
