//
//  AdminTabbarController.swift
//  ios
//
//  Created by 정호진 on 2023/05/05.
//

import Foundation
import UIKit

// MARK: 관리자 화면 Tab bar
final class AdminTabbarController: UITabBarController{
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tabBar.tintColor = .black
        let firstView = AdminController()
        firstView.tabBarItem.image = UIImage(systemName: "checklist.unchecked")
        let secondView = AcceptedOraganicationController()
        secondView.tabBarItem.image = UIImage(systemName: "checklist.checked")
        let viewController = [firstView, secondView]
        self.setViewControllers(viewController, animated: true)
    }
    
}
