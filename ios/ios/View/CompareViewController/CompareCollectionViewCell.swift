//
//  CompareCollectionViewCell.swift
//  ios
//
//  Created by 정호진 on 2023/06/19.
//

import Foundation
import UIKit
import SnapKit

final class CompareCollectionViewCell: UICollectionViewCell{
    static let identfier = "CompareCollectionViewCell"
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    override var isSelected: Bool{
        didSet{
            if isSelected {
                layer.opacity = 0.5
                shortView.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 1) /* #ffc2c2 */
            }
            else {
                layer.opacity = 1
                shortView.backgroundColor = .clear
            }
        }
    }
    
    // MARK:
    private lazy var label: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 15)
        label.textColor = .black
        return label
    }()
    
    // MARK:
    private lazy var shortView: UIView = {
        let view = UIView()
        return view
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(label)
        self.addSubview(shortView)
        
        label.snp.makeConstraints { make in
            make.center.equalToSuperview()
        }
        
        shortView.snp.makeConstraints { make in
            make.leading.equalTo(label.snp.leading)
            make.trailing.equalTo(label.snp.trailing)
            make.bottom.equalToSuperview()
            make.height.equalTo(2)
        }
    }
    
    func inputData(text: String){
        addUI()
        label.text = text
        label.sizeToFit()
    }
    
}
