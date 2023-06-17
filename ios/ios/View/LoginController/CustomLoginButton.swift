//
//  CustomLoginButton.swift
//  ios
//
//  Created by 정호진 on 2023/06/18.
//

import Foundation
import UIKit
import SnapKit

final class CustomLoginButton: UIButton{
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK:
    private lazy var imgIcon: UIImageView = {
        let imgview = UIImageView()
        
        return imgview
    }()
    
    // MARK:
    private lazy var title: UILabel = {
        let label = UILabel()
        label.textColor = .white
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK:
    private lazy var lineView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        return view
    }()
        
    
    // MARK:
    private func addUI(){
        self.addSubview(imgIcon)
        self.addSubview(title)
        self.addSubview(lineView)
        
        imgIcon.snp.makeConstraints { make in
            make.centerY.equalToSuperview()
            make.leading.equalToSuperview().offset(10)
        }
        
        lineView.snp.makeConstraints { make in
            make.centerY.equalToSuperview()
            make.leading.equalTo(imgIcon.snp.trailing).offset(5)
            make.height.equalToSuperview()
            make.width.equalTo(1)
        }
        
        title.snp.makeConstraints { make in
            make.centerY.equalToSuperview()
            make.centerX.equalToSuperview().offset(20)
        }
    }
    
    func inputData(icon: UIImage, title: String){
        addUI()
        imgIcon.image = icon
        self.title.text = title
    }
}
