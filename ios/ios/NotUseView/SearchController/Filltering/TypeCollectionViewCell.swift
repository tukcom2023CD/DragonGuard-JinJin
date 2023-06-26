////
////  TypeCollectionViewCell.swift
////  ios
////
////  Created by 정호진 on 2023/03/16.
////
//
//import Foundation
//import UIKit
//import SnapKit
//
//final class TypeCollectionViewCell: UICollectionViewCell{
//    static let identifier = "TypeCollectionViewCell"
//    
//    lazy var label: UILabel = {
//        let label = UILabel()
//        
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15)
//        label.textColor = .black
//        
////        self.addSubview(label)
//        label.snp.makeConstraints({ make in
//            make.center.equalToSuperview()
//        })
//        
//        return label
//    }()
//    
//    
//    func inputText(text: String){
//        label.text = text
//    }
//}
//
//
