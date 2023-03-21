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
import WebKit

final class LoginController: UIViewController{
    let disposeBag = DisposeBag()
    let webView = WKWebView()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        addUItoView()
        checkClearAuths()
        
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
        btn.isEnabled = false
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
        LoginViewModel.loginService.prepareKlip()
            .subscribe(onNext: { url in
                self.moveToDeepLink(url)
            })
            .disposed(by: disposeBag)
    }
    
    // DeepLink 이동하는 함수
    private func moveToDeepLink(_ url: String){
        // Klip 앱 이동 후 다른 화면 출력
        let klipCheck = KlipLoginCheckView()
        klipCheck.viewModel = LoginViewModel.loginService
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
    
    @objc func clickedGoGihbubBtn(){
        
        let url = URL(string: APIURL.apiUrl.callBackendForGithubLogin(ip: APIURL.ip))!
        print("url \(url)")
        let urlRequest = URLRequest(url: url)
        webView.load(urlRequest)
        
//        if UIApplication.shared.canOpenURL(url) {
//            let github = SFSafariViewController(url: url)
//            self.present(github, animated: true)
//        }
        
        
        
    }

    
    // 사용자가 인증을 완료했는지 확인하는 함수
    func checkClearAuths(){
        let checkGithubAuth = LoginViewModel.loginService.githubAuthSubject
        let checkklipAuth = LoginViewModel.loginService.klipAuthSubject
        let jwtTokenSubject = LoginViewModel.loginService.jwtTokenSubject
        var jwtToken = ""
        
        jwtTokenSubject.subscribe(onNext: {
            if !$0.isEmpty{
                jwtToken = $0
                self.klipLoginBtn.isEnabled = true
            }
        })
        .disposed(by: self.disposeBag)
        
        
        
        
        Observable.combineLatest(checkGithubAuth, checkklipAuth)
            .subscribe(onNext: { first, second in
                if first && second{
                    
                    let rootView = MainController()
                    rootView.jwtToken = jwtToken
                    let nc = UINavigationController(rootViewController: rootView)
                    let sceneDelegate = UIApplication.shared.connectedScenes.first?.delegate as! SceneDelegate
                    sceneDelegate.window?.rootViewController = nc
                    
                }
                else if first{
                    self.goGithubBtn.backgroundColor = .lightGray
                    self.goGithubBtn.isEnabled = false
                }
                else if second{
                    self.klipLoginBtn.backgroundColor = .lightGray
                    self.klipLoginBtn.isEnabled = false
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

extension LoginController: UIWebViewDelegate, WKNavigationDelegate{
    func webView(_ webView: WKWebView, didReceiveServerRedirectForProvisionalNavigation navigation: WKNavigation!) {
        print("called")
        print(webView.url?.absoluteString)
    }
}
