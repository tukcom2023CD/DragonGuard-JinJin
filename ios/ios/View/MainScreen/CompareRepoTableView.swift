//
//  CompareRepoTableView.swift
//  ios
//
//  Created by 홍길동 on 2023/02/28.
//

import Foundation
import UIKit
import SnapKit

final class CompareRepoTableView : UITableViewCell {
    static let identifier = "CompareRepoIdentifier"
    let deviceWidth = UIScreen.main.bounds.width
    
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
    
    lazy var nameLabel : UILabel = {
        let name = UILabel()
        name.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15)
        name.numberOfLines = 2
        // contentview(테이블 뷰)에 라벨 추가,
        contentView.addSubview(name)
        name.backgroundColor = .white
        name.textColor = .black
        name.textAlignment = .left
        name.snp.makeConstraints({ make in
            make.top.bottom.equalTo(contentView)
            make.leading.equalTo(contentView).offset(20)
        })
        return name
    }()
    
    lazy var repo1Label : UILabel = {
        let name = UILabel()
        name.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15)
        // contentview(테이블 뷰)에 라벨 추가,
        contentView.addSubview(name)
        name.textAlignment = .center
        name.numberOfLines = 2
        name.backgroundColor = .white
        name.textColor = .black
        name.preferredMaxLayoutWidth = deviceWidth/5
        name.snp.makeConstraints({ make in
            make.top.bottom.equalTo(contentView)
            make.centerX.equalToSuperview()
        })
        return name
    }()
    
    lazy var repo2Label : UILabel = {
        let name = UILabel()
        name.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15)
        // contentview(테이블 뷰)에 라벨 추가,
        contentView.addSubview(name)
        name.textAlignment = .right
        name.numberOfLines = 2
        name.backgroundColor = .white
        name.textColor = .black
        name.preferredMaxLayoutWidth = deviceWidth/5
        name.snp.makeConstraints({ make in
            make.top.bottom.equalTo(contentView)
            make.trailing.equalTo(contentView).offset(-20)
        })
        return name
    }()
    // 라벨에 텍스트 입력
    public func prepare(nameLabel:String, repo1Label:String, repo2Label:String){
        self.nameLabel.text = nameLabel
        self.repo1Label.text = repo1Label
        self.repo2Label.text = repo2Label
    }
}
