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
        self.addSubview(btn)
        
        setAutoLayout()
    }
    
    /*
     UI 코드 작성
     */
    
    lazy var btn: UIButton = {
        let btn = UIButton()
        btn.setTitleColor(.black, for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return btn
    }()
    
    
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func setAutoLayout(){
        btn.snp.makeConstraints({ make in
            make.center.equalToSuperview()
        })
        
    }
    
    
}
