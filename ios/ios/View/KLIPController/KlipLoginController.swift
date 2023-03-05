//
//  LoginController.swift
//  ios
//
//  Created by 정호진 on 2023/02/22.
//

import Foundation
import UIKit
import SnapKit
import RxSwift
import SafariServices

final class KlipLoginController: UIViewController{
    
    let viewModel = LoginViewModel()
    let disposeBag = DisposeBag()
    var walletAddress: String?
    var id: Int? // user DB Id
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        addUItoView()
        NotificationCenter.default.addObserver(self, selector: #selector(finishedWalletAddress(notification: )), name: Notification.Name.walletAddress, object: nil)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        viewModel.sendUserGithubId()
    }
    /*
     UI 코드 작성
     */
    
    lazy var klipLoginBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("Go KLIP", for: .normal)
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
        viewModel.goKlip()
        NotificationCenter.default.addObserver(self, selector: #selector(notificationData(notification: )), name: Notification.Name.deepLink, object: nil)
    }
    
    @objc func clickedGoMainBtn(){
        self.navigationController?.pushViewController(MainController(), animated: true)
    }
    
    
    // KLIP deep link 이동
    @objc func notificationData(notification: Notification){
        guard let url = notification.userInfo?[NotificationDeepLinkKey.link] as? String else {return}
        
        // Klip 앱 이동 후 다른 화면 출력
        let klipCheck = KlipLoginCheckView()
        klipCheck.viewModel = viewModel
        self.present(klipCheck, animated: true)
        
        // 사용자 기본 브라우저에서 deeplink 주소 열 수 있는지 확인 후 열기
        if let url = URL(string: url) {
            // 사용자의 기본 브라우저에서 url 확인
            if UIApplication.shared.canOpenURL(url) {
                // 사용자 기본 브라우저에서 url 열기
                UIApplication.shared.open(url, options: [:]){ handler in
                    let configuration = SFSafariViewController.Configuration()
                    configuration.entersReaderIfAvailable = false
                    let deepLinkView = SFSafariViewController(url: url,configuration: configuration)
                    self.present(deepLinkView, animated: true)
                }
            } else {
                print("기본 브라우저를 열 수 없습니다.")
            }
        }
        
    }
    
    @objc func finishedWalletAddress(notification: Notification){
        guard let address = notification.userInfo?[NotificationWalletAddress.walletAddress] as? String else {return}
        self.walletAddress = address
        
        guard let address = self.walletAddress else { return }
        if !address.isEmpty{
            self.navigationController?.pushViewController(MainController(), animated: true)
        }
        
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

