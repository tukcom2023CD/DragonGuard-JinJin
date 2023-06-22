//
//  CompareRepoModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/24.
//

import Foundation

struct CompareRepoModel{
    var firstRepo: FirstRepoModel
    var secondRepo: secondRepoModel
}

struct FirstRepoModel{
    var gitRepo: GitRepoModel
    var statistics: StatisticsModel
    var languages: LanguagesModel
    var languagesStats: StatisticsStatsModel
    var profileUrls: [String]
}

struct secondRepoModel{
    var gitRepo: GitRepoModel
    var statistics: StatisticsModel
    var languages: LanguagesModel
    var languagesStats: StatisticsStatsModel
    var profileUrls: [String]
}

struct GitRepoModel{
    var full_name: String
    var forks_count, stargazers_count, watchers_count, open_issues_count, closed_issues_count: Int
    var subscribers_count: Int
}

struct StatisticsModel{
    let commitStats: StatisticsStatsModel
    let additionStats: StatisticsStatsModel
    let deletionStats: StatisticsStatsModel
}

struct LanguagesModel{
    var language: [String]
    var count: [Int]
}

struct StatisticsStatsModel{
    var count: Int
    var sum: Int
    var min: Int
    var max: Int
    var average: Double
}



/*
 Decoding Model
 */

struct CompareRepoDecodingModel: Decodable{
    var firstRepo: FirstRepo
    var secondRepo: SecondRepo
}

struct FirstRepo: Decodable{
    var gitRepo: GitRepo
    var statistics: Statistics
    var languages: Languages
    var languagesStats: StatisticsStats
    var profileUrls: [String]
}

struct SecondRepo: Decodable{
    let gitRepo: GitRepo
    let statistics: Statistics
    var languages: Languages
    let languagesStats: StatisticsStats
    var profileUrls: [String]
}

struct GitRepo: Codable{
    let full_name: String
    let forks_count, stargazers_count, watchers_count, open_issues_count, closed_issues_count: Int?
    let subscribers_count: Int?
    
}

struct Statistics: Codable{
    let commitStats: StatisticsStats
    let additionStats: StatisticsStats
    let deletionStats: StatisticsStats
}

struct StatisticsStats: Codable{
    let count: Int?
    let sum: Int?
    let min: Int?
    let max: Int?
    let average: Double?
}

struct Languages: Decodable{
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
