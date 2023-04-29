//
//  SceneDelegate.swift
//  ios
//
//  Created by 정호진 on 2023/01/03.
//

import UIKit
import Alamofire
import WebKit

class SceneDelegate: UIResponder, UIWindowSceneDelegate{
    var window: UIWindow?
    private let mainService = MainService()
    
    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        guard let windowScene = (scene as? UIWindowScene) else { return }
        window = UIWindow(windowScene: windowScene)
        
        if let accessToken = UserDefaults.standard.string(forKey: "Access"),
            let refreshToken = UserDefaults.standard.string(forKey: "Refresh"){

            checkValidUser(accessToken: accessToken, refreshToken: refreshToken, complete: moveMainController)
        }
        else{
            moveLoginController()
        }
        
    }
    
    // MARK: go to LoginController
    func moveLoginController(){
        let rootView = LoginController()
        let nc = UINavigationController(rootViewController: rootView)
        window?.rootViewController = nc
        window?.makeKeyAndVisible()
    }
    
    // MARK: If success User, go to MainController
    func moveMainController(){
        let rootView = LoginController()
        rootView.autoLoginCheck = true
        let nc = UINavigationController(rootViewController: rootView)
        window?.rootViewController = nc
        window?.makeKeyAndVisible()
    }
    
    // MARK: Check User Access Token
    func checkValidUser(accessToken: String, refreshToken: String, complete: @escaping () -> () ){
        let url = APIURL.apiUrl.getMembersInfo(ip: APIURL.ip)
        
        AF.request(url,
                   headers: ["Authorization": "Bearer \(accessToken)"])
        .responseDecodable(of: MainDecodingModel.self ){ response in
                switch response.result{
                case .success(let data):
                    if data.id != ""{
                        self.mainService.updateProfile()
                        complete()
                    }
                    else{
                        self.checkRefreshToken(refreshToken: refreshToken, complete: self.getNewAccessToken)
                    }
                case .failure(let error):
                    self.checkRefreshToken(refreshToken: refreshToken, complete: self.getNewAccessToken)
                    print("checkValidUser error! \(error)")
                }
            }
    }
    
    // MARK: If success about Refresh Token, get new Access Token
    func getNewAccessToken(){
        
    }
    
    // MARK: Check User Refresh Token
    func checkRefreshToken(refreshToken: String, complete: @escaping () -> ()){
        let url = APIURL.apiUrl.getRefreshToken(ip: APIURL.ip)

        guard let accessToken = UserDefaults.standard.string(forKey: "Access") else {return}
        guard let refreshToken = UserDefaults.standard.string(forKey: "Refresh") else {return}
        
        AF.request(url,
                   headers: ["accessToken" : accessToken, "refreshToken" : refreshToken])
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
}

