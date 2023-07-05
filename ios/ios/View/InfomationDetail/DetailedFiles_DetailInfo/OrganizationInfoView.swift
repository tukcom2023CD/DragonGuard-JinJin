//
//  OrganizationInfoView.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import UIKit
import SnapKit

final class OrganizationInfoView: UIView{
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: Organization or Repository 이미지
    private lazy var titleImg: UIImageView = {
        let image = UIImageView()
        image.layer.cornerRadius = 20
        image.clipsToBounds = true
        return image
    }()
    
    // MARK: Organization or Repository 제목
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 25)
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
    
    // MARK:
    private func addUI_Organization(){
        self.addSubview(titleImg)
        self.addSubview(titleLabel)
        self.addSubview(sendingImage)
        
        titleImg.snp.makeConstraints { make in
            make.leading.equalToSuperview().offset(10)
            make.centerY.equalToSuperview()
            
        }
        
        titleLabel.snp.makeConstraints { make in
            make.leading.equalTo(titleImg.snp.trailing).offset(10)
            make.centerY.equalToSuperview()
        }
        
        sendingImage.snp.makeConstraints { make in
            make.trailing.equalToSuperview().offset(-10)
            make.centerY.equalToSuperview()
        }
    }
    
    func inputData(imgPath: String, title: String){
        addUI_Organization()
        titleImg.load(img: titleImg, url: URL(string: imgPath)!, width: 60, height: 60)
//        titleImg.image = UIImage(named: "pomi")?.resize(newWidth: 60, newHeight: 60)
        titleLabel.text = title
    }
    
}
