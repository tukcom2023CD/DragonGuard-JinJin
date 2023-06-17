//
//  TierTableViewCell.swift
//  ios
//
//  Created by 홍길동 on 2023/03/29.
//

import Foundation
import UIKit
import SnapKit


final class TierTableViewCell : UITableViewCell {
    static let identifier = "TierTableViewCell"

    /*
     UI 작성
     */
    
    // MARK: 설정 리스트 버튼 정보
    private lazy var listLabel: UILabel = {
        let label = UILabel()
        contentView.addSubview(label)
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15)
        return label
    }()
    
    // MARK: 티어 종류 사진 및 설명
    private lazy var tierImage: UIImageView = {
        let tier = UIImageView()
        contentView.addSubview(tier)
        return tier
    }()
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    private func tierAutoLayout() {
        tierImage.snp.makeConstraints ({ make in
            make.centerY.equalToSuperview()
            make.leading.equalToSuperview().offset(10)
        })
        
        listLabel.snp.makeConstraints ({ make in
            make.centerY.equalToSuperview()
            make.leading.equalTo(tierImage.snp.trailing).offset(10)
        })
    }
    
    // MARK: 티어에 이미지, 라벨에 텍스트 입력
    func inputData(tier: UIImage, list: String) {
        tierAutoLayout()
        
        tierImage.image = tier.resize(newWidth: 100, newHeight: 100)
        listLabel.text = list
    }
    
    
}
