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
        addUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK:
    private lazy var imgView: UIImageView = {
        let imgView = UIImageView()
        imgView.image = UIImage(systemName: "person.circle")?.resize(newWidth: 80, newHeight: 80)
        imgView.layoutIfNeeded()
        imgView.layer.cornerRadius = 20
        return imgView
    }()
    
    // MARK:
    private lazy var userName: UILabel = {
        let label = UILabel()
        label.textColor = .black
        label.backgroundColor = .clear
        label.text = "User"
        label.adjustsFontSizeToFitWidth = true
        label.minimumScaleFactor = 0.5
        label.textAlignment = .center
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
            make.leading.trailing.equalToSuperview()
        }
    }
    
    func inputData(imgPath: String, name: String){
        imgView.load(img: imgView, url: URL(string: imgPath)!, width: 80, height: 80)
        userName.text = name
    }
    
}
