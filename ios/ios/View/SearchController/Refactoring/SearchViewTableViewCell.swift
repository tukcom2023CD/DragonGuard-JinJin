//
//  SearchViewTableViewCell.swift
//  ios
//
//  Created by 정호진 on 2023/05/29.
//

import UIKit
import SnapKit

final class SearchViewTableViewCell: UITableViewCell{
    static let identifier = "SearchViewTableViewCell"
    
    // 클래스 생성자
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        add()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    
    /*
     UI 작성
     */
    
    // MARK: title
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont.systemFont(ofSize: 25)
        label.textColor = .black
        return label
    }()
    
    // MARK: 생성 날짜
    private lazy var createLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont.systemFont(ofSize: 17)
        label.textColor = .black
        return label
    }()
    
    // MARK: 사용한 언어
    private lazy var languageLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont.systemFont(ofSize: 17)
        label.textColor = .black
        return label
    }()
    
    // MARK:
    private func add(){
        self.addSubview(titleLabel)
        self.addSubview(createLabel)
        self.addSubview(languageLabel)
        
        titleLabel.snp.makeConstraints({ make in
            make.top.equalTo(self.snp.top).offset(10)
            make.leading.equalTo(self.snp.leading).offset(20)
            make.trailing.equalTo(self.snp.trailing).offset(-20)
        })
        
        createLabel.snp.makeConstraints({ make in
            make.top.equalTo(self.titleLabel.snp.bottom).offset(10)
            make.leading.equalTo(self.titleLabel.snp.leading)
        })
        
        languageLabel.snp.makeConstraints({ make in
            make.top.equalTo(self.createLabel.snp.top)
            make.trailing.equalTo(self.snp.trailing).offset(-20)
        })
    }

    
    func inputInfo(title: String, create: String, language: String){
        self.titleLabel.text = title
        self.createLabel.text = create
        self.languageLabel.text = language
        
        
        
    }
    
    
}


