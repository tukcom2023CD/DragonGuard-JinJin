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
        
        if let savedData = UserDefaults.standard.object(forKey: "UserDBId") as? Data{
            if let savedObject = try? JSONDecoder().decode(UserDBId.self, from: savedData){
                print("before Root View")
                print(savedObject)
                if savedObject.id != 0 && savedObject.address != ""{
                    let rootView = MainController()
                    let nc = UINavigationController(rootViewController: rootView)
                    window?.rootViewController = nc
                    window?.makeKeyAndVisible()
                }
            }
        }
        else{
            let rootView = KlipLoginController()
            let nc = UINavigationController(rootViewController: rootView)
            window?.rootViewController = nc
            window?.makeKeyAndVisible()
        }
        
 
     
    }


}

