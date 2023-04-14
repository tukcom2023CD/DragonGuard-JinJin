//
//  RepoTableViewCell.swift
//  ios
//
//  Created by 정호진 on 2023/04/14.
//

import Foundation
import UIKit
import SnapKit

final class RepoTableViewCell: UITableViewCell{
    static let identifier = "RepoTableViewCell"
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        
    }
    
    // MARK: repo 이름
    private lazy var repoTitleLabel: UILabel = {
        let label = UILabel()
         label.backgroundColor = .white
        label.textColor = .black
         label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
         return label
    }()
    
    func inputInfo(title: String){
        repoTitleLabel.text = title
    }
    
}
