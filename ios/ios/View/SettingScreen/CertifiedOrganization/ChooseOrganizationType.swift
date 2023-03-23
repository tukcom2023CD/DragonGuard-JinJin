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
        addToView()
    }
    
    lazy var typeTableView: UITableView = {
        let tableview = UITableView()
        
        tableview.register(ChooseOrganizationTypeCell.self, forCellReuseIdentifier: ChooseOrganizationTypeCell.identifier)
        return tableview
    }()
    
    private func addToView(){
        self.view.addSubview(self.typeTableView)
        
        self.typeTableView.snp.makeConstraints({ make in
            make.top.bottom.equalTo(view.safeAreaLayoutGuide)
            make.leading.trailing.equalTo(0)
        })
    }
    
}

// MARK: 조직 선택하는 목록 옵션
extension ChooseOrganizationType: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: ChooseOrganizationTypeCell.identifier, for: indexPath) as! ChooseOrganizationTypeCell
        cell.inputText(text: oraganizatinoList[indexPath.row])
        cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
        cell.layer.cornerRadius = 20
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



import SwiftUI

struct VCPreViewChooseOrganizationType:PreviewProvider {
    static var previews: some View {
        ChooseOrganizationType().toPreview().previewDevice("iPhone 14 Pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
struct VCPreViewChooseOrganizationType2:PreviewProvider {
    static var previews: some View {
        ChooseOrganizationType().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
