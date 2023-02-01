//
//  SearchPageTableView.swift
//  ios
//
//  Created by 정호진 on 2023/01/25.
//

import UIKit
import SnapKit

final class SearchPageTableView: UITableViewCell{
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
        
        // contentview(테이블 뷰)에 라벨 추가,
        contentView.addSubview(label)
        label.font = UIFont.systemFont(ofSize: 35)
        label.snp.makeConstraints({ make in
            make.top.bottom.equalTo(contentView)
            make.leading.equalTo(contentView).offset(20)
        })
        return label
    }()
    
    
    // 라벨에 텍스트 입력
    public func prepare(text:String){
        self.customLabel.text = text
        self.customLabel.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        
    }
    
    
}

