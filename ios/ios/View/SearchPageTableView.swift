//
//  SearchPageTableView.swift
//  ios
//
//  Created by 정호진 on 2023/01/25.
//

import UIKit
import SnapKit

class SearchPageTableView: UITableViewCell{
    static let identifier = "SearchPageIdentifier"
    
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
    lazy var customLabel: UILabel = {
        let label = UILabel()
        contentView.addSubview(label)
        label.font = UIFont.systemFont(ofSize: 30)
        label.snp.makeConstraints({ make in
            make.top.bottom.equalTo(contentView)
            make.centerX.equalTo(contentView)
        })
        return label
    }()
    
    
    public func prepare(text:String){
        self.customLabel.text = text
    }
    
    
}

