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
    var id = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        addUItoView()
        combineSubject()
        
        // github Id 서버에 전송한 후 DB id 받음
        viewModel.userGithubId()
            .subscribe(onNext: { id in
                print("DB Id: \(id)")
                self.id = id
            })
            .disposed(by: disposeBag)
        
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
    
    lazy var goGithubBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("Go Github", for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.layer.borderWidth = 2
        btn.addTarget(self, action: #selector(clickedGoGihbubBtn), for: .touchUpInside)
        return btn
    }()
    
    /*
     UI Action 작성
     */
    
    @objc func clickedKlipLoginBtn(){
        self.viewModel.prepareKlip()
            .subscribe(onNext: { url in
                self.moveToDeepLink(url)
            })
            .disposed(by: disposeBag)
    }
    
    // DeepLink 이동하는 함수
    private func moveToDeepLink(_ url: String){
        // Klip 앱 이동 후 다른 화면 출력
        let klipCheck = KlipLoginCheckView()
        klipCheck.viewModel = self.viewModel
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
        
        // KLIP 동의 시 KLIP 동의 하는 버튼 막기
//        self.viewModel.chekcKlipAuthSubject
//            .subscribe(onNext: { check in
//                self.klipLoginBtn.isEnabled = false
//                self.klipLoginBtn.backgroundColor = .lightGray
//            })
//            .disposed(by: disposeBag)
    }
    
    @objc func clickedGoGihbubBtn(){
        
        let scope = "user"
        let url = APIURL.apiUrl.githubGetAPI()
        var component = URLComponents(string: url)
        component?.queryItems = [
            URLQueryItem(name: "client_id", value: Environment.clientId),
            URLQueryItem(name: "scope", value: scope)
        ]
        
        guard let url = component?.url else { return }
        if UIApplication.shared.canOpenURL(url) {
            let github = SFSafariViewController(url: url)
            self.present(github, animated: true)
        }
        
        
//        self.viewModel.chekcGithubAuthSubject
//            .subscribe(onNext: { check in
//                self.goGithubBtn.isEnabled = false
//                self.goGithubBtn.backgroundColor = .lightGray
//                print("check \(check)")
//            })
//            .disposed(by: disposeBag)
    }
    
    func combineSubject(){
        self.viewModel.checkClearAllAuth()
            .subscribe(onNext: { check in
                if check {
                    print("check")
                }
            })
            .disposed(by: disposeBag)
    }
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUItoView(){
        self.view.addSubview(klipLoginBtn)
        self.view.addSubview(goGithubBtn)
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
        
        goGithubBtn.snp.makeConstraints({ make in
            make.top.equalTo(klipLoginBtn.snp.bottom).offset(30)
            make.leading.equalTo(90)
            make.trailing.equalTo(-90)
        })
        
        
    }
    
}
