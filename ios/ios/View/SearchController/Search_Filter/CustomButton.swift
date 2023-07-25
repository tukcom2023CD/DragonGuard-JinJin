//
//  CustomButton.swift
//  ios
//
//  Created by 정호진 on 2023/05/29.
//

import Foundation
import UIKit
import SnapKit

final class CustomButton: UIButton{
    override init(frame: CGRect) {
        super.init(frame: frame)
        add()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: 돋보기 아이콘 이미지
    private lazy var imgview: UIImageView = {
        let img = UIImageView()
        img.image = UIImage(systemName: "magnifyingglass")
        return img
    }()
    
    // MARK: 검색할 textfield
    lazy var textfield: UITextField = {
        let field = UITextField()
        field.font = .systemFont(ofSize: 20)
        field.textColor = .black
        field.placeholder = "검색어 입력"
        field.attributedPlaceholder = NSAttributedString(string: "검색어 입력", attributes: [NSAttributedString.Key.foregroundColor : UIColor.lightGray])

        return field
    }()
    
    // MARK:
    private func add(){
        self.addSubview(imgview)
        self.addSubview(textfield)
        
        imgview.snp.makeConstraints { make in
            make.trailing.equalTo(textfield.snp.leading).offset(-5)
            make.centerY.equalTo(self.snp.centerY)
        }
        
        textfield.snp.makeConstraints { make in
            make.centerX.equalTo(self.snp.centerX).offset(5)
            make.centerY.equalTo(self.snp.centerY)
        }
    }
    
}
