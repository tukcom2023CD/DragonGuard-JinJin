//
//  CompareRepositorycontroller.swift
//  ios
//
//  Created by 홍길동 on 2023/02/06.
//

import Foundation
import UIKit
import SnapKit

// Repository 비교
final class CompareRepositoryController: UIViewController{
    let deviceHeight = UIScreen.main.bounds.height
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.isHidden = false
        self.view.backgroundColor = .white
        
        addToView()
    }
    
    /*
     UI 코드 작성
     */
    
    lazy var searchLabel: UILabel = {
        let label = UILabel()
        label.text = "레포지토리 선택하기"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 30)
        label.textColor = .black
        return label
    }()
    
    lazy var searchBtn1: UIButton = {
        let btn = UIButton()
        btn.titleColor(for: .normal)
        btn.tintColor = .black
        btn.setTitle("ChooseRepository1 ", for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 20
        btn.layer.borderWidth = 2
        return btn
    }()
    
    lazy var searchBtn2: UIButton = {
        let btn = UIButton()
        btn.titleColor(for: .normal)
        btn.setTitle("Choose Repository2", for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 20
        btn.layer.borderWidth = 2
        return btn
    }()
    
    lazy var nextBtn: UIButton = {
        let btn = UIButton()
        btn.titleColor(for: .normal)
        btn.setTitle("다음", for: .normal)
        btn.backgroundColor = UIColor(red: 48/255, green: 151/255, blue: 255/255, alpha: 1.0) /* #3097ff */
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 20
        btn.layer.borderWidth = 2
        return btn
    }()
    
    /*
     UI Action 작성
     */
    
    private func addToView(){
        self.view.addSubview(searchLabel)
        
        self.view.addSubview(searchBtn1)
        self.view.addSubview(searchBtn2)
        self.view.addSubview(nextBtn)
        
        setAutoLayout()
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func setAutoLayout(){
        searchLabel.snp.makeConstraints({ make in
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.centerX.equalToSuperview()
        })
        
        searchBtn1.snp.makeConstraints({ make in
            make.top.equalTo(self.view.snp.centerY).offset(-50)
            make.height.equalTo(deviceHeight/18)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
        })
        
        searchBtn2.snp.makeConstraints({ make in
            make.top.equalTo(searchBtn1.snp.bottom).offset(20)
            make.height.equalTo(deviceHeight/18)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
        })
        
        nextBtn.snp.makeConstraints({ make in
            make.bottom.equalTo(view.safeAreaLayoutGuide).offset(-30)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
        })
    }
    
}





/*
 SwiftUI preview 사용 코드      =>      Autolayout 및 UI 배치 확인용
 
 preview 실행이 안되는 경우 단축키
 Command + Option + Enter : preview 그리는 캠버스 띄우기
 Command + Option + p : preview 재실행
 */

import SwiftUI

struct VCPreViewCompareRepositoryController:PreviewProvider {
    static var previews: some View {
        CompareRepositoryController().toPreview().previewDevice("iPhone 14 pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
