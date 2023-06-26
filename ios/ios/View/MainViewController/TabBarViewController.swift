//
//  TabBarViewController.swift
//  ios
//
//  Created by 정호진 on 2023/06/18.
//

import Foundation
import UIKit

final class TabBarViewController: UITabBarController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        
        // Do any additional setup after loading the view.
        let questionVC = QuestionViewController()
        let rankingVC = AllUserRankingController()
        let homeVC = MainViewController()
        let compareVC = CompareChooseRepoViewController()
        let profileVC = DetailInfoController()
        
        
        questionVC.tabBarItem.image = UIImage(named: "question")
        rankingVC.tabBarItem.image = UIImage(named: "ranking")
        homeVC.tabBarItem.image = UIImage(named: "home")
        compareVC.tabBarItem.image = UIImage(named: "compare")
        profileVC.tabBarItem.image = UIImage(named: "profile")
        
        //self.tabBarItem.imageInsets = UIEdgeInsets(top: 6, left: 0, bottom: -6, right: 0);
        self.selectedIndex = 2
        self.tabBar.clipsToBounds = true
        
        setViewControllers([questionVC, rankingVC, homeVC, compareVC, profileVC], animated: false)
    }

    
}
