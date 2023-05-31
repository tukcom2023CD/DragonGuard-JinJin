//
//  BackgroundUIView.swift
//  ios
//
//  Created by 정호진 on 2023/05/29.
//

import Foundation
import UIKit
import SnapKit

final class BackgroundUIView: UIView{
    
    private lazy var backgroundImage: UIImageView = {
        let img = UIImageView()
        
        self.addSubview(img)
        img.snp.makeConstraints { make in
            make.top.equalTo(self.snp.top)
            make.leading.equalTo(self.snp.leading)
            make.trailing.equalTo(self.snp.trailing)
            make.bottom.equalTo(self.snp.bottom)
        }
        return img
    }()
    
    func setImg(imgName: String){
        backgroundImage.image = UIImage(named: imgName)
    }
}
