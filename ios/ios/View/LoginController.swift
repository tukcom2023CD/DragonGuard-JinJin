//
//  LoginController.swift
//  ios
//
//  Created by 정호진 on 2023/02/22.
//

import Foundation
import UIKit
import SnapKit

class LoginController: UIViewController{
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        addUItoView()
    }
    
    /*
     UI 코드 작성
     */
    
    lazy var klipLoginBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("Go Main", for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.layer.borderWidth = 2
        btn.addTarget(self, action: #selector(clickedKlipLoginBtn), for: .touchUpInside)
        return btn
    }()
    
    lazy var goMainBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("Go Main", for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.layer.borderWidth = 2
        btn.addTarget(self, action: #selector(clickedGoMainBtn), for: .touchUpInside)
        return btn
    }()
    
    /*
     UI Action 작성
     */
    
    @objc func clickedKlipLoginBtn(){
        
    }
    
    @objc func clickedGoMainBtn(){
        self.navigationController?.pushViewController(MainController(), animated: true)
    }
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUItoView(){
        self.view.addSubview(klipLoginBtn)
        self.view.addSubview(goMainBtn)
        setAutoLayout()
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func setAutoLayout(){
        klipLoginBtn.snp.makeConstraints({ make in
            make.bottom.equalTo(view.safeAreaLayoutGuide).offset(-100)
            make.leading.equalTo(90)
            make.trailing.equalTo(-90)
        })
        
        goMainBtn.snp.makeConstraints({ make in
            make.top.equalTo(klipLoginBtn.snp.bottom).offset(30)
            make.leading.equalTo(90)
            make.trailing.equalTo(-90)
        })
        
        
    }
    
}

