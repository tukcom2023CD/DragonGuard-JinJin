//
//  CompareRepoTableViewCell.swift
//  ios
//
//  Created by 정호진 on 2023/06/19.
//

import Foundation
import UIKit
import SnapKit

final class CompareTableViewCell: UITableViewCell{
    static let identfier = "CompareTableViewCell"
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: repo1 infomation
    private lazy var repo1Info: UILabel = {
        let label = UILabel()
        
        return label
    }()
    
    // MARK: repo1 infomation
    private lazy var titleInfo: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 25)
        label.adjustsFontForContentSizeCategory = true
        return label
    }()
    
    // MARK: repo1 infomation
    private lazy var repo2Info: UILabel = {
        let label = UILabel()
        
        return label
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(repo1Info)
        self.addSubview(titleInfo)
        self.addSubview(repo2Info)
        
        repo1Info.snp.makeConstraints { make in
            make.leading.equalToSuperview().offset(20)
            make.centerY.equalToSuperview()
        }
        
        titleInfo.snp.makeConstraints { make in
            make.center.equalToSuperview()
        }
        
        repo2Info.snp.makeConstraints { make in
            make.centerY.equalToSuperview()
            make.trailing.equalToSuperview().offset(-20)
        }
    }
    
    func inputData(repo1: Int, title: String, repo2: Int){
        addUI()
        
        repo1Info.text = "\(repo1)"
        titleInfo.text = title
        repo2Info.text = "\(repo2)"
    }
    
    
    
}
