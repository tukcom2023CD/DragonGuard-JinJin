//
//  WatchRankingTableView.swift
//  ios
//
//  Created by 정호진 on 2023/02/01.
//

import Foundation
import UIKit
import SnapKit

// 랭킹보러가기 하위 tableview cell들 사용하기 위한 클래스

final class WatchRankingTableView: UITableViewCell {
    static let identifier = "WatchRankingTableView"
    
    // 클래스 생성자
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // Label UI
    lazy var rankingLabel: UILabel = {
        let rankingLabel = UILabel()
        rankingLabel.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 23)
        contentView.addSubview(rankingLabel)
        
        rankingLabel.snp.makeConstraints({ make in
            make.top.bottom.equalTo(contentView)
            make.centerX.equalTo(contentView)
        })

        return rankingLabel
    }()
    
    
    // 데이터 삽입
    func prepare(text: String){
        rankingLabel.text = text
    }
    
}


