//
//  ChooseOrganizationType.swift
//  ios
//
//  Created by 정호진 on 2023/03/23.
//

import Foundation
import UIKit

final class ChooseOrganizationType: UIViewController{
    private let oraganizatinoList = ["학교","회사","고등학교","etc"]
    private let typeList = ["UNIVERSITY", "COMPANY","HIGH_SCHOOL","ETC"]
    var delegate: SendType?
    
    override func viewDidLoad() {
        super.viewDidLoad()
    }
    
    lazy var typeTableView: UITableView = {
        let tableview = UITableView()
        
        tableview.register(ChooseOrganizationTypeCell.self, forCellReuseIdentifier: ChooseOrganizationTypeCell.identifier)
        view.addSubview(tableview)
        
        tableview.snp.makeConstraints({ make in
            make.top.bottom.equalTo(view.safeAreaLayoutGuide)
            make.leading.trailing.equalTo(0)
        })
        
        return tableview
    }()
    
}

// MARK: 조직 선택하는 목록 옵션
extension ChooseOrganizationType: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: ChooseOrganizationTypeCell.identifier, for: indexPath) as! ChooseOrganizationTypeCell
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        for index in 0..<self.typeList.count{
            if indexPath.row == index{
                delegate?.sendType(type: self.typeList[index])
            }
        }
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return self.oraganizatinoList.count }
}


protocol SendType{
    func sendType(type: String)
}
