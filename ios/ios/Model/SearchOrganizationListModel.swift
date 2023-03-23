//
//  OrganizationListModel.swift
//  ios
//
//  Created by 정호진 on 2023/03/23.
//

import Foundation

// MARK: 조직 검색했을 때 받는 리스트 모델
struct SearchOrganizationListDecodingModel: Codable{
    var id: Int
    var name: String
    var type: String
    var emailEndpoint: String
}

// MARK: 조직 검색했을 때 받는 리스트 모델
struct SearchOrganizationListModel{
    var id: Int
    var name: String
    var type: String
    var emailEndpoint: String
}
