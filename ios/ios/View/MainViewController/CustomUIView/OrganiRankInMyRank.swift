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
        label.textAlignment = .center
        label.textColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.5)
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: group 내용
    private lazy var gNumLabel1: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textAlignment = .center
        label.textColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.5)
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    
    
    // MARK: group 글자
    private lazy var groupLabel2: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textAlignment = .center
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: group 내용
    private lazy var gNumLabel2: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textAlignment = .center
        label.textColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 1)
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()

    
        
    // MARK: group 글자
    private lazy var groupLabel3: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textAlignment = .center
        label.textColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.5)
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: group 내용
    private lazy var gNumLabel3: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textAlignment = .center
        label.textColor = UIColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.5)
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK:
    private lazy var stack1: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [groupLabel1,groupLabel2,groupLabel3])
        stack.axis = .vertical
        stack.spacing = 10
        stack.distribution = .fillEqually
        return stack
    }()
    
    // MARK:
    private lazy var stack2: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [gNumLabel1,gNumLabel2,gNumLabel3])
        stack.axis = .vertical
        stack.spacing = 10
        stack.distribution = .fillEqually
        return stack
    }()

    
    // MARK:
    private func addUI(){
        addSubview(stack1)
        addSubview(stack2)
        
        stack1.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(30)
            make.leading.equalToSuperview().offset(50)
            make.bottom.equalToSuperview().offset(-20)
        }
        
        stack2.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(30)
            make.trailing.equalToSuperview().offset(-50)
            make.bottom.equalToSuperview().offset(-20)
        }
        
    }
    
    func inputData(rank1: Int?, top: String?, rank2: Int?, me: String?, rank3: Int?, under: String?){
        addUI()
        var check1 = false
        var check2 = false
        var check3 = false
        
        if let rank1 = rank1, let top = top{
            groupLabel1.text = "\(rank1)"
            gNumLabel1.text = top
            check1 = true
        }
        
        if let rank2 = rank2, let me = me{
            groupLabel2.text = "\(rank2)"
            gNumLabel2.text = me
            check2 = true
        }
        
        if let rank3 = rank3, let under = under{
            groupLabel3.text = "\(rank3)"
            gNumLabel3.text = under
            check3 = true
        }
        
        if !check1 && !check2 && !check3{
            groupLabel1.text = ""
            gNumLabel1.text = ""
            groupLabel2.text = ""
            gNumLabel2.text = ""
            groupLabel3.text = ""
            gNumLabel3.text = ""
        }
        
        
    }

}
