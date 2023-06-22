//
//  ContributorAutoCollectionViewCell.swift
//  ios
//
//  Created by 정호진 on 2023/06/22.
//

import Foundation
import UIKit
import SnapKit

final class ContributorAutoCollectionViewCell: UICollectionViewCell{
    static let identifier = "ContributorAutoCollectionViewCell"
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK:
    private lazy var title: UILabel = {
        let label = UILabel()
        
        return label
    }()
    
    // MARK:
    private lazy var num: UILabel = {
        let label = UILabel()
        
        return label
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(title)
        self.addSubview(num)
        
        title.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(10)
            make.centerX.equalToSuperview()
        }
        
        num.snp.makeConstraints { make in
            make.top.equalTo(title.snp.bottom).offset(10)
            make.centerX.equalTo(title.snp.centerX)
        }
    }
    
    func inputData(title: String, num: Int){
        addUI()
        self.title.text = title
        self.num.text = "\(num)"
    }
    
}
