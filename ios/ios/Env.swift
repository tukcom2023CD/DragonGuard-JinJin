//
//  Env.swift
//  ios
//
//  Created by 정호진 on 2023/03/13.
//

import Foundation

// 커밋할 때 꼭 다 지우세요!!
final class Environment{
//    static let env = Environment()
    
    static let ip = ""
    static var jwtToken = UserDefaults.standard.string(forKey: "Access")
    static var refreshToken = UserDefaults.standard.string(forKey: "Refresh")
}
