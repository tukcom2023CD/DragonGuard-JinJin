//
//  CertifiedOrganizationController.swift
//  ios
//
//  Created by 정호진 on 2023/03/30.
//

import Foundation
import UIKit
import SnapKit

// MARK:  조직 인증 클래스
final class OrganizationCertificationController: UIViewController{
    private var urlType: String? /// api 통신에 사용할 타입
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        addUIToView()
    }
    
    /*
     UI 코드 작성
     */
    
    // MARK: '조직 인증' 제목 라벨
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.text = "조직 인증"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 30)
        return label
    }()
    
    // MARK: 조직 타입 라벨
    private lazy var typeLabel: UILabel = {
        let label = UILabel()
        label.text = "조직 타입"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: 조직 타입 선택하는 버튼
    private lazy var typeBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("타입을 선택하세요", for: .normal)
        btn.setTitleColor(.gray, for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.addTarget(self, action: #selector(clickedTypeBtn), for: .touchUpInside)
        return btn
    }()
    
    // MARK: 조직 타입 Stack View
    private lazy var typeStackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [typeLabel, typeBtn])
        stack.axis = .horizontal
        stack.distribution = .equalSpacing
        return stack
    }()
    
    // MARK: 조직 이름 라벨
    private lazy var nameLabel: UILabel = {
        let label = UILabel()
        label.text = "조직 이름"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: 조직 이름 선택하는 버튼
    private lazy var nameChooseBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("조직을 선택하세요", for: .normal)
        btn.setTitleColor(.gray, for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.addTarget(self, action: #selector(clickedNameChooseBtnBtn), for: .touchUpInside)
        return btn
    }()
    
    // MARK: 조직 이름 Stack View
    private lazy var nameStackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [nameLabel, nameChooseBtn])
        stack.axis = .horizontal
        stack.distribution = .equalSpacing
        return stack
    }()
    
    // MARK: 사용자 이메일 라벨
    private lazy var userEmailLabel: UILabel = {
        let label = UILabel()
        label.text = "사용자 이메일"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: 사용자 이메일 입력
    private lazy var emailTextField: UITextField = {
        let textField = UITextField()
        textField.attributedPlaceholder =  NSAttributedString(string: "이메일을 입력하세요",
                                                              attributes: [NSAttributedString.Key.foregroundColor: UIColor.gray])
        textField.textColor = .black
        textField.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return textField
    }()
    
    // MARK: 사용자 이메일 Stack View
    private lazy var userEmailStackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [userEmailLabel, emailTextField])
        stack.axis = .horizontal
        stack.spacing = 10
        stack.distribution = .equalSpacing
        return stack
    }()
    
    // MARK: Horizontal StackView 3개 묶는 StackView
    private lazy var verticalStackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [typeStackView, nameStackView, userEmailStackView])
        stack.axis = .vertical
        stack.distribution = .fillEqually
        return stack
    }()
    
    // MARK: 인증 버튼
    private lazy var certifiedBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("인증", for: .normal)
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 10
        btn.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)   //셀 배경 색상
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 25)
        btn.addTarget(self, action: #selector(clickedCertifiedBtn), for: .touchUpInside)
        return btn
    }()
    
    /*
     UI AutoLayout & Add UI
     */
    
    // MARK: UI 등록
    private func addUIToView(){
        self.view.addSubview(titleLabel)
        self.view.addSubview(verticalStackView)
        self.view.addSubview(certifiedBtn)
        setAutoLayout()
    }
    
    // MARK: UI AutoLayout 적용
    private func setAutoLayout(){
        self.titleLabel.snp.makeConstraints({ make in
            make.centerX.equalToSuperview()
            make.top.equalTo(self.view.safeAreaLayoutGuide).offset(UIScreen.main.bounds.height/5)
        })
        
        self.verticalStackView.snp.makeConstraints({ make in
            make.top.equalTo(titleLabel.snp.top).offset(50)
            make.centerX.equalToSuperview()
            make.height.equalTo(UIScreen.main.bounds.height/3)
        })
        
        self.certifiedBtn.snp.makeConstraints({ make in
            make.centerX.equalTo(verticalStackView.snp.centerX)
            make.top.equalTo(verticalStackView.snp.bottom).offset(30)
        })
        
    }
    
    /*
     UI Action
     */
    
    // MARK: 타입 버튼 누른 경우
    @objc
    private func clickedTypeBtn(){
        let typeController = OraganizationTypeTableView()
        typeController.delegate = self
        self.modalPresentationStyle = .formSheet
        
        self.present(typeController, animated: true)
    }
    
    // MARK: 조직 선택 버튼 누른 경우
    @objc
    private func clickedNameChooseBtnBtn(){
        let searchController = OrganizationSearchController()
        
        self.navigationController?.pushViewController(searchController, animated: true)
    }
    
    // MARK: 인증 버튼 누른 경우
    @objc
    private func clickedCertifiedBtn(){
        
    }
    
}

extension OrganizationCertificationController: SendingType{
    
    // MARK: 사용자가 선택한 타입 받아옴
    func sendType(type: String, urlType: String) {
        self.urlType = urlType
        self.typeBtn.setTitle(type, for: .normal)
    }
    
    
}
