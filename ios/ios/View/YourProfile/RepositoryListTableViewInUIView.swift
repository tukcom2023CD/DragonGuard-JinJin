//
//  RepositoryListTableViewInUIView.swift
//  ios
//
//  Created by 정호진 on 2023/06/21.
//

import Foundation
import UIKit
import SnapKit

final class RepositoryListTableViewInUIView: UIView{
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK:  Repository 이미지
    private lazy var titleImg: UIImageView = {
        let image = UIImageView()
        image.layer.cornerRadius = 10
        image.clipsToBounds = true
        return image
    }()
    
    // MARK: Repository 제목
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
//        label.font = .systemFont(ofSize: 23)
        label.adjustsFontSizeToFitWidth = true
        return label
    }()
    
    // MARK: > 모양 이미지
    private lazy var sendingImage: UIImageView = {
        let image = UIImageView()
        image.image = UIImage(systemName: "chevron.right")?.resize(newWidth: 30)
        image.layer.opacity = 0.2
        image.clipsToBounds = true
        return image
    }()
    
    // MARK: 유저 이름 Reposiotry만 할당
    private lazy var userNameLabel: UILabel = {
        let label = UILabel()
//        label.font = .systemFont(ofSize: 20)
        label.adjustsFontSizeToFitWidth = true
        return label
    }()
    
    
 
    
    // MARK:
    private func addUI_Repository(){
        self.addSubview(titleImg)
        self.addSubview(userNameLabel)
        self.addSubview(titleLabel)
        self.addSubview(sendingImage)
        
        titleImg.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(10)
            make.leading.equalToSuperview().offset(15)
        }
        
        userNameLabel.snp.makeConstraints { make in
            make.centerY.equalTo(titleImg.snp.centerY)
            make.leading.equalTo(titleImg.snp.trailing).offset(5)
        }
        
        titleLabel.snp.makeConstraints { make in
            make.leading.equalTo(titleImg.snp.leading)
            make.top.equalTo(titleImg.snp.bottom).offset(5)
        }
        
        sendingImage.snp.makeConstraints { make in
            make.trailing.equalToSuperview().offset(-10)
            make.centerY.equalToSuperview()
        }
    }
    
    func inputData(imgPath: String?, title: String?, userName: String?){
        addUI_Repository()
        
        titleImg.load(img: titleImg, url: URL(string: imgPath ?? "")!, size: 30)
        titleLabel.text = title ?? "a"
        userNameLabel.text = userName ?? "a"
    }
        
}
