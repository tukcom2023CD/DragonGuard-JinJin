//
//  UserListTableViewCell.swift
//  ios
//
//  Created by 정호진 on 2023/05/31.
//

import Foundation
import UIKit
import SnapKit

final class UserListCollectionViewCell: UICollectionViewCell{
    static let identifier = "UserListCollectionViewCell"
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        addUI_SetAutoLayout()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // 사용자 이름
    private lazy var nameLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // 커밋 개수 라벨
    private lazy var numLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        label.backgroundColor = .white
        label.textColor = .black
        return label
    }()
    
    // MARK: Add UI and Set AutoLayout
    private func addUI_SetAutoLayout(){
        self.addSubview(numLabel)
        self.addSubview(nameLabel)
        
        numLabel.snp.makeConstraints { make in
            make.centerY.equalToSuperview()
            make.trailing.equalTo(self.snp.trailing).offset(-10)
        }
        
        nameLabel.snp.makeConstraints { make in
            make.centerY.equalToSuperview()
            make.leading.equalTo(self.snp.leading).offset(10)
        }
    }
    
    // input Data
    func inputData(name: String, commits: Int){
        nameLabel.text = name
        numLabel.text = "\(commits)"
    }
}
