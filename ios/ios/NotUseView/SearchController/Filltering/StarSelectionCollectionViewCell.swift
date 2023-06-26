////
////  StartSelectionCollectionViewCell.swift
////  ios
////
////  Created by 정호진 on 2023/03/14.
////
//
//import Foundation
//import UIKit
//
//final class StarSelectionCollectionViewCell: UICollectionViewCell{
//    static let identifier = "StartSelectionCollectionViewCell"
//
//    override var isSelected: Bool {
//        didSet{
//           if isSelected {
//               backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 1) /* #ffc2c2 */
//           }
//           else {
//               backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.4) /* #ffc2c2 */
//           }
//       }
//    }
//
//    lazy var label: UILabel = {
//        let label = UILabel()
//
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15)
//        label.textColor = .black
//
//        self.addSubview(label)
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
