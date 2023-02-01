//
//  WatchRankingModel.swift
//  ios
//
//  Created by 정호진 on 2023/02/01.
//

import Foundation
import RxDataSources

// DataSource
struct rankingModel {
    var header: String
    var items: [Item]
}

extension rankingModel : AnimatableSectionModelType {
    typealias Item = String
    
    var identity: String {
        return header
    }
    
    init(original: rankingModel, items: [Item]) {
        self = original
        self.items = items
    }
}
