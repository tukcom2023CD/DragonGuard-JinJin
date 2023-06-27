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
//            make.width.equalTo(50)
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
        addUI()
        
        let date = time.split(separator: "T")[0]
        let time = time.split(separator: "T")[1].split(separator: ".")[0]
        
        
        
//        let newSize = timeLabel.sizeThatFits(timeLabel.frame.size) //1
//        let newSize = timeLabel.sizeThatFits( CGSize(width: timeLabel.frame.width, height: CGFloat.greatestFiniteMagnitude)) //2
        let newSize = timeLabel.sizeThatFits(self.frame.size)
        print(self.frame.size)
        print(newSize)
        print("\n")
        
        timeLabel.frame.size = newSize
        timeLabel.text = "\(date) \(time)"
        
        typeLabel.text = type
        countLabel.text = count
        
        
    }
    
}



