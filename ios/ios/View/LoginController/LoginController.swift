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
        bind()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        klipLoginBtn.isHidden = true
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        klipLoginBtn.isHidden = true
    }
    /*
     UI 코드 작성
     */
    
    // MARK:
    private lazy var fillView: UIView = {
        let view = UIView()
        view.backgroundColor = UIColor(red: 255/255, green: 104/255, blue: 120/255, alpha: 1.0) /* #ff6878 */
        return view
    }()
    
    // MARK:
    private lazy var imgView: UIImageView = {
        let imgView = UIImageView()
        imgView.image = UIImage(named: "2")
        return imgView
    }()
    
    private lazy var klipLoginBtn: CustomLoginButton = {
        let btn = CustomLoginButton()
        btn.inputData(icon: (UIImage(named: "KlipIcon")?.resize(newWidth: 40))!,
                      title: "Klip 로그인")
        btn.backgroundColor = UIColor(red: 45/255, green: 106/255, blue: 255/255, alpha: 1.0) /* #2d6aff */
        btn.clipsToBounds = true
        btn.layer.cornerRadius = 20
        btn.isEnabled = false
        btn.isHidden = true
        btn.addTarget(self, action: #selector(clickedKlipLoginBtn), for: .touchUpInside)
        return btn
    }()
    
    private lazy var goGithubBtn: CustomLoginButton = {
        let btn = CustomLoginButton()
        btn.backgroundColor = UIColor(red: 27/255, green: 31/255, blue: 34/255, alpha: 1.0) /* #1b1f22 */
        btn.inputData(icon: (UIImage(named: "githubIcon")?.resize(newWidth: 40))!,
                      title: "Github 로그인")
        btn.clipsToBounds = true
        btn.layer.cornerRadius = 20
        btn.addTarget(self, action: #selector(clickedGoGihbubBtn), for: .touchUpInside)
        return btn
    }()
    
    
    
    /*
     UI Action 작성
     */
    
    @objc
    private func clickedKlipLoginBtn(){
        LoginViewModel.loginService.prepareKlip()
            .subscribe(onNext: { url in
                self.moveToDeepLink(url)
            })
            .disposed(by: disposeBag)
    }
    
    // DeepLink 이동하는 함수
    private func moveToDeepLink(_ url: String){
        
        if let url = URL(string: url) {
            UIApplication.shared.open(url, options: [:]){ handler in
                LoginViewModel.loginService.getWallet()
                    .subscribe(onNext: { address in
                        print("지갑 주소")
                        print(address)
                    })
                    .disposed(by: self.disposeBag)
            }
        }
        
    }
    
    @objc
    private func clickedGoGihbubBtn(){
        let url = URL(string: APIURL.apiUrl.callBackendForGithubLogin(ip: APIURL.ip))!
        print("url \(url)")
        let urlRequest = URLRequest(url: url)
        let vc = UIViewController()
        let newViewController = UINavigationController(rootViewController: vc )
        let webView = WKWebView(frame: vc.view.bounds)
        newViewController.view.addSubview(webView)
        
        webView.navigationDelegate = self
        webView.load(urlRequest)
        
        webView.goBack()
        webView.reload()
        newViewController.modalPresentationStyle = .fullScreen
        self.present(newViewController,animated: true)
    }
    
    private func deleteCookies(){
        let websiteDataTypes = NSSet(array: [WKWebsiteDataTypeDiskCache, WKWebsiteDataTypeMemoryCache, WKWebsiteDataTypeCookies])
        let date = NSDate(timeIntervalSince1970: 0)
        WKWebsiteDataStore.default().removeData(ofTypes: websiteDataTypes as! Set, modifiedSince: date as Date, completionHandler:{ })
        
        WKWebsiteDataStore.default().fetchDataRecords(ofTypes: WKWebsiteDataStore.allWebsiteDataTypes(), completionHandler: {
            (records) -> Void in
            for record in records{
                WKWebsiteDataStore.default().removeData(ofTypes: record.dataTypes, for: [record], completionHandler: {})
                print("delete cache data")
            }
        })
    }
    
    // 사용자가 인증을 완료했는지 확인하는 함수
    func checkClearAuths(){
        let checkGithubAuth = LoginViewModel.loginService.githubAuthSubject
        let checkklipAuth = LoginViewModel.loginService.klipAuthSubject
        
        Observable.combineLatest(checkGithubAuth, checkklipAuth)
            .subscribe(onNext: { first, second in
                if first && second{
                    
                    let rootView = TabBarViewController()
                    self.klipLoginBtn.isEnabled = false
                    self.goGithubBtn.isEnabled = true
                    self.klipLoginBtn.layer.opacity = 1
                    self.goGithubBtn.layer.opacity = 1
                    rootView.modalPresentationStyle = .fullScreen
                    self.klipLoginBtn.isHidden = true
                    self.present(rootView, animated: true)
                    
                    self.deleteCookies()
                    
                }
                else if first{
                    self.goGithubBtn.layer.opacity = 0.4
                    self.klipLoginBtn.isEnabled = true
                    self.goGithubBtn.isEnabled = false
                }
                else if second{
                    self.klipLoginBtn.layer.opacity = 0.4
                    self.klipLoginBtn.isEnabled = false
                }
                else{
                    self.klipLoginBtn.isEnabled = false
                    self.goGithubBtn.isEnabled = true
                    self.goGithubBtn.layer.opacity = 1
                    self.klipLoginBtn.layer.opacity = 1
                }
            })
            .disposed(by: disposeBag)
    }
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUItoView(){
        view.addSubview(klipLoginBtn)
        view.addSubview(goGithubBtn)
        view.addSubview(imgView)
        view.addSubview(fillView)
        setAutoLayout()
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func setAutoLayout(){
        
        fillView.snp.makeConstraints { make in
            make.top.equalTo(0)
            make.leading.trailing.equalTo(view.safeAreaLayoutGuide)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/4)
        }
        
        imgView.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/5)
            make.leading.trailing.equalTo(view.safeAreaLayoutGuide)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/2)
        }
        
        goGithubBtn.snp.makeConstraints({ make in
            make.top.equalTo(imgView.snp.bottom).offset(30)
            make.centerX.equalTo(view.snp.centerX)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/18)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width*2/3)
        })
        
        klipLoginBtn.snp.makeConstraints({ make in
            make.top.equalTo(goGithubBtn.snp.bottom).offset(30)
            make.centerX.equalTo(view.snp.centerX)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/18)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width*2/3)
        })
        
        
    }
    
    /// MARK:
    private func bind(){
        LoginViewModel.loginService.checkNewMemberInKlip
            .bind { [weak self] check in
                if check{
                    self?.klipLoginBtn.isHidden = false
                    self?.klipLoginBtn.isEnabled = true
                    self?.alert()
                }
            }
            .disposed(by: disposeBag)
    }
    
    // 클립인증 하라는 안내문구
    private func alert(){
        // 팝업창 띄움
        let sheet = UIAlertController(title: "클립 인증 해주세요!", message: nil, preferredStyle: .alert)
        // 팝업창 확인 버튼
        sheet.addAction(UIAlertAction(title: "확인", style: .default))
        // 화면에 표시
        present(sheet,animated: true)
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
                    self.dismiss(animated: true)
                    LoginViewModel.loginService.githubAuthSubject.accept(true)
                    
                    LoginViewModel.loginService.checkLoginUser()
                        .subscribe(onNext: { [weak self] check in
                            guard let self = self else { return }
                            if check{
                                let rootView = TabBarViewController()
                                self.klipLoginBtn.isEnabled = false
                                self.klipLoginBtn.isHidden = true
                                self.goGithubBtn.isEnabled = true
                                
                                self.klipLoginBtn.layer.opacity = 1
                                self.goGithubBtn.layer.opacity = 1
                                rootView.modalPresentationStyle = .fullScreen
                                self.present(rootView, animated: true)
                            }
                        })
                        .disposed(by: self.disposeBag)
                }
                
            }
        }
        
        decisionHandler(.allow)
    }
    
    
    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        print("webview error!!\n\(error)")
    }
    
    
    func webViewWebContentProcessDidTerminate(_ webView: WKWebView) {
        print("\(#function)")
        print("Reload");
        webView.reload()
    }
    
}
