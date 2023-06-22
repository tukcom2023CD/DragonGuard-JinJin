//
//  ContributorUIView.swift
//  ios
//
//  Created by 정호진 on 2023/06/19.
//

import Foundation
import UIKit
import SnapKit

final class ContributorUIView: UIView{
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK:
    private lazy var imgView1: UIImageView = {
        let imgView = UIImageView()
        imgView.layer.cornerRadius = 10
        imgView.clipsToBounds = true
        imgView.layer.masksToBounds = true
        return imgView
    }()
    
    // MARK:
    private lazy var imgView2: UIImageView = {
        let imgView = UIImageView()
        imgView.layer.cornerRadius = 10
        imgView.clipsToBounds = true
        imgView.layer.masksToBounds = true
        return imgView
    }()
    
    // MARK:
    private lazy var imgView3: UIImageView = {
        let imgView = UIImageView()
        imgView.layer.cornerRadius = 10
        imgView.clipsToBounds = true
        imgView.layer.masksToBounds = true
        return imgView
    }()
    
    // MARK:
    private lazy var imgView4: UIImageView = {
        let imgView = UIImageView()
        imgView.layer.cornerRadius = 10
        imgView.clipsToBounds = true
        imgView.layer.masksToBounds = true
        return imgView
    }()
    
    // MARK:
    private lazy var horStackView1: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [imgView1, imgView2])
        stack.axis = .horizontal
        stack.distribution = .fillEqually
        stack.spacing = 10
        return stack
    }()
    
    // MARK:
    private lazy var horStackView2: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [imgView3, imgView4])
        stack.axis = .horizontal
        stack.distribution = .fillEqually
        stack.spacing = 10
        return stack
    }()
    
    // MARK:
    private lazy var vetiStackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [horStackView1, horStackView2])
        stack.axis = .vertical
        stack.distribution = .fillEqually
        stack.spacing = 10
        return stack
    }()
    
    // MARK: Add UI
    private func addUI(){
        self.addSubview(vetiStackView)
        
        vetiStackView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(10)
            make.leading.equalToSuperview().offset(10)
            make.trailing.equalToSuperview().offset(-10)
            make.bottom.equalToSuperview().offset(-10)
        }
    }
    
    // MARK: input Data
    func inputData(imgList: [String]){
        addUI()
        
        imgView1.image = UIImage(named: "githubIcon")?.resize(newWidth: 30)
        imgView2.image = UIImage(named: "githubIcon")?.resize(newWidth: 30)
        imgView3.image = UIImage(named: "githubIcon")?.resize(newWidth: 30)
        imgView4.image = UIImage(named: "githubIcon")?.resize(newWidth: 30)
    }
    
}
