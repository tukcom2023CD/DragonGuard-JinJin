//
//  SceneDelegate.swift
//  ios
//
//  Created by 정호진 on 2023/01/03.
//

import UIKit
import Alamofire
import Foundation


class SceneDelegate: UIResponder, UIWindowSceneDelegate{
    
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
            let component = URLComponents(string: url.absoluteString)
            let items = component?.queryItems ?? []
            print("request_uri \(url)")
            
            let jwtToken = items[0].value ?? ""
            Environment.jwtToken = jwtToken
                
            let request = try! URLRequest(url: url, method: .head)
            print(request.headers)
//            let session = URLSession.shared
//            print("sessing \(session)")
//            let task = session.dataTask(with: request) { (data, response, error) in
//                print("response \(response)")
//                guard let response = response as? HTTPURLResponse else { return }
//                let headers = response.allHeaderFields
//                print(headers)
//            }
//                task.resume()
            
//            getRefreshToken()
            
            LoginViewModel.loginService.saveJWTToken(token: jwtToken)
        }
    }
    
    func getRefreshToken(){
        let url = APIURL.apiUrl.getRefreshToken(ip: APIURL.ip,accessToken: Environment.jwtToken)
        
        AF.request(url,
                   method: .get)
        .responseJSON { response in
            print(response)
        }
    }
    
}

