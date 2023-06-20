//
//  UserUIView.swift
//  ios
//
//  Created by 정호진 on 2023/06/19.
//

import Foundation
import UIKit
import SnapKit

final class UserUIButton: UIButton{
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK:
    private lazy var imgView: UIImageView = {
        let imgView = UIImageView()
        imgView.image = UIImage(named: "3")?.resize(newWidth: 50, newHeight: 50)
        return imgView
    }()
    
    // MARK:
    private lazy var userName: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 20)
        return label
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(imgView)
        self.addSubview(userName)
        
        imgView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(10)
            make.leading.equalToSuperview().offset(30)
            make.trailing.equalToSuperview().offset(-30)
        }
        
        userName.snp.makeConstraints { make in
            make.top.equalTo(imgView.snp.bottom).offset(20)
            make.centerX.equalTo(imgView.snp.centerX)
        }
    }
    
    func inputData(img: UIImage, name: String){
        addUI()
        imgView.image = img.resize(newWidth: 80, newHeight: 80)
        userName.text = name
    }
    
}
