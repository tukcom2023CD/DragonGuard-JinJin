//
//  TierTokenCustomUIView.swift
//  ios
//
//  Created by 홍길동 on 2023/02/10.
//

import Foundation
import UIKit

class TierTokenCustomUIView : UIView {
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
        return myTierLabel
    }()
    
    // 실제 티어 결과값 부분
    lazy var resultTierLabel : UILabel = {
        let resultTierLabel = UILabel()
        resultTierLabel.text = "루비"
        return resultTierLabel
    }()
    
    // "내 토큰" 부분
    lazy var myTokenLabel : UILabel = {
        let myTokenLabel = UILabel()
        myTokenLabel.text = "내 토큰 : "
        return myTokenLabel
    }()
    
    // 실제 토큰 결과값 부분
    lazy var resultTokenLabel : UILabel = {
        let resultTokenLabel = UILabel()
        resultTokenLabel.text = "28.7 T"
        return resultTokenLabel
    }()
    
    // UI 등록 함수
    private func addUItoView(){
        addSubview(myTierLabel)
        addSubview(resultTierLabel)
        addSubview(myTokenLabel)
        addSubview(resultTokenLabel)
    }
    
    // UI AutoLayout
    private func settingAutoLayout(){
        
        
        
    }
    
}


