//
//  KlipLoginCheckController.swift
//  ios
//
//  Created by 정호진 on 2023/02/24.
//

import Foundation
import UIKit
import SnapKit

final class KlipLoginCheckView: UIViewController{
    var viewModel: LoginViewModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.view.backgroundColor = .white
        addToView()
        
    }
    
    lazy var checkBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("확인하기", for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 30)
        btn.setTitleColor(.black, for: .normal)
        btn.addTarget(self, action: #selector(clickedCheckBtn), for: .touchUpInside)
        return btn
    }()
    
    private func addToView(){
        self.view.addSubview(checkBtn)
        setAutoLayout()
    }
    
    private func setAutoLayout(){
        checkBtn.snp.makeConstraints({ make in
            make.center.equalToSuperview()
        })
    }
    
    @objc func clickedCheckBtn(){
        viewModel?.getWallet()
        self.dismiss(animated: true)
    }
    
}

