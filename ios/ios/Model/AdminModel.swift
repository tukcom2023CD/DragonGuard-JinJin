//
//  AdminModel.swift
//  ios
//
//  Created by 정호진 on 2023/05/05.
//

import Foundation

struct AdminModel{
    let id: Int?
    let name: String?
    let type: String?
}

struct AdminDecodingModel: Codable{
    let id: Int?
    let name: String?
    let type: String?
}
