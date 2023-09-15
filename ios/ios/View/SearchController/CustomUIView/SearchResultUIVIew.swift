//
//  SearchResultUIVIew.swift
//  ios
//
//  Created by 정호진 on 2023/05/29.
//

import Foundation
import UIKit
import SnapKit

final class SearchResultUIVIew: UIView{
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        settingUIView()
        add()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: title
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.adjustsFontSizeToFitWidth = true
        label.textColor = .black
        return label
    }()
    
    // MARK: 생성 날짜
    private lazy var createLabel: UILabel = {
        let label = UILabel()
        label.adjustsFontSizeToFitWidth = true
        label.textColor = .black
        return label
    }()
    
    // MARK: 사용한 언어
    private lazy var languageLabel: UILabel = {
        let label = UILabel()
        label.adjustsFontSizeToFitWidth = true
        label.textColor = .black
        return label
    }()
    
    // MARK:
    private func settingUIView(){
        self.layer.cornerRadius = 15
        self.backgroundColor = .white
        self.layer.borderWidth = 1
        self.layer.borderColor = CGColor(red: 255/255, green: 255/255, blue: 255/255, alpha: 0.5)
        self.layer.cornerRadius = 20
        self.layer.shadowOpacity = 0.5
        self.layer.shadowOffset = CGSize(width: 0, height: 3)
    }
    
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
            make.bottom.equalToSuperview().offset(-10)
            make.leading.equalTo(self.titleLabel.snp.leading)
        })
        
        languageLabel.snp.makeConstraints({ make in
            make.bottom.equalToSuperview().offset(-10)
            make.trailing.equalTo(self.snp.trailing).offset(-20)
        })
    }

    
    func inputInfo(title: String, create: String, language: String){
        self.titleLabel.text = title
        if create != "" {
            var date = create.split(separator: "T")
            let time = date[1].split(separator: "Z")
            self.createLabel.text = "\(date[0]) \(time[0])"
        }
        else{
            self.createLabel.text = ""
        }
        self.languageLabel.text = language
    }
    
}
