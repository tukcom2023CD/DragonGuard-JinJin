//
//  CertifiedOrganizationController.swift
//  ios
//
//  Created by 정호진 on 2023/03/30.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

// MARK:  조직 인증 클래스
final class OrganizationCertificationController: UIViewController{
    private var urlType: String? /// api 통신에 사용할 타입
    private var userOrganizationName: String?   /// 사용자 조직 이름
    private var organizationId: Int?    /// 조직 아이디
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        addUIToView()
    }
    
    /*
     UI 코드 작성
     */
    
    // MARK: 뒤로가기 버튼
    private lazy var backBtn: UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(named: "backBtn")?.resize(newWidth: 30), for: .normal)
        return btn
    }()
    
    // MARK: '조직 인증' 제목 라벨
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.text = "조직 인증"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 30)
        label.backgroundColor = .clear
        return label
    }()
    
    // MARK: 조직 타입 라벨
    private lazy var typeLabel: UILabel = {
        let label = UILabel()
        label.text = "조직 타입"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        label.backgroundColor = .clear
        return label
    }()
    
    // MARK: 조직 타입 선택하는 버튼
    private lazy var typeBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("타입을 선택하세요", for: .normal)
        btn.setTitleColor(.gray, for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.addTarget(self, action: #selector(clickedTypeBtn), for: .touchUpInside)
        btn.backgroundColor = .clear
        return btn
    }()
    
    // MARK: 조직 타입 Stack View
    private lazy var typeStackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [typeLabel, typeBtn])
        stack.axis = .horizontal
        stack.distribution = .equalSpacing
        stack.backgroundColor = .clear
        return stack
    }()
    
    // MARK: 조직 이름 라벨
    private lazy var nameLabel: UILabel = {
        let label = UILabel()
        label.text = "조직 이름"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        label.backgroundColor = .clear
        return label
    }()
    
    // MARK: 조직 이름 선택하는 버튼
    private lazy var nameChooseBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("조직을 선택하세요", for: .normal)
        btn.setTitleColor(.gray, for: .normal)
        btn.isEnabled = false
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.addTarget(self, action: #selector(clickedNameChooseBtnBtn), for: .touchUpInside)
        btn.backgroundColor = .white
        return btn
    }()
    
    // MARK: 조직 이름 Stack View
    private lazy var nameStackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [nameLabel, nameChooseBtn])
        stack.axis = .horizontal
        stack.distribution = .equalSpacing
        stack.backgroundColor = .clear
        return stack
    }()
    
    // MARK: 사용자 이메일 라벨
    private lazy var userEmailLabel: UILabel = {
        let label = UILabel()
        label.text = "사용자 이메일"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        label.backgroundColor = .clear
        return label
    }()
    
    // MARK: 사용자 이메일 입력
    private lazy var emailTextField: UITextField = {
        let textField = UITextField()
        textField.attributedPlaceholder =  NSAttributedString(string: "이메일을 입력하세요",
                                                              attributes: [NSAttributedString.Key.foregroundColor: UIColor.gray])
        textField.textColor = .black
        textField.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        textField.textAlignment = .right
        textField.backgroundColor = .white
        return textField
    }()
    
    // MARK: 사용자 이메일 Stack View
    private lazy var userEmailStackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [userEmailLabel, emailTextField])
        stack.axis = .horizontal
        stack.spacing = 10
        stack.distribution = .equalSpacing
        stack.backgroundColor = .white
        return stack
    }()
    
    // MARK: Horizontal StackView 3개 묶는 StackView
    private lazy var verticalStackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [typeStackView, nameStackView, userEmailStackView])
        stack.axis = .vertical
        stack.distribution = .fillEqually
        stack.backgroundColor = .white
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
        self.view.addSubview(backBtn)
        self.view.addSubview(titleLabel)
        self.view.addSubview(verticalStackView)
        self.view.addSubview(certifiedBtn)
        setAutoLayout()
        clickedBackBtn()
    }
    
    // MARK: UI AutoLayout 적용
    private func setAutoLayout(){
        
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(15)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
        }
        
        self.titleLabel.snp.makeConstraints({ make in
            make.centerX.equalToSuperview()
            make.top.equalTo(self.view.safeAreaLayoutGuide).offset(UIScreen.main.bounds.height/20)
        })
        
        self.verticalStackView.snp.makeConstraints({ make in
            make.top.equalTo(titleLabel.snp.top).offset(50)
            make.centerX.equalToSuperview()
            make.height.equalTo(UIScreen.main.bounds.height/4)
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
        if self.urlType != nil {
            let searchController = OrganizationSearchController()
            searchController.type = self.urlType
            searchController.delegate = self
            searchController.modalPresentationStyle = .fullScreen
            present(searchController, animated: true)
        }
    }
    
    // MARK: 인증 버튼 누른 경우
    @objc
    private func clickedCertifiedBtn(){
        /// 이메일 형식 확인하는 구문
        let emailCheck = CertifiedOrganizationViewModel.viewModel.checkEmail(userEmail: self.emailTextField.text ?? "")
        /// 타입 및 조직 이름 선택했는지 확인
        let otherCheck = CertifiedOrganizationViewModel.viewModel.checkTypeAndName(type: self.urlType ?? "",
                                                                                   name: self.userOrganizationName ?? "")
        /// 모두 다 선택했는지 확인하는 코드
        Observable.combineLatest(emailCheck, otherCheck)
            .subscribe { first, second in
                if first && second{
                    let certificatedEmail = CertificationEmailController()
                    certificatedEmail.organizationId = self.organizationId
                    certificatedEmail.userEmail = self.emailTextField.text ?? ""
                    certificatedEmail.modalPresentationStyle = .fullScreen
                    self.present(certificatedEmail, animated: false)

                }
                else{
                    // 팝업창 띄움
                    let sheet = UIAlertController(title: "오류!", message: "잘못된 정보 입니다.", preferredStyle: .alert)
                    // 팝업창 확인 버튼
                    sheet.addAction(UIAlertAction(title: "확인", style: .default))
                    // 화면에 표시
                    self.present(sheet,animated: true)
                }
                
            }
            .disposed(by: self.disposeBag)
        
    }
    
    // MARK:
    private func clickedBackBtn(){
        backBtn.rx.tap.subscribe(onNext: {
            self.dismiss(animated: true)
        })
        .disposed(by: disposeBag)
    }
    
}

extension OrganizationCertificationController: SendingType, SendingOrganizationName{
    
    // MARK: 검색하고 조직을 선택한 경우
    func sendName(name: String, organizationId: Int) {
        self.userOrganizationName = name
        self.organizationId = organizationId
        self.nameChooseBtn.setTitle(name, for: .normal)
        self.nameChooseBtn.setTitleColor(.black, for: .normal)
    }
    
    
    // MARK: 사용자가 선택한 타입 받아옴
    func sendType(type: String, urlType: String) {
        print("urltype \(urlType)")
        self.urlType = urlType
        self.typeBtn.setTitle(type, for: .normal)
        self.typeBtn.setTitleColor(.black, for: .normal)
        self.nameChooseBtn.isEnabled = true
    }
    
    
}
