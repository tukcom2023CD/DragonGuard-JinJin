//
//  CertifiedOrganizationController.swift
//  ios
//
//  Created by 정호진 on 2023/03/23.
//

import Foundation
import UIKit
import SnapKit

final class CertifiedOrganizationController: UIViewController{
    let deviceWidth = UIScreen.main.bounds.width
    private var type: String = ""
    var delegate: SendOrganizationInfo?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        addToView()
    }
    
    /*
     UI 코드 작성
     */
    
    private lazy var certifiedLabel: UILabel = {
        let label = UILabel()
        label.text = "조직 인증하기"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 40)
        return label
    }()
    
    // MARK: 학교 이름 라벨
    private lazy var organizationName: UILabel = {
        let label = UILabel()
        label.text = "조직"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: 학교 이름 입력하는 텍스트 필드
    private lazy var organizationTextField: UITextField = {
        let field = UITextField()
        field.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        field.placeholder = "조직 이름을 입력하세요"
        return field
    }()
    
    // MARK: 학교 UI들 묶는 StackView
    private lazy var organizationStackView: UIStackView = {
        let stackview = UIStackView()
        stackview.axis = .horizontal
        stackview.alignment = .fill
        stackview.distribution = .equalSpacing
        stackview.spacing = 8
        
        return stackview
    }()
    
    // MARK: 조직 타입 라벨
    private lazy var organizationType: UILabel = {
        let label = UILabel()
        label.text = "타입"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: 타입 선택하는 버튼
    private lazy var typeButton: UIButton = {
        let btn = UIButton()
        btn.setTitle("조직 타입을 선택해주세요", for: .normal)
        btn.setTitleColor(.lightGray, for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.addTarget(self, action: #selector(clickedTypeButton), for: .touchUpInside)
        return btn
    }()
    
    // MARK: 조직 UI들 묶는 StackView
    private lazy var typeStackView: UIStackView = {
        let stackview = UIStackView()
        stackview.axis = .horizontal
        stackview.alignment = .fill
        stackview.distribution = .equalSpacing
        stackview.spacing = 8
        return stackview
    }()
    
    // MARK: 이메일 endpoint 라벨
    private lazy var emailName: UILabel = {
        let label = UILabel()
        label.text = "이메일"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
 
    // MARK: 이메일 입력하는 텍스트 필드
    private lazy var emailTextField: UITextField = {
        let field = UITextField()
        field.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        field.placeholder = "이메일을 입력하세요"
        return field
    }()
    
    // MARK: 이메일 UI 묶는 Stack View
    private lazy var emailStackView: UIStackView = {
        let stackview = UIStackView()
        stackview.axis = .horizontal
        stackview.alignment = .fill
        stackview.distribution = .equalSpacing
        stackview.spacing = 8
        
        return stackview
    }()
    
    // MARK: 전체 stack view 묶는 vertical stack view
    private lazy var allStackView: UIStackView = {
        let stackview = UIStackView()
        stackview.axis = .vertical
        stackview.alignment = .fill
        stackview.distribution = .equalSpacing
        stackview.spacing = 10
        
        return stackview
    }()
    
    // MARK: 모든 선택을 마치고 조직 등록 버튼
    private lazy var doneBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("완료", for: .normal)
        btn.setTitleColor(.black, for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
        btn.layer.cornerRadius = 10
        btn.addTarget(self, action: #selector(clickedDoneBtn), for: .touchUpInside)
        return btn
    }()
    
    
    /*
     UI Action 작성
     */
    
    // MARK: 타입 선택하러 Modal view 방식으로 화면 이동
    @objc private func clickedTypeButton(){
        let chooseOrganizationType = ChooseOrganizationType()
        chooseOrganizationType.delegate = self
        self.present(chooseOrganizationType, animated: true)
    }
    
    // MARK: 조직 등록 버튼을 눌렀을 때
    @objc private func clickedDoneBtn(){
        
        // 조직 인증을 모두 입력한 뒤 전송하는 부분
        if !(organizationTextField.text?.isEmpty ?? false) &&
            !(emailTextField.text?.isEmpty ?? false) &&
            !self.type.isEmpty{
            
           
            
        }
    }
    
    // MARK: view에 UI 추가
    private func addToView(){
        
        self.view.addSubview(certifiedLabel)
        
        // 조직 이름
        self.view.addSubview(organizationStackView)
        [organizationName,organizationTextField].map{
            self.organizationStackView.addArrangedSubview($0)
        }
        
        // 조직 타입
        self.view.addSubview(typeStackView)
        [organizationType,typeButton].map{
            self.typeStackView.addArrangedSubview($0)
        }
        
        // 이메일 endpoint
        self.view.addSubview(emailStackView)
        [emailName,emailTextField].map {
            self.emailStackView.addArrangedSubview($0)
        }
        
        self.view.addSubview(allStackView)
        [organizationStackView,typeStackView,emailStackView].map {
            self.allStackView.addArrangedSubview($0)
        }
        
        self.view.addSubview(doneBtn)
        
        settingAutoLayout()
    }
    
    // MARK: UI AutoLayout 설정
    private func settingAutoLayout(){
        
        self.certifiedLabel.snp.makeConstraints({ make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(50)
            make.centerX.equalToSuperview()
        })
        
        self.allStackView.snp.makeConstraints({ make in
            make.center.equalToSuperview()
        })
        
        self.doneBtn.snp.makeConstraints({ make in
            make.top.equalTo(self.allStackView.snp.bottom).offset(20)
            make.centerX.equalToSuperview()
            make.width.equalTo(self.deviceWidth/5)
        })
    }
    
    
}

// MARK: 선택한 조직 타입 가져옴
extension CertifiedOrganizationController: SendType{
    func sendType(type: String) {
        self.type = type
    }
}

// MARK: 조직 정보 등록했을 때 조직 인증 요청
protocol SendOrganizationInfo{
    func sendOrganizationInfo(name: String,
                              type: String,
                              emailEndpoint: String)
}


/*
 preview
 */

import SwiftUI

struct VCPreViewCertifiedOrganizationController:PreviewProvider {
    static var previews: some View {
        CertifiedOrganizationController().toPreview().previewDevice("iPhone 14 Pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
struct VCPreViewCertifiedOrganizationController2:PreviewProvider {
    static var previews: some View {
        CertifiedOrganizationController().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
