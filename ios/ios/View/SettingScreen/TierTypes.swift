//
//  TierTypes.swift
//  ios
//
//  Created by 정호진 on 2023/01/30.
//

import Foundation
import UIKit

final class TierTypes: UIViewController{
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .orange
        self.navigationItem.title = "Tier 종류"
        self.navigationController?.interactivePopGestureRecognizer?.isEnabled = true
    }
}
