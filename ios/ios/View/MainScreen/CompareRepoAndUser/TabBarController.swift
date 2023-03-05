//
//  TabBarController.swift
//  ios
//
//  Created by 홍길동 on 2023/02/22.
//

import Foundation
import UIKit

final class TabBarController : UITabBarController {
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tabBar.tintColor = .black
        let firstView = CompareRepositoryController()
        firstView.tabBarItem.image = UIImage(systemName: "book.closed")
        let secondView = CompareUserController()
        secondView.tabBarItem.image = UIImage(systemName: "person.3.fill")
        let viewController = [firstView, secondView]
        self.setViewControllers(viewController, animated: true)
    }
}
