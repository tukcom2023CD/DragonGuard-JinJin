////
////  LanguageFillteringTableViewCell.swift
////  ios
////
////  Created by 정호진 on 2023/03/07.
////
//
//import Foundation
//import UIKit
//
//final class LanguageFillteringTableViewCell: UITableViewCell{
//    static let identifier = "LanguageFillteringTableViewCell"
//    
//    lazy var setLanguageLabel : UILabel = {
//        let label = UILabel()
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
//        label.textColor = .black
//        label.textAlignment = .center
//        return label
//    }()
//    
//    func setAttribute(){
//        self.addSubview(setLanguageLabel)
//        setLanguageLabel.snp.makeConstraints({ make in
//            make.center.equalToSuperview()
//        })
//    }
//    
//    
//    func setLangugae(text: String){
//        setAttribute()
//        self.setLanguageLabel.text = text
//    }
//}
