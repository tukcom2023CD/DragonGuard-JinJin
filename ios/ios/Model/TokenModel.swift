//
//  TokenModel.swift
//  ios
//
//  Created by 정호진 on 2023/03/22.
//

import Foundation

struct TokenModel: Codable{
    let accessToken: String
    let grantType: String
    let refreshToken: String
    
    enum CodingKeys: String, CodingKey{
        case accessToken = "access_token"
        case grantType = "grant_type"
        case refreshToken = "refresh_token"
    }
}
