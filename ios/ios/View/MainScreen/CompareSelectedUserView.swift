//
//  CompareSelectedUserView.swift
//  ios
//
//  Created by 정호진 on 2023/02/27.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

final class CompareSelectedUserView: UIViewController{
    var userArray: [String] = []
    let beforePage = CompareUserController()
    var whereComeFrom: String?
    var beforeUser = ""
    var delegate: SendingProtocol?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white

        if let num = whereComeFrom{
            self.beforeUser = num
        }
        addToView()

    }
    
    lazy var tableview: UITableView = {
        let tb = UITableView()
        tb.backgroundColor = .white
        tb.register(CompareSelectedUserTableView.self, forCellReuseIdentifier: CompareSelectedUserTableView.identifier)
        return tb
    }()
    
    private func addToView(){
        self.view.addSubview(tableview)
        self.tableview.delegate = self
        self.tableview.dataSource = self
        setAutoLayout()
    }
    
    private func setAutoLayout(){
        tableview.snp.makeConstraints({ make in
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
    }
    
    
}


extension CompareSelectedUserView: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableview.dequeueReusableCell(withIdentifier: CompareSelectedUserTableView.identifier, for: indexPath) as? CompareSelectedUserTableView ?? CompareSelectedUserTableView()
        cell.setText(text: userArray[indexPath.section])
        cell.backgroundColor = .white
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let beforePage = CompareUserController()
        
        if whereComeFrom == "user1"{
            beforePage.user1Index = indexPath.section
            delegate?.dataSend(index: indexPath.section ,user: "user1")
        }
        else if whereComeFrom == "user2"{
            beforePage.user2Index = indexPath.section
            delegate?.dataSend(index: indexPath.section, user: "user2" )
        }
        
        dismiss(animated: true)
        
    }
    
    // cell 높이 설정
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat { return 60 }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    
    func numberOfSections(in tableView: UITableView) -> Int { return userArray.count }
    
}

protocol SendingProtocol {
    func dataSend(index: Int, user: String)
}
