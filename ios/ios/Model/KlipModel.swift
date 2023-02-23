//
//  KlipModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/23.
//

import Foundation

struct KlipDecodingModel: Codable{
    let request_key: String
    let status: String
    let expiration_time: String
}

struct KlipResultModel: Codable{
    let request_key: String
    let status: String
    let expiration_time: String
    let result: KlipKlaytnAddr
}

struct KlipKlaytnAddr: Codable{
    let klaytn_address: String
}
