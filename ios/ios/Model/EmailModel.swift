//
//  EmailModel.swift
//  ios
//
//  Created by 정호진 on 2023/03/29.
//

import Foundation


struct CheckEmailModel: Codable{
    let is_valid_code: Bool
}

// MARK: 멤버에 추가할 때 인증번호 전송
struct EmailIdDecodingModel: Codable{
    let id: Int
}

