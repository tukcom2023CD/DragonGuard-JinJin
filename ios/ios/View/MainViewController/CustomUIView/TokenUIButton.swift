//
//  TokenUIButton.swift
//  ios
//
//  Created by 홍길동 on 2023/06/22.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

final class TokenUIButton: UIButton {
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }

    
    
    /*
     UI 작성
     */
    
    //MARK: token 틀
    private lazy var tokenButton: UIButton = {
        let btn = UIButton()
        
        return tokenButton
    }()
    
}

