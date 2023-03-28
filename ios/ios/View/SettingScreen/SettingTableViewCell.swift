//
//  SettingTableView.swift
//  ios
//
//  Created by 정호진 on 2023/01/30.
//

import Foundation
import UIKit

final class SettingTableViewCell: UITableViewCell{
    static let identifier = "SettingTableViewCell"
    
    // 클래스 생성자
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        // 셀 내부 간격 설정
        contentView.frame = contentView.frame.inset(by: UIEdgeInsets(top: 10, left: 10, bottom: 10, right: 10))
    }
    
    /*
     UI 작성
     */
    
    // repo title
    lazy var customLabel: UILabel = {
        let label = UILabel()
        contentView.addSubview(label)
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 23)
        
        //AutoLayout 설정
        customLabelLayout(label: label)
        
        return label
    }()
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func customLabelLayout(label: UILabel){
        label.snp.makeConstraints({ make in
            make.top.bottom.equalTo(contentView)
            make.leading.equalTo(contentView)
        })
    }
    
    
    // 라벨에 텍스트 입력
    public func inputDataTableView(text:String, color: UIColor){
        self.customLabel.text = text    // text 데이터
        self.customLabel.textColor = color  // 색상 설정
    }
    
    
}
