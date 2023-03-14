//
//  TopicSelectionCollectionViewCell.swift
//  ios
//
//  Created by 정호진 on 2023/03/14.
//

import Foundation
import UIKit

final class TopicSelectionCollectionViewCell: UICollectionViewCell{
    static let identifier = "TopicSelectionCollectionViewCell"
    
    lazy var label: UILabel = {
        let label = UILabel()
        
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15)
        label.textColor = .black
        
        self.addSubview(label)
        label.snp.makeConstraints({ make in
            make.center.equalToSuperview()
        })
        
        return label
    }()
    
    
    func inputText(text: String){
        label.text = text
    }
}
