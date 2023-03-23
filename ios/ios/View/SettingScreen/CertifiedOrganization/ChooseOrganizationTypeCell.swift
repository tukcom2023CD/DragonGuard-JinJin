//
//  ChooseOrganizationTypeCell.swift
//  ios
//
//  Created by 정호진 on 2023/03/23.
//

import Foundation
import UIKit

final class ChooseOrganizationTypeCell: UITableViewCell{
    static let identifier = "ChooseOrganizationTypeCell"
    
    private lazy var label: UILabel = {
        let label = UILabel()
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 30)
        self.addSubview(label)
        label.snp.makeConstraints({ make in
            make.center.equalToSuperview()
        })
        return label
    }()
    
    func inputText(text: String){
        self.label.text = text
    }
    
    
}
