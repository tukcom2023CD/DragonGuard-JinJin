//
//  WatchRankingCollectionView.swift
//  ios
//
//  Created by 정호진 on 2023/01/31.
//

import Foundation
import UIKit

class WatchRankingCollectionView: UICollectionViewCell {
    static let identifier = "watchRankingCollectionView"
    
    override init(frame: CGRect) {
          super.init(frame: frame)
          self.cellSetting()
      }
      
      required init?(coder: NSCoder) {
          fatalError("init(coder:) has not been implemented")
      }
    
    func cellSetting() {
        self.backgroundColor = .white
        self.addSubview(customLabel)
        
        setAutoLayout()
    }
    
    /*
     UI 코드 작성
     */
    
    lazy var customLabel: UILabel = {
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
        customLabel.snp.makeConstraints({ make in
            make.center.equalToSuperview()
        })
        
    }
    
    
}