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
    static let walletAddress = Notification.Name("")
    static let compareRepo = Notification.Name("")
}

enum NotificationRepos{
    case repo1
    case repo2
}

// KLIP 지갑 주소
enum NotificationWalletAddress{
    case walletAddress
}

// KLIP Deep Link
enum NotificationDeepLinkKey{
    case link
}

// 비교하기 -> 레포지토리 한개 씩 고르는 곳
enum NotificationKey{
    case choiceId
    case repository
}

