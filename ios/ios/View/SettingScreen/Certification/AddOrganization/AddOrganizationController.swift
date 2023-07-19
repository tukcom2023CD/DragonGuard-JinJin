//
//  CertifiedOrganizationController.swift
//  ios
//
//  Created by 정호진 on 2023/03/23.
//

import Foundation
import UIKit
import RxSwift
import SnapKit

// MARK: 조직 등록화면
final class AddOrganizationController: UIViewController{
    private let deviceWidth = UIScreen.main.bounds.width
    private let typeList = ["대학교", "회사", "고등학교", "기타"]
    private let urlTypeList = ["UNIVERSITY", "COMPANY", "HIGH_SCHOOL", "ETC"]
    var type: String?
    private var viewType: String?
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        for index in 0..<urlTypeList.count{
            if self.type == urlTypeList[index]{
                self.viewType = typeList[index]
            }
        }
        
        addToView()
        clickedBackBtn()
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
    
    private lazy var certifiedLabel: UILabel = {
        let label = UILabel()
        label.text = "조직 등록하기"
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
        field.textColor = .black
        field.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        field.attributedPlaceholder =  NSAttributedString(string: "조직 이름을 입력하세요",
                                                              attributes: [NSAttributedString.Key.foregroundColor: UIColor.gray])
        return field
    }()
    
    /// MARK:
    private lazy var organizationUIView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        
        return view
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
    private lazy var typeLabel: UILabel = {
        let label = UILabel()
        label.text = self.viewType
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    /// MARK:
    private lazy var typeUIView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        
        return view
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
        field.textColor = .black
        field.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        field.attributedPlaceholder =  NSAttributedString(string: "@ 뒤부터 적어주세요",
                                                          attributes: [NSAttributedString.Key.foregroundColor: UIColor.gray])
        
        return field
    }()
    
    /// MARK:
    private lazy var emailUIView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        
        return view
    }()
    
    // MARK: 전체 stack view 묶는 vertical stack view
    private lazy var allStackView: UIStackView = {
        let stackview = UIStackView(arrangedSubviews: [typeUIView, organizationUIView, emailUIView])
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
    
    
    // MARK: 조직 등록 버튼을 눌렀을 때
    @objc
    private func clickedDoneBtn(){
        guard let name = organizationTextField.text else { return }
        guard let email = emailTextField.text else { return }
        guard let type = self.type else { return }
        
        // 조직 인증을 모두 입력한 뒤 전송하는 부분
        if !name.isEmpty && !email.isEmpty && !type.isEmpty {
            CertifiedOrganizationViewModel.viewModel.addOrganization(name: name,
                                                                     type: type,
                                                                     endPoint: email)
            .subscribe { id in
                if id != 0 {
                    self.presentingViewController?.dismiss(animated: false, completion: nil)

                    // 두 번째 모달 창 닫기
                    self.presentingViewController?.presentingViewController?.dismiss(animated: false, completion: nil)

                    // 세 번째 모달 창 닫기
                    self.presentingViewController?.presentingViewController?.presentingViewController?.dismiss(animated: true, completion: nil)
                }
            }
            .disposed(by: self.disposeBag)
            
        }
    }
    
    // MARK: view에 UI 추가
    private func addToView(){
        
        view.addSubview(backBtn)
        
        view.addSubview(certifiedLabel)
        
        emailUIView.addSubview(emailName)
        emailUIView.addSubview(emailTextField)
        
        typeUIView.addSubview(organizationType)
        typeUIView.addSubview(typeLabel)
        
        organizationUIView.addSubview(organizationName)
        organizationUIView.addSubview(organizationTextField)
        
        view.addSubview(allStackView)
        
        view.addSubview(doneBtn)
        
        settingAutoLayout()
    }
    
    // MARK: UI AutoLayout 설정
    private func settingAutoLayout(){
        
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(15)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
        }
        
        self.certifiedLabel.snp.makeConstraints({ make in
            make.top.equalTo(backBtn.snp.bottom).offset(10)
            make.centerX.equalToSuperview()
        })
        
        emailName.snp.makeConstraints { make in
            make.top.bottom.equalToSuperview()
            make.leading.equalToSuperview()
        }
        
        emailTextField.snp.makeConstraints { make in
            make.top.bottom.equalToSuperview()
            make.trailing.equalToSuperview()
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/2)
        }
        
        organizationType.snp.makeConstraints { make in
            make.top.bottom.equalToSuperview()
            make.leading.equalToSuperview()
        }
        
        typeLabel.snp.makeConstraints { make in
            make.top.bottom.equalToSuperview()
            make.trailing.equalToSuperview()
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/2)
        }
        
        organizationName.snp.makeConstraints { make in
            make.top.bottom.equalToSuperview()
            make.leading.equalToSuperview()
        }
        
        organizationTextField.snp.makeConstraints { make in
            make.top.bottom.equalToSuperview()
            make.trailing.equalToSuperview()
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/2)
        }
        
        self.allStackView.snp.makeConstraints({ make in
            make.leading.equalToSuperview().offset(40)
            make.trailing.equalToSuperview().offset(-40)
            make.top.equalTo(certifiedLabel.snp.bottom).offset(10)
        })
        
        self.doneBtn.snp.makeConstraints({ make in
            make.top.equalTo(self.allStackView.snp.bottom).offset(20)
            make.centerX.equalToSuperview()
            make.width.equalTo(self.deviceWidth/5)
        })
    }
    
    // MARK:
    private func clickedBackBtn(){
        backBtn.rx.tap.subscribe(onNext: {
            self.dismiss(animated: true)
        })
        .disposed(by: disposeBag)
    }
}
