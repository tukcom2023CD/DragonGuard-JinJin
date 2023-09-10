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
        imgView.clipsToBounds = true
        return imgView
    }()
    
    // MARK: 유저 이름 라벨
    private lazy var nameLabel: UILabel = {
        let label = UILabel()
        label.text = "abc"
        label.textColor = .black
        return label
    }()
    
    // MARK: 유저 기여도 라벨
    private lazy var numLabel: UILabel = {
        let label = UILabel()
        label.text = "11"
        label.textColor = .black
        label.backgroundColor = .clear
        return label
    }()
    
    // MARK:
    private lazy var stackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [nameLabel, numLabel])
        stack.axis = .vertical
        stack.spacing = 10
        stack.backgroundColor = .clear
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
            make.width.equalTo(60)
            make.centerY.equalToSuperview()
        }
        
        
        stackView.snp.makeConstraints { make in
            make.leading.equalTo(userImage.snp.trailing).offset(10)
            make.trailing.equalTo(linkImage.snp.leading).offset(-10)
            make.centerY.equalTo(userImage.snp.centerY)
            
        }
        
        linkImage.snp.makeConstraints { make in
            make.trailing.equalToSuperview().offset(-10)
            make.top.equalTo(stackView.snp.top).offset(-10)
            make.width.height.equalTo(30)
        }
        
    }
    
    func getData(data: AllUserRankingModel){
        addUI_SetAutoLayout()
        
        userImage.load(img: userImage, url: URL(string: data.profile_image ?? "")!, size: 60)
        nameLabel.text = data.github_id
        numLabel.text = "\(data.tokens ?? 0)"
    }
    
    func inputData(data: TypeRankingModel){
        addUI_SetAutoLayout()
        
        switch data.organization_type{
        case "COMPANY":
            userImage.image = UIImage(named: "comapny")?.resize(newWidth: 60)
        case "UNIVERSITY":
            userImage.image = UIImage(named: "university")?.resize(newWidth: 60)
        case "HIGH_SCHOOL":
            userImage.image = UIImage(named: "highschool")?.resize(newWidth: 60)
        case "ETC":
            userImage.image = UIImage(named: "etc")?.resize(newWidth: 60)
        default:
            print("wrong error!\n")
        }
        nameLabel.text = data.name
        numLabel.text = "\(data.token_sum ?? 0)"
    }
    
}
