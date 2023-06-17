//
//  BlockChainListModel.swift
//  ios
//
//  Created by 정호진 on 2023/06/18.
//

import Foundation

struct BlockChainListModel{
    let contributeType: String?
    let amount: Int?
    let githubId: String?
    let createdAt: String?
    let transactionHashUrl: String?
}

struct BlockChainListCodableModel: Codable{
    let id: Int?
    let contributeType: String?
    let amount: Int?
    let githubId: String?
    let memberId: String?
    let createdAt: String?
    let transactionHashUrl: String?
}
