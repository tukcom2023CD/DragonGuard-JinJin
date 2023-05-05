//
//  AdminController.swift
//  ios
//
//  Created by 정호진 on 2023/05/05.
//

import Foundation
import UIKit
import RxSwift
import SnapKit

// MARK: 요청한 조직 리스트 확인하는 화면
final class AdminController: UIViewController{
    private let disposeBag = DisposeBag()
    private var requestList: [AdminModel] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.navigationItem.title = "요청 리스트"
        self.navigationController?.navigationBar.titleTextAttributes = [NSAttributedString.Key.font: UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)!, .foregroundColor: UIColor.black]
        
        
    }
    
    // MARK: 요청된 리스트 표현할 tableview
    private lazy var requestTableView: UITableView = {
        let tableView = UITableView()
        tableView.backgroundColor = .white
        
        return tableView
    }()
    
    // MARK: Add UI
    private func addUIToView(){
        self.view.addSubview(requestTableView)
        requestTableView.register(AdminTableViewCell.self, forCellReuseIdentifier: AdminTableViewCell.identifier)
        requestTableView.dataSource = self
        requestTableView.delegate = self
        
        setAutoLayout()
    }
    
    // MARK: set AutoLayout
    private func setAutoLayout(){
        requestTableView.snp.makeConstraints { make in
            make.top.equalTo(self.view.safeAreaLayoutGuide)
            make.leading.equalTo(self.view.safeAreaLayoutGuide).offset(30)
            make.trailing.equalTo(self.view.safeAreaLayoutGuide).offset(-30)
            make.bottom.equalTo(self.view.safeAreaLayoutGuide)
        }
        
    }
    
}

extension AdminController: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: AdminTableViewCell.identifier,for: indexPath) as! AdminTableViewCell
        
        cell.inputText(text: requestList[indexPath.section].name ?? "")
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        // 팝업창 띄움
        let sheet = UIAlertController(title: "승인", message: "\(self.requestList[indexPath.section])을 승인하시겠습니까?", preferredStyle: .alert)
        // 팝업창 확인 버튼
        let success = UIAlertAction(title: "확인", style: .default){ action in
            
        }
        
        sheet.addAction(success)
        sheet.addAction(UIAlertAction(title: "취소", style: .default))
        // 화면에 표시
        self.present(sheet,animated: true)
    }
    
    func numberOfSections(in tableView: UITableView) -> Int { return requestList.count }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " "}
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat { return 1 }
    
}
