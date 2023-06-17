//
//  LanguageFiltering.swift
//  ios
//
//  Created by 정호진 on 2023/03/14.
//

import Foundation
import UIKit

final class LanguageSelectionsCollectionViewCell: UICollectionViewCell{
    static let identifier = "LanguageSelectionsCollectionViewCell"
    
    lazy var selectedLang: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14)
        label.textColor = .black

        self.addSubview(label)
        label.snp.makeConstraints({ make in
            make.center.equalToSuperview()
        })
        return label
    }()
    
    func inputText(text: String){
        selectedLang.text = text
    }
    
    
}
