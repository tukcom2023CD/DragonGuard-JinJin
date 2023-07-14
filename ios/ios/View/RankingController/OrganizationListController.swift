//
//  OrganizationListController.swift
//  ios
//
//  Created by 정호진 on 2023/07/15.
//

import Foundation
import UIKit
import RxSwift
import SnapKit

final class OrganizationListController: UIViewController{
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
    }
    
    /// MARK:
    private lazy var tableView: UITableView = {
        let table = UITableView()
        
        return table
    }()
    
    
}
