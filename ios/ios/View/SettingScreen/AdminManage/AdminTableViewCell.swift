//
//  AdminTableViewCell.swift
//  ios
//
//  Created by 정호진 on 2023/05/05.
//

import Foundation
import UIKit
import SnapKit

final class AdminTableViewCell: UITableViewCell{
    static let identifier = "AdminTableViewCell"
    
    private lazy var label: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        label.textColor = .black
        label.backgroundColor = .clear
        self.addSubview(label)
        label.snp.makeConstraints { make in
            make.center.equalToSuperview()
        }
        return label
    }()
    
    func inputText(text: String){
        self.label.text = text
    }
    
}
