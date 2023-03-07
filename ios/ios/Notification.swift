//
//  Notification.swift
//  ios
//
//  Created by 정호진 on 2023/02/24.
//

import Foundation

extension Notification.Name{
    static let data = Notification.Name("")
}

// 비교하기 -> 레포지토리 한개 씩 고르는 곳
enum NotificationKey{
    case choiceId
    case repository
}
