//
//  BlockChainListTableViewCell.swift
//  ios
//
//  Created by 정호진 on 2023/06/18.
//

import Foundation
import SnapKit
import UIKit

final class BlockChainListTableViewCell: UITableViewCell{
    static let identfider = "BlockChainListTableViewCell"
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: 시간
    private lazy var timeLabel: UILabel = {
        let label = UILabel()
        label.text = "a"
        return label
    }()
    
    // MARK: 타입
    private lazy var typeLabel: UILabel = {
        let label = UILabel()
        label.text = "b"
        return label
    }()
    
    // MARK: 토큰 개수
    private lazy var countLabel: UILabel = {
        let label = UILabel()
        label.text = "c"
        return label
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(timeLabel)
        self.addSubview(typeLabel)
        self.addSubview(countLabel)
        
        timeLabel.snp.makeConstraints { make in
            make.leading.equalToSuperview()
            make.centerY.equalToSuperview()
        }
        
        typeLabel.snp.makeConstraints { make in
            make.center.equalToSuperview()
        }
        
        countLabel.snp.makeConstraints { make in
            make.trailing.equalToSuperview()
            make.centerY.equalToSuperview()
        }
        
    }
    
    func inputData(time: String, type: String, count: String){
        timeLabel.text = time
        typeLabel.text = type
        countLabel.text = count
        
        addUI()
    }
    
}
