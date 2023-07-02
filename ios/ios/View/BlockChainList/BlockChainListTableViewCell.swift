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
        label.adjustsFontSizeToFitWidth = true
        label.minimumScaleFactor = 0.5
        label.textAlignment = .center
        return label
    }()
    
    // MARK: 타입
    private lazy var typeLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .center
        return label
    }()
    
    // MARK: 토큰 개수
    private lazy var countLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .center
        return label
    }()
    
    // MARK:
    private lazy var stack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [timeLabel, typeLabel, countLabel])
        stack.axis = .horizontal
        stack.distribution = .fillEqually
        return stack
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(stack)
        
        stack.snp.makeConstraints { make in
            make.top.bottom.trailing.leading.equalToSuperview()
        }
    }
    
    func inputData(time: String, type: String, count: String){
        addUI()
        
        let date = time.split(separator: "T")[0]
        let time = time.split(separator: "T")[1].split(separator: ".")[0]
        
        timeLabel.text = "\(date) \(time)"
        
        typeLabel.text = type
        countLabel.text = count
    }
    
}



