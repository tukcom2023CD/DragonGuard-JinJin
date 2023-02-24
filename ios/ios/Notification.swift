//
//  Notification.swift
//  ios
//
//  Created by 정호진 on 2023/02/24.
//

import Foundation



extension Notification.Name{
    static let data = Notification.Name("")
    static let deepLink = Notification.Name("")
}

enum NotificationDeepLinkKey{
    case link
}

enum NotificationKey{
    case choiceId
    case repository
}
