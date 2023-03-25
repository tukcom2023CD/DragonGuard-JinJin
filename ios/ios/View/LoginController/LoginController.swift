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
import Alamofire

final class LoginController: UIViewController{
    let disposeBag = DisposeBag()
    
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
        let newViewController = UIViewController()
        let webView = WKWebView(frame: newViewController.view.bounds)
        newViewController.view.addSubview(webView)
        
        webView.navigationDelegate = self
        webView.goBack()
        webView.load(urlRequest)
        self.navigationController?.pushViewController(newViewController,animated: true)
        
    }

    
    // 사용자가 인증을 완료했는지 확인하는 함수
    func checkClearAuths(){
        let checkGithubAuth = LoginViewModel.loginService.githubAuthSubject
        let checkklipAuth = LoginViewModel.loginService.klipAuthSubject
        
        Observable.combineLatest(checkGithubAuth, checkklipAuth)
            .subscribe(onNext: { first, second in
                if first && second{
                    
                    let rootView = MainController()
                    rootView.jwtToken = Environment.jwtToken
                    let nc = UINavigationController(rootViewController: rootView)
                    let sceneDelegate = UIApplication.shared.connectedScenes.first?.delegate as! SceneDelegate
                    sceneDelegate.window?.rootViewController = nc
                    
                }
                else if first{
                    self.goGithubBtn.backgroundColor = .lightGray
                    self.klipLoginBtn.isEnabled = true
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

extension LoginController: UIWebViewDelegate, WKNavigationDelegate, WKUIDelegate{
    
    // WKNavigationDelegate 메서드 구현
        func webView(_ webView: WKWebView, decidePolicyFor navigationAction: WKNavigationAction,
                     decisionHandler: @escaping (WKNavigationActionPolicy) -> Void) {
            
            // 저장되어 있는 쿠키를 확인 구문
            WKWebsiteDataStore.default().httpCookieStore.getAllCookies { cookies in
                var accessTokenCheck = false
                var refreshTokenCheck = false
                
                for cookie in cookies{
                    if cookie.name == "Access" {
                        UserDefaults.standard.set(cookie.value, forKey:"Access")
                        print("@@@ Access  저장하기: \(cookie.value)")
                        accessTokenCheck = true
                    }
                    if cookie.name == "Refresh" {
                        UserDefaults.standard.set(cookie.value, forKey:"Refresh")
                        print("@@@ Refresh  저장하기: \(cookie.value)")
                        refreshTokenCheck = true
                    }
               
                    if accessTokenCheck && refreshTokenCheck{
                        self.navigationController?.popViewController(animated: true)
                        LoginViewModel.loginService.githubAuthSubject.onNext(true)
                    }
                }
            }
          
            decisionHandler(.allow)
        }
    
}



