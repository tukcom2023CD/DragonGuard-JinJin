//
//  UserProfileImageView.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import UIKit
import SnapKit

final class UserProfileImgView: UIView{
    var userLink: String?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
        addUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    
    // MARK:
    private lazy var userImgView: UIImageView = {
        let imgview = UIImageView()
        imgview.image = UIImage(named: "pomi")?.resize(newWidth: 60)
        imgview.layer.cornerRadius = 20
        imgview.clipsToBounds = true
        return imgview
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(userImgView)
        
        userImgView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(10)
            make.leading.equalToSuperview().offset(10)
            make.trailing.equalToSuperview().offset(-10)
            make.bottom.equalToSuperview().offset(-10)
        }
    }
    
    func updateData(img: String){
        userImgView.load(img: userImgView, url: URL(string: img)!, size: 60)
    }
}
