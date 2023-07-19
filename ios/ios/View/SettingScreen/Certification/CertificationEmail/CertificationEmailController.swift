//
//  CertificationEmailController.swift
//  ios
//
//  Created by 정호진 on 2023/04/02.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

// MARK: 인증번호 인증하는 화면
final class CertificationEmailController: UIViewController{
    var organizationId: Int?    /// 조직 Id
    var userEmail: String?  /// 사용자 이메일
    private var emailId: Int?   /// 사용자 이메일 Id
    private let disposeBag = DisposeBag()
    private var timer: Timer?
    private var timecount: Double = 0
    private var recordTime: Double = 0  /// 기록 재는 타이머, 소수점 2번째 자리
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        addUIToView()
        sendEmailForAddMember()
        timer = validNumberTimer()
        clickedBackBtn()
    }
    
    // MARK: UI가 로드 된 후 레이아웃 설정 적용
    override func viewDidLayoutSubviews() {
        let border = CALayer()
        border.frame = CGRect(x: 0, y: numberTextField.frame.size.height-1, width: numberTextField.frame.width, height: 1)
        border.borderWidth = 1
        border.borderColor = UIColor.black.cgColor
        numberTextField.layer.addSublayer(border)
        numberTextField.layer.masksToBounds = true
        numberTextField.rightView = timerLabel
        numberTextField.rightViewMode = .always
        checkBtn.layer.cornerRadius = checkBtn.frame.height/5
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
    
    // MARK: 인증번호 입력하라는 문구 라벨
    private lazy var label: UILabel = {
        let label = UILabel()
        label.text = "인증번호를 입력하세요"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 30)
        return label
    }()
    
    // MARK: 인증번호 입력하는 textField
    private lazy var numberTextField: UITextField = {
        let field = UITextField()
        field.textColor = .black
        field.keyboardType = .numberPad
        field.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        field.backgroundColor = .white
        return field
    }()
    
    // MARK: 인증번호 입력 후 확인 버튼
    private lazy var checkBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("확인", for: .normal)
        btn.setTitleColor(.black, for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)   //셀 배경 색상
        btn.addTarget(self, action: #selector(clickedCheckBtn), for: .touchUpInside)
        return btn
    }()
    
    // MARK: 인증번호 재전송 버튼
    private lazy var reSend: UIButton = {
        let btn = UIButton()
        btn.setTitle("인증번호가 안 오나요?", for: .normal)
        btn.setTitleColor(.blue, for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15)
        btn.addTarget(self, action: #selector(clickedReSend), for: .touchUpInside)
        return btn
    }()
    
    private lazy var timerLabel: UILabel = {
        let label = UILabel()
        label.text = "00:00"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14)
        return label
    }()
    
    /*
     UI AutoLayout & Add UI
     */
    
    // MARK: View에 UI 추가
    private func addUIToView(){
        view.addSubview(backBtn)
        view.addSubview(label)
        view.addSubview(numberTextField)
        view.addSubview(checkBtn)
        view.addSubview(reSend)
        
        setAutoLayout()
    }
    
    // MARK: UI AutoLayout
    private func setAutoLayout(){
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(15)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
        }
        
        label.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/4)
            make.centerX.equalToSuperview()
        }
        
        numberTextField.snp.makeConstraints { make in
            make.top.equalTo(label.snp.bottom).offset(view.safeAreaLayoutGuide.layoutFrame.height/10)
            make.centerX.equalToSuperview()
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/2)
        }
        
        checkBtn.snp.makeConstraints { make in
            make.top.equalTo(reSend.snp.bottom).offset(30)
            make.centerX.equalToSuperview()
        }
        
        reSend.snp.makeConstraints { make in
            make.top.equalTo(numberTextField.snp.bottom).offset(5)
            make.trailing.equalTo(numberTextField.snp.trailing)
        }
    }
    
    /*
     UI Action
     */
        
    // MARK: 확인 버튼 누른 경우
    @objc
    private func clickedCheckBtn(){
        guard let codeString = numberTextField.text else {return}
        let certificatedNumber = Int(codeString) ?? 0

        /// 인증번호 5자리가 넘은 경우
        if certificatedNumber > 10000{
            CertifiedOrganizationViewModel.viewModel.checkValidNumber(id: self.emailId ?? 0,
                                                                      code: certificatedNumber,
                                                                      organization_id: self.organizationId ?? 0)
            .subscribe { valid in
                if valid{
                    self.timer?.invalidate()
                    self.timecount = 0
                    self.recordTime = 0
                    // 팝업창 띄움
                    let sheet = UIAlertController(title: "조직이 인증되었습니다.", message: "", preferredStyle: .alert)
                    // 팝업창 확인 버튼
                    
                    sheet.addAction(UIAlertAction(title: "인증 마치기", style: .default,handler: { action in
                        print("done")
                        // 첫 번째 모달 창 닫기
                        self.presentingViewController?.dismiss(animated: false, completion: nil)

                        // 두 번째 모달 창 닫기
                        self.presentingViewController?.presentingViewController?.dismiss(animated: false, completion: nil)

                        // 세 번째 모달 창 닫기
                        self.presentingViewController?.presentingViewController?.presentingViewController?.dismiss(animated: true, completion: nil)


                    }))
                    // 화면에 표시
                    self.present(sheet,animated: true)
                }
                else{
                    self.showAlert(title: "틀렸습니다.",
                              message: "인증번호를 다시 입력하세요",
                              btnTitle: "확인")
                }
            }
            .disposed(by: self.disposeBag)
        }
        else{
            showAlert(title: "인증번호 자리 수가 맞지 않습니다.",
                      message: "다시 입력하세요",
                      btnTitle: "확인")
        }
        
    }
    
    // MARK: 재전송 버튼 누른 경우
    @objc
    private func clickedReSend(){
        timer?.invalidate()
        self.timecount = 0
        self.recordTime = 0
        let _ = validNumberTimer()
    }
    
    // MARK: 멤버 추가하는 함수
    private func sendEmailForAddMember(){
        CertifiedOrganizationViewModel.viewModel.addMember(organizationId: self.organizationId ?? 0,
                                                           email: self.userEmail ?? "")
        .subscribe { emailId in
            self.emailId = emailId
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
    
    // MARK: 인증번호 타이머
    private func validNumberTimer() -> Timer{
        
        let timer = Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true){ t in
            self.timecount += t.timeInterval
            
            let digit: Double = pow(10, 1) // 10의 3제곱
            let timerCount = floor(self.timecount * digit) / digit
            self.recordTime = timerCount
            
            let minute = 4 - (Int)(fmod((timerCount/60), 60))
            let second = 59 - (Int)(fmod(timerCount, 60))

            
            var strMinute: String = "\(minute)"
            var strSecond: String = "\(second)"
            
            if second < 10{
                strSecond = "0\(second)"
            }
            if minute < 10{
                strMinute = "0\(minute)"
            }
            
            self.timerLabel.text = "\(strMinute):" + "\(strSecond)"
            
            if minute == 0 && second == 0{
                t.invalidate()
                self.showAlert(title: "시간 초과",
                          message: "인증 번호를 재전송 해주세요.",
                          btnTitle: "확인")
                /// 인증번호 삭제
                CertifiedOrganizationViewModel.viewModel.removeCertificatedNumber()
            }

        }
        return timer
        
    }
    
    // MARK: 알림 띄우는 함수
    private func showAlert(title: String, message: String, btnTitle: String){
        // 팝업창 띄움
        let sheet = UIAlertController(title: title, message: message, preferredStyle: .alert)
        // 팝업창 확인 버튼
        sheet.addAction(UIAlertAction(title: btnTitle, style: .default))
        // 화면에 표시
        self.present(sheet,animated: true)
    }
    
}
