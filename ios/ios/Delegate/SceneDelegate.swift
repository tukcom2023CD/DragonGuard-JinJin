//
//  SceneDelegate.swift
//  ios
//
//  Created by 정호진 on 2023/01/03.
//

import UIKit

class SceneDelegate: UIResponder, UIWindowSceneDelegate {

    var window: UIWindow?

    func scene(_ scene: UIScene, willConnectTo session: UISceneSession, options connectionOptions: UIScene.ConnectionOptions) {
        guard let windowScene = (scene as? UIWindowScene) else { return }
        window = UIWindow(windowScene: windowScene)

        let rootView = LoginController()
        let nc = UINavigationController(rootViewController: rootView)
        window?.rootViewController = nc
        window?.makeKeyAndVisible()
 
     
    }

    // github get redirect_uri 통신 후 데이터 받는 함수
    func scene(_ scene: UIScene, openURLContexts URLContexts: Set<UIOpenURLContext>) {
        if let url = URLContexts.first?.url {
            print(url)
//            let code = url.absoluteString.components(separatedBy: "=")[1]
//            LoginViewModel.loginService.getUserOAuthToken(code: code)
        }
    }
    
}

