//
//  CustomUserVue.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import UIKit
import SnapKit

// MARK: tableview 내부에 유저 정보 보여주는 View
final class CustomUserView: UIView{
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: 유저 프로필 이미지
    private lazy var userImage: UIImageView = {
        let imgView = UIImageView()
        imgView.image = UIImage(named: "linkIcon")?.resize(newWidth: 60)
        imgView.layer.cornerRadius = 20
        
        return imgView
    }()
    
    // MARK: 유저 이름 라벨
    private lazy var nameLabel: UILabel = {
        let label = UILabel()
        label.text = "abc"
        return label
    }()
    
    // MARK: 유저 기여도 라벨
    private lazy var numLabel: UILabel = {
        let label = UILabel()
        label.text = "11"
        return label
    }()
    
    // MARK:
    private lazy var stackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [nameLabel, numLabel])
        stack.axis = .vertical
        stack.spacing = 10
        return stack
    }()
    
    // MARK: 링크 이미지
    private lazy var linkImage: UIImageView = {
        let imgView = UIImageView()
        imgView.image = UIImage(named: "linkIcon")?.resize(newWidth: 30)
        return imgView
    }()
    
    // MARK: Setting AutoLayout
    private func addUI_SetAutoLayout(){
        self.addSubview(userImage)
        self.addSubview(stackView)
        self.addSubview(linkImage)
        
        userImage.snp.makeConstraints { make in
            make.leading.equalToSuperview().offset(10)
            make.centerY.equalToSuperview()
        }
        
        
        stackView.snp.makeConstraints { make in
            make.leading.equalTo(userImage.snp.trailing).offset(20)
            make.trailing.equalToSuperview().offset(-20)
            make.centerY.equalTo(userImage.snp.centerY)
            make.width.equalTo(200)
        }
        
        linkImage.snp.makeConstraints { make in
            make.trailing.equalToSuperview().offset(-10)
            make.bottom.equalTo(stackView.snp.top)
        }
        
    }
    
    func getData(data: AllUserRankingModel){
        addUI_SetAutoLayout()
        
        nameLabel.text = data.github_id
        numLabel.text = "\(data.tokens ?? 0)"
    }
    
}
