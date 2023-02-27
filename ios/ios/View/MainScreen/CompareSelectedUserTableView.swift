//
//  CompareSelectedUserTableView.swift
//  ios
//
//  Created by 정호진 on 2023/02/27.
//

import Foundation
import UIKit
import SnapKit

final class CompareSelectedUserTableView: UITableViewCell{
    static let identifier = "CompareSelectedUserTableView"
    
    // 클래스 생성자
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        
        addSubView()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    lazy var userLabel: UILabel = {
       let label = UILabel()
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        label.backgroundColor = .white
        label.textColor = .black
        return label
    }()
    
    
    private func addSubView(){
        contentView.addSubview(userLabel)
        setAutoLayout()
    }
        
    private func setAutoLayout(){
        userLabel.snp.makeConstraints({ make in
            make.center.equalToSuperview()
        })
    }
    
    func setText(text: String){
        self.userLabel.text = text
    }
}
