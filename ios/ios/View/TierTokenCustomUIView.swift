//
//  TierTokenCustomUIView.swift
//  ios
//
//  Created by 홍길동 on 2023/02/10.
//

import Foundation
import UIKit

final class TierTokenCustomUIView : UIView {
    override init(frame: CGRect) {
        super.init(frame: frame)
        // UI view에 적용
        addUItoView()
        
        // UI AutoLayout 적용
        settingAutoLayout()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    /*
     UI 코드 작성
     */
    
    // "내 티어" 부분
    lazy var myTierLabel : UILabel = {
        let myTierLabel = UILabel()
        myTierLabel.text = "내 티어 : "
        myTierLabel.textColor = .black
        myTierLabel.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 17)
        return myTierLabel
    }()
    
    // 실제 티어 결과값 부분
    lazy var resultTierLabel : UILabel = {
        let resultTierLabel = UILabel()
        resultTierLabel.textColor = .black
        resultTierLabel.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 17)
        return resultTierLabel
    }()
    
    // "내 토큰" 부분
    lazy var myTokenLabel : UILabel = {
        let myTokenLabel = UILabel()
        myTokenLabel.textColor = .black
        myTokenLabel.text = "내 토큰 : "
        myTokenLabel.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 17)
        return myTokenLabel
    }()
    
    // 실제 토큰 결과값 부분
    lazy var resultTokenLabel : UILabel = {
        let resultTokenLabel = UILabel()
        resultTokenLabel.textColor = .black
        resultTokenLabel.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 17)
        return resultTokenLabel
    }()
    
    // 티어 이미지 부분 (일단 포미 사진 넣음)
    
    lazy var tierImage : UIImageView = {
        let tierImage = UIImageView()
//        tierImage.image = UIImage(named: "img1")?.resize(newWidth: 60)
        return tierImage
    }()
    
    // UI 등록 함수
    private func addUItoView(){
        addSubview(myTierLabel)
        addSubview(resultTierLabel)
        addSubview(myTokenLabel)
        addSubview(resultTokenLabel)
        addSubview(tierImage)
    }
    
    
    // UI AutoLayout
    private func settingAutoLayout(){
        
        myTierLabel.snp.makeConstraints({ make in
            make.top.equalTo(30)
            make.leading.equalTo(20)
        })
        
        resultTierLabel.snp.makeConstraints({ make in
            make.top.equalTo(30)
            make.leading.equalTo(myTierLabel.snp.trailing).offset(10)
        })
        
        myTokenLabel.snp.makeConstraints({ make in
            make.leading.equalTo(20)
            make.bottom.equalTo(-30)
        })
        
        resultTokenLabel.snp.makeConstraints({ make in
            make.leading.equalTo(myTokenLabel.snp.trailing).offset(10)
            make.bottom.equalTo(-30)
        })
        
        tierImage.snp.makeConstraints({ make in
            make.top.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(-30)
        })
    }
    
    func inputText(myTier: String, tokens: Int){
        resultTierLabel.text = myTier
        resultTokenLabel.text = "\(tokens)"
    }
    
}


