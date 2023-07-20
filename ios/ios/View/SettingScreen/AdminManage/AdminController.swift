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
        
        addUIToView()
        clickedBackBtn()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        getData()
    }
    
    // MARK: 뒤로가기 버튼
    private lazy var backBtn: UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(named: "backBtn")?.resize(newWidth: 30), for: .normal)
        return btn
    }()
    
    // MARK: 요청된 리스트 표현할 tableview
    private lazy var requestTableView: UITableView = {
        let tableView = UITableView()
        tableView.backgroundColor = .white
        
        return tableView
    }()
    
    // MARK: Add UI
    private func addUIToView(){
        view.addSubview(backBtn)
        view.addSubview(requestTableView)
        requestTableView.register(AdminTableViewCell.self, forCellReuseIdentifier: AdminTableViewCell.identifier)
        requestTableView.dataSource = self
        requestTableView.delegate = self
        
        setAutoLayout()
    }
    
    // MARK: set AutoLayout
    private func setAutoLayout(){
        
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(15)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
        }
        
        requestTableView.snp.makeConstraints { make in
            make.top.equalTo(backBtn.snp.bottom)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(30)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-30)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        }
        
    }
    
    // MARK: 요청된 조직 리스트 가져오는 함수
    private func getData(){
        AdminViewModel.admin.getOrganizationList(status: "REQUESTED")
            .subscribe(onNext: { data in
                self.requestList = data
                self.requestTableView.reloadData()
            })
            .disposed(by: self.disposeBag)
    }
    
    // MARK:
    private func clickedBackBtn(){
        backBtn.rx.tap.subscribe(onNext: {
            self.dismiss(animated: true)
        })
        .disposed(by: disposeBag)
    }
    
}

extension AdminController: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: AdminTableViewCell.identifier,for: indexPath) as! AdminTableViewCell
        
        cell.inputText(text: requestList[indexPath.section].name ?? "")
        cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
        cell.layer.cornerRadius = 20
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        // 팝업창 띄움
        let sheet = UIAlertController(title: "승인", message: "\(self.requestList[indexPath.section].name ?? "")을 승인하시겠습니까?\nEmail Endpoint : \(self.requestList[indexPath.section].email_endpoint ?? "")", preferredStyle: .alert)
        // 팝업창 확인 버튼
        let success = UIAlertAction(title: "승인", style: .default){ action in
            AdminViewModel.admin.updateOrganizationList(id: self.requestList[indexPath.section].id ?? 0,
                                                        decide: "ACCEPTED")
            .subscribe { data in
                self.requestList = []
                self.requestList = data
                tableView.reloadData()
            }
            .disposed(by: self.disposeBag)
        }
        
        let delete = UIAlertAction(title: "반려", style: .default){ action in
            AdminViewModel.admin.updateOrganizationList(id: self.requestList[indexPath.section].id ?? 0,
                                                        decide: "DENIED")
            .subscribe { data in
                self.requestList = []
                self.requestList = data
                tableView.reloadData()
            }
            .disposed(by: self.disposeBag)
        }
        
        sheet.addAction(success)
        sheet.addAction(delete)
        sheet.addAction(UIAlertAction(title: "취소", style: .default))
        // 화면에 표시
        self.present(sheet,animated: true)
    }
    
    func numberOfSections(in tableView: UITableView) -> Int { return requestList.count }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " "}
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat { return 1 }
    
}
