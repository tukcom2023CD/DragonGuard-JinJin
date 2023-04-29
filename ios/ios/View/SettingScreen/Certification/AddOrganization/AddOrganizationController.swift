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
    }
    
    /*
     UI 코드 작성
     */
    
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
    
    // MARK: 학교 UI들 묶는 StackView
    private lazy var organizationStackView: UIStackView = {
        let stackview = UIStackView(arrangedSubviews: [organizationName,organizationTextField])
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
    private lazy var typeLabel: UILabel = {
        let label = UILabel()
        label.text = self.viewType
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: 조직 UI들 묶는 StackView
    private lazy var typeStackView: UIStackView = {
        let stackview = UIStackView(arrangedSubviews: [organizationType,typeLabel])
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
        field.textColor = .black
        field.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        field.attributedPlaceholder =  NSAttributedString(string: "이메일을 입력하세요",
                                                              attributes: [NSAttributedString.Key.foregroundColor: UIColor.gray])
        return field
    }()
    
    // MARK: 이메일 UI 묶는 Stack View
    private lazy var emailStackView: UIStackView = {
        let stackview = UIStackView(arrangedSubviews: [emailName,emailTextField])
        stackview.axis = .horizontal
        stackview.alignment = .fill
        stackview.distribution = .equalSpacing
        stackview.spacing = 8
        
        return stackview
    }()
    
    // MARK: 전체 stack view 묶는 vertical stack view
    private lazy var allStackView: UIStackView = {
        let stackview = UIStackView(arrangedSubviews: [typeStackView, organizationStackView, emailStackView])
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
                print("id \(id)")
                if id != 0 {
                    guard let viewControllerStack = self.navigationController?.viewControllers else { return }
                    
                    for viewController in viewControllerStack {
                        if let mainView = viewController as? MainController {
                            self.navigationController?.popToViewController(mainView, animated: true)
                        }
                    }
                }
            }
            .disposed(by: self.disposeBag)
            
        }
    }
    
    // MARK: view에 UI 추가
    private func addToView(){
        
        self.view.addSubview(certifiedLabel)
        
        self.view.addSubview(allStackView)
        
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
//            make.center.equalToSuperview()
            make.centerX.equalToSuperview()
            make.top.equalTo(certifiedLabel.snp.bottom).offset(10)
        })
        
        self.doneBtn.snp.makeConstraints({ make in
            make.top.equalTo(self.allStackView.snp.bottom).offset(20)
            make.centerX.equalToSuperview()
            make.width.equalTo(self.deviceWidth/5)
        })
    }
    
    
}


import SwiftUI

struct VCPreViewAddOrganizationController:PreviewProvider {
    static var previews: some View {
        AddOrganizationController().toPreview().previewDevice("iPhone 14 Pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
struct VCPreViewAddOrganizationController2:PreviewProvider {
    static var previews: some View {
        AddOrganizationController().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
