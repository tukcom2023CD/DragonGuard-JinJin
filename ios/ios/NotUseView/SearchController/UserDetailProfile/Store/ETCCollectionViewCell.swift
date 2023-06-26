////
////  ETCCollectionViewCell.swift
////  ios
////
////  Created by 정호진 on 2023/04/13.
////
//
//import Foundation
//import UIKit
//import SnapKit
//
//final class ETCCollectionViewCell: UICollectionViewCell{
//    static let identifier = "ETCCollectionViewCell"
//
//    // MARK: Commit, Issue, PR, Comment
//    private lazy var titleLabel: UILabel = {
//       let label = UILabel()
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
//
//        self.addSubview(label)
//        label.snp.makeConstraints { make in
//            make.centerY.equalToSuperview()
//            make.leading.equalTo(self.safeAreaLayoutGuide).offset(10)
//        }
//        return label
//    }()
//
//    // MARK: 개수
//    private lazy var infoLabel: UILabel = {
//       let label = UILabel()
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15)
//
//        self.addSubview(label)
//        label.snp.makeConstraints { make in
//            make.centerY.equalToSuperview()
//            make.leading.equalTo(titleLabel.snp.trailing).offset(10)
//        }
//        return label
//    }()
//
//    // MARK: 정보 입력
//    func inputInfo(title: String, count: Int){
//        self.titleLabel.text = title
//        self.infoLabel.text = "\(count)"
//    }
//
//
//}
