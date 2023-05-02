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
}
