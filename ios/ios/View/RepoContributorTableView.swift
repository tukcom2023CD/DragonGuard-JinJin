//
//  RepoContributorTableView.swift
//  ios
//
//  Created by 정호진 on 2023/02/08.
//

import Foundation
import UIKit
import SnapKit

final class RepoContributorTableView: UITableViewCell{
    static let identifier = "RepoContributorTableView"
    
    // 클래스 생성자
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        
        addSubView()
        setAutoLayout()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    /*
     UI 작성
     */
    
    // 순위 매기는 라벨
    lazy var numLabel: UILabel = {
        let label = UILabel()
        
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        label.backgroundColor = .white
        label.textColor = .black
//        label.layer.borderWidth = 1
        return label
    }()
    
    // 사용자 이름
    lazy var nameLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // 색 표시
    lazy var colorView: UIButton = {
        let btn = UIButton()
        btn.isEnabled = false
        btn.layer.cornerRadius = contentView.bounds.height / 2 - 5
        return btn
    }()
    
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addSubView(){
        contentView.addSubview(numLabel)
        contentView.addSubview(nameLabel)
        contentView.addSubview(colorView)
    }
    
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것 (추천)
     */
    
    private func setAutoLayout(){
        numLabel.snp.makeConstraints({ make in
            make.top.equalTo(5)
            make.leading.equalTo(contentView.snp.leading).offset(10)
            make.width.equalTo(contentView.bounds.width / 5)
        })
        
        nameLabel.snp.makeConstraints({ make in
            make.leading.equalTo(numLabel.snp.trailing)
            make.top.equalTo(5)
        })
        
        colorView.snp.makeConstraints({ make in
            make.top.equalTo(5)
            make.trailing.equalTo(contentView.snp.trailing).offset(-10)
            make.width.equalTo(contentView.bounds.height - 10)
        })
    }
    
    
    public func setLabel(num: Int, name: String, color: UIColor) {
        self.numLabel.text = String(num)
        self.nameLabel.text = name
        self.colorView.backgroundColor = color
    }
    
}
