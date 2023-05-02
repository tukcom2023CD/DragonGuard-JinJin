//
//  OrganizationSearchTableViewCell.swift
//  ios
//
//  Created by 정호진 on 2023/03/31.
//

import Foundation
import UIKit
import SnapKit

// MARK: 검색 결과 나타낼 tableview cell
final class OrganizationSearchTableViewCell: UITableViewCell {
    static let identifier = "OrganizationSearchTableViewCell"
    
    // MARK: 조직 이름 넣을 라벨
    private lazy var label: UILabel = {
        let label = UILabel()
        label.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        
        self.addSubview(label)
        
        label.snp.makeConstraints({ make in
            make.center.equalToSuperview()
        })
        
        return label
    }()

    
    // MARK: 조직 이름 넣는 함수
    func inputName(name: String){
        label.text = name
    }
    
}

