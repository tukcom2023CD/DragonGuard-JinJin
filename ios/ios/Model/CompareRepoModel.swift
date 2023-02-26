//
//  CompareRepoModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/24.
//

import Foundation

struct CompareRepoModel: Decodable{
    var firstRepo: FirstRepo
    var secondRepo: SecondRepo
}

struct FirstRepo: Decodable{
    var gitRepo: GitRepo
    var statistics: Statistics
//    var languages: Languages
    var languagesStat: StatisticsStats
    
}

struct SecondRepo: Decodable{
    let gitRepo: GitRepo
    let statistics: Statistics
//    var languages: Languages
    let languagesStat: StatisticsStats
    
}

struct GitRepo: Codable{
    let full_Name: String
    let forks_Count, stargazers_Count, watchers_Count, open_Issues_Count: Int
    let subscribers_Count: Int
    
}

struct Statistics: Codable{
    let commitStats: StatisticsStats
    let additionStats: StatisticsStats
    let deletionStats: StatisticsStats
}

struct StatisticsStats: Codable{
    let count: Int
    let sum: Int
    let min: Int
    let max: Int
    let average: Double
}

struct Languages: Decodable{
    var lang: [String : Any] = [:]
    
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
            print("key1: \(key)")
            
            let value =  try extraContainer.decode(Int.self, forKey: CustomCodingKeys(stringValue: key.stringValue)!)
            self.lang[key.stringValue] = value
            
        }
    }
}
