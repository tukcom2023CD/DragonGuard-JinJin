//
//  BlockChainListModel.swift
//  ios
//
//  Created by 정호진 on 2023/06/18.
//

import Foundation

struct BlockChainListModel: Codable{
    let id: Int?
    let contribute_type: String?
    let amount: Int?
    let github_id: String?
    let member_id: String?
    let created_at: String?
    let transaction_hash_url: String?
}
