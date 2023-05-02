//
//  FAQTableViewCell.swift
//  ios
//
//  Created by 홍길동 on 2023/04/06.
//

import Foundation
import UIKit

final class FAQTableViewCell: UITableViewCell{
    static let identifier = "FAQTableViewCell"
    
    private lazy var label: UILabel = {
       let label = UILabel()
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15)
        label.numberOfLines = 3
        self.addSubview(label)
        label.snp.makeConstraints { make in
            make.centerY.equalTo(self.snp.centerY)
            make.leading.equalTo(10)
        }
        return label
    }()
    
  
    
    func inputText(text: String){
        label.text = text
    }
    
    
    
}
