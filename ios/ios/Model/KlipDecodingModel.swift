//
//  KlipModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/23.
//

import Foundation

final class KlipModel{
    var request_key: String
    var status: String
    var expiration_time: String
    
    init(request_key: String, status: String, expiration_time: String) {
        self.request_key = request_key
        self.status = status
        self.expiration_time = expiration_time
    }
}

struct KlipDecodingModel: Codable{
    let request_key: String
    let status: String
    let expiration_time: String
}
