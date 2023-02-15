//
//  MainRankingCollectionView.swift
//  ios
//
//  Created by 홍길동 on 2023/02/15.
//

import Foundation
import UIKit

final class MainRankingCollectionView: UICollectionViewCell {
    static let identifier = "mainRankingCollectionView"
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        self.cellSetting()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    func cellSetting() {
        self.backgroundColor = .white
        self.addSubview(rankingNameLabel)
        self.addSubview(rankingNumLabel)
        self.addSubview(percentLabel)
        
                setAutoLayout()
    }
    
    
    /*
     UI 코드 작성
     */
    
    lazy var rankingNameLabel: UILabel = {
        let label = UILabel()
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    lazy var rankingNumLabel: UILabel = {
        let label = UILabel()
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    lazy var percentLabel: UILabel = {
        let label = UILabel()
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func setAutoLayout(){
        rankingNameLabel.snp.makeConstraints({ make in
            make.top.equalTo(contentView.snp.top).offset(10)
            make.centerX.equalToSuperview()
        })
        
        rankingNumLabel.snp.makeConstraints({ make in
            make.center.equalToSuperview()
        })
        
        percentLabel.snp.makeConstraints({ make in
            make.centerX.equalToSuperview()
            make.bottom.equalTo(contentView.snp.bottom).offset(-10)
        })
        
    }
    
    func labelText(_ rankingName: String?, rankingNum: String, _ percent: String?) {
        self.rankingNameLabel.text = rankingName ?? ""
        self.rankingNumLabel.text = rankingNum
        self.percentLabel.text = percent ?? ""
    }
}
