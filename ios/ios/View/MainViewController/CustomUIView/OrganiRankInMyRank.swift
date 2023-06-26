//
//  OrganiRankInMyRank.swift
//  ios
//
//  Created by 정호진 on 2023/06/22.
//

import Foundation
import UIKit
import SnapKit

final class OrganiRankInMyRank: UIView {
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: group 글자
    private lazy var groupLabel1: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.5)
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: group 내용
    private lazy var gNumLabel1: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.5)
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK:
    private lazy var stack1: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [groupLabel1, gNumLabel1])
        stack.axis = .horizontal
        stack.spacing = 20
        stack.distribution = .fillEqually
        return stack
    }()
    
    // MARK: group 글자
    private lazy var groupLabel2: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: group 내용
    private lazy var gNumLabel2: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 1)
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK:
    private lazy var stack2: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [groupLabel2, gNumLabel2])
        stack.axis = .horizontal
        stack.spacing = 20
        stack.distribution = .fillEqually
        return stack
    }()
        
    // MARK: group 글자
    private lazy var groupLabel3: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.5)
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: group 내용
    private lazy var gNumLabel3: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.5)
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK:
    private lazy var stack3: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [groupLabel3, gNumLabel3])
        stack.axis = .horizontal
        stack.spacing = 20
        stack.distribution = .fillEqually
        return stack
    }()
    
    // MARK:
    private lazy var vStack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [stack1,stack2,stack3])
        stack.axis = .vertical
        stack.spacing = 10
        stack.distribution = .fillEqually
        return stack
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(vStack)
        
        vStack.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.top.equalToSuperview().offset(10)
            make.bottom.equalToSuperview().offset(-5)
        }
    }
    
    func inputData(rank1: Int?, top: String?, rank2: Int?, me: String?, rank3: Int?, under: String?){
        addUI()
        
        if let rank1 = rank1, let top = top{
            groupLabel1.text = "\(rank1)"
            gNumLabel1.text = top
        }
        
        if let rank2 = rank2, let me = me{
            groupLabel2.text = "\(rank2)"
            gNumLabel2.text = me
        }
        
        if let rank3 = rank3, let under = under{
            groupLabel3.text = "\(rank3)"
            gNumLabel3.text = under
        }
        
    }
}
