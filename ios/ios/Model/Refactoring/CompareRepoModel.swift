//
//  CompareRepoModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/24.
//

import Foundation

struct CompareRepoModel: Codable{
    var first_repo: FirstRepo
    var second_repo: SecondRepo
}

struct FirstRepo: Codable{
    var git_repo: GitRepo
    var statistics: Statistics
    var languages: Languages
    var languages_stats: StatisticsStats
    var profile_urls: [String]
}

struct SecondRepo: Codable{
    var git_repo: GitRepo
    var statistics: Statistics
    var languages: Languages
    var languages_stats: StatisticsStats
    var profile_urls: [String]
}

struct GitRepo: Codable{
    let full_name: String
    let forks_count, stargazers_count, watchers_count, open_issues_count, closed_issues_count: Int?
    let subscribers_count: Int?
    
}

struct Statistics: Codable{
    let commit_stats: StatisticsStats
    let addition_stats: StatisticsStats
    let deletion_stats: StatisticsStats
}

struct StatisticsStats: Codable{
    let count: Int?
    let sum: Int?
    let min: Int?
    let max: Int?
    let average: Double?
}

struct Languages: Codable{
    var language: [String] = []
    var count: [Int] = []
    
    struct CustomCodingKeys: CodingKey {
        var stringValue: String
        init?(stringValue: String) {
            self.stringValue = stringValue
        }
        var intValue: Int? { return 0 }
        init?(intValue: Int) { return nil }
    }
    
    public init(from decoder: Decoder) throws {
        let extraContainer = try decoder.container(keyedBy: CustomCodingKeys.self)
        
        for key in extraContainer.allKeys {
            
            let value =  try extraContainer.decode(Int.self, forKey: CustomCodingKeys(stringValue: key.stringValue)!)
            self.language.append(key.stringValue)
            self.count.append(value)
//            self.lang[key.stringValue] = value
            
        }
    }
}
