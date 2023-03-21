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
    let cookieStore = WKWebsiteDataStore.default().httpCookieStore
    var window: UIWindow?
    
    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        guard let windowScene = (scene as? UIWindowScene) else { return }
        window = UIWindow(windowScene: windowScene)
        
        
//        if let accessToken = UserDefaults.standard.string(forKey: "Access"),
//            let refreshToken = UserDefaults.standard.string(forKey: "Refresh"){
//
//            checkValidUser(accessToken: accessToken, refreshToken: refreshToken, complete: moveMainController)
//
//        }
//        else{
            
            let rootView = LoginController()
            rootView.cookieStore = cookieStore
            let nc = UINavigationController(rootViewController: rootView)
            window?.rootViewController = nc
            window?.makeKeyAndVisible()
//        }
        
        
        
    }
    
    // MARK: If success User, go to MainController
    func moveMainController(){
        let rootView = MainController()
        let nc = UINavigationController(rootViewController: rootView)
        window?.rootViewController = nc
        window?.makeKeyAndVisible()
    }
    
    // MARK: Check User Access Token
    func checkValidUser(accessToken: String, refreshToken: String, complete: @escaping () -> () ){
        let url = APIURL.apiUrl.getMembersInfo(ip: APIURL.ip)
        
        AF.request(url,
                   headers: ["Authorization": "Bearer \(accessToken)1"])
        .responseDecodable(of: MainDecodingModel.self ){ response in
                switch response.result{
                case .success(let data):
                    if data.id != ""{
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
//        var header: HTTPHeaders = [:]
//
//        self.cookieStore.getAllCookies { cookies in
//            print("cookies \(cookies)")
//            for cookie in cookies{
//                print("cookes \(cookie)")
//                if cookie.name == "Access" {
//                    print(cookie.name)
//                    header[cookie.name] = cookie.value
//                }
//                if cookie.name == "Refresh" {
//                    header[cookie.name] = cookie.value
//                }
//            }
//        }
        guard let accessToken = UserDefaults.standard.string(forKey: "Access") else {return}
        guard let refreshToken = UserDefaults.standard.string(forKey: "Refresh") else {return}
        
        AF.request(url,
                   headers: ["accessToken" : accessToken, "refreshToken" : refreshToken])
            .responseJSON { response in
                print(response)
            }
        
    }
}

