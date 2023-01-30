//
//  SettingTableView.swift
//  ios
//
//  Created by 정호진 on 2023/01/30.
//

import Foundation
import UIKit

final class SettingTableView: UITableViewCell{
    static let identifier = "SettingTableView"
    
    // 클래스 생성자
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    /*
     UI 작성
     */
    
    // repo title
    lazy var customLabel: UILabel = {
        let label = UILabel()
        contentView.addSubview(label)
        
        label.font = UIFont.systemFont(ofSize: 20)
        label.snp.makeConstraints({ make in
            make.top.bottom.equalTo(contentView)
            make.centerX.equalTo(contentView)
        })
        return label
    }()
    
    // 라벨에 텍스트 입력
    public func inputDataTableView(text:String){
        self.customLabel.text = text
    }
    
    
}
