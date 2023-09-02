//
//  SceneDelegate.swift
//  ios
//
//  Created by 정호진 on 2023/01/03.
//

import UIKit
import Alamofire
import WebKit
import RxSwift

class SceneDelegate: UIResponder, UIWindowSceneDelegate{
    var window: UIWindow?
    private let mainService = MainService()
    private let disposeBag = DisposeBag()
    
    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        guard let windowScene = (scene as? UIWindowScene) else { return }
        window = UIWindow(windowScene: windowScene)
        
        if let accessToken = UserDefaults.standard.string(forKey: "Access"),
           let refreshToken = UserDefaults.standard.string(forKey: "Refresh"){
            
            checkValidUser(accessToken: accessToken, refreshToken: refreshToken)
                .subscribe(onNext: {
                    self.checkingLoginKlip()
                        .subscribe(onNext: { check in
                            if check{
                                self.moveMainController()
                            }
                            else{
                                self.moveLoginController()
                            }
                        })
                        .disposed(by: self.disposeBag)
                })
                .disposed(by: disposeBag)
        }
        else{
            moveLoginController()
        }
    }
    
    // MARK: go to LoginController
    func moveLoginController(){
        let rootView = LoginController()
        window?.rootViewController = rootView
        window?.makeKeyAndVisible()
    }
    
    // MARK: If success User, go to MainController
    func moveMainController(){
        let rootView = TabBarViewController()
        window?.rootViewController = rootView
        window?.makeKeyAndVisible()
    }
    
    // MARK: Check User Refresh Token
    func checkRefreshToken(refreshToken: String){
        let url = APIURL.apiUrl.getRefreshToken(ip: APIURL.ip)
        
        guard let accessToken = UserDefaults.standard.string(forKey: "Access") else {return}
        guard let refreshToken = UserDefaults.standard.string(forKey: "Refresh") else {return}
        
        AF.request(url,
                   headers: ["accessToken" : accessToken, "refreshToken" : refreshToken])
        .validate(statusCode: 200..<201)
        .responseDecodable(of: TokenModel.self){ response in
            switch response.result{
            case .success(let data):
                UserDefaults.standard.set(data.accessToken, forKey:"Access")
                UserDefaults.standard.set(data.refreshToken, forKey:"Refresh")
                self.moveMainController()
            case .failure(let error):
                print("checkRefreshToken error! \(error)")
                self.moveLoginController()
            }
        }
    }
    
    // MARK: Check User Access Token
    func checkValidUser(accessToken: String, refreshToken: String) -> Observable<Void>{
        let url = APIURL.apiUrl.getMembersInfo(ip: APIURL.ip)
        
        let accessToken = UserDefaults.standard.string(forKey: "Access") ?? ""
        
        return Observable.create { observer in
            AF.request(url,
                       headers: ["Authorization": "Bearer \(accessToken)"])
            .validate(statusCode: 200..<201)
            .responseDecodable(of: MainModel.self ){ response in
                switch response.result{
                case .success(let data):
                    if data.id != ""{
                        self.mainService.updateProfile()
                        observer.onNext(())
                    }
                    else{
                        self.checkRefreshToken(refreshToken: refreshToken)
                    }
                case .failure(let error):
                    self.checkRefreshToken(refreshToken: refreshToken)
                    print("checkValidUser error! \(error)")
                }
            }
            
            return Disposables.create()
        }
        
    }
    
    /// MARK: 클립까지 인증했는지 확인
    private func checkingLoginKlip() -> Observable<Bool>{
        let url = APIURL.apiUrl.getMembersInfo(ip: APIURL.ip)
        
        let accessToken = UserDefaults.standard.string(forKey: "Access") ?? ""
        
        return Observable.create { observer in
            AF.request(url,
                       headers: ["Authorization": "Bearer \(accessToken)"])
            .validate(statusCode: 200..<201)
            .responseDecodable(of: Klipchecking.self ){ response in
                switch response.result{
                case .success(let data):
                    observer.onNext(data.is_login_user ?? false)
                case .failure(let error):
                    print("checkingLoginKlip error!\n\(error)")
                    self.moveLoginController()
                }
            }
            return Disposables.create()
        }
        
    }
    
    
    func changeRootViewController(_ vc: UIViewController, animated: Bool = true) {
        guard let window = self.window else { return }
        window.rootViewController = vc
    }
    
}
