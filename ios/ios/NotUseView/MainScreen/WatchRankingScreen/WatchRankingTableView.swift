////
////  WatchRankingTableView.swift
////  ios
////
////  Created by 정호진 on 2023/02/01.
////
//
//import Foundation
//import UIKit
//import SnapKit
//
//// 랭킹보러가기 하위 tableview cell들 사용하기 위한 클래스
//
//final class WatchRankingTableView: UITableViewCell {
//    static let identifier = "WatchRankingTableView"
//    
//    // 클래스 생성자
//    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
//        super.init(style: style, reuseIdentifier: reuseIdentifier)
//    }
//    
//    required init?(coder: NSCoder) {
//        fatalError("init(coder:) has not been implemented")
//    }
//    
//    // Ranking Number UI
//    lazy var rankLabel: UILabel = {
//        let label = UILabel()
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 23)
//        label.textColor = .black
//        contentView.addSubview(label)
//        
//        label.snp.makeConstraints({ make in
//            make.leading.equalTo(contentView.snp.leading).offset(20)
//            make.centerY.equalTo(contentView.center)
//        })
//        
//        return label
//    }()
//    
//    // User Name UI
//    lazy var userLabel: UILabel = {
//        let rankingLabel = UILabel()
//        rankingLabel.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 23)
//        rankingLabel.textColor = .black
//        rankingLabel.textAlignment = .center
//        contentView.addSubview(rankingLabel)
//        
//        rankingLabel.snp.makeConstraints({ make in
//            make.center.equalTo(contentView.center)
//            make.centerX.equalTo(self.center)
//        })
//
//        return rankingLabel
//    }()
//    
//    // Commit, Token Count UI
//    lazy var dataLabel: UILabel = {
//        let label = UILabel()
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 17)
//        label.textColor = .black
//        contentView.addSubview(label)
//        
//        label.snp.makeConstraints({ make in
//            make.trailing.equalTo(contentView.snp.trailing).offset(-20)
//            make.centerY.equalTo(contentView.center)
//        })
//        
//        return label
//    }()
//    
//    // 데이터 삽입
//    func prepare(rank: Int, text: String, count: Int){
//        rankLabel.text = String(rank)
//        userLabel.text = text
//        dataLabel.text = String(count)
//    }
//    
//}
//
//
