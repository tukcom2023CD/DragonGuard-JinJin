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
    var organizationName: String?
    private var memberList: [AllUserRankingModel] = []
    private let viewModel = OrganizationListViewModel()
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        getData()
        clickBtn()
    }
    
    // MARK: 뒤로가기 버튼
    private lazy var backBtn: UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(named: "backBtn")?.resize(newWidth: 30), for: .normal)
        return btn
    }()
    
    // MARK: 유저 이름
    private lazy var nameLabel: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 25)
        label.textColor = .black
        label.backgroundColor = .clear
        return label
    }()
    
    /// MARK:
    private lazy var tableView: UITableView = {
        let table = UITableView()
        table.backgroundColor = .white
        table.isScrollEnabled = true
        return table
    }()
    
    /// MARK:
    private func addUI(){
        view.addSubview(backBtn)
        view.addSubview(nameLabel)
        view.addSubview(tableView)
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.register(OrganizationListTableViewCell.self, forCellReuseIdentifier: OrganizationListTableViewCell.identfier)
        setAutoLayout()
    }
    
    /// MARK:
    private func setAutoLayout(){
        
        backBtn.snp.makeConstraints { make in
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.top.equalTo(view.safeAreaLayoutGuide).offset(10)
        }
        
        nameLabel.snp.makeConstraints { make in
            make.top.equalTo(backBtn.snp.top)
            make.leading.equalTo(backBtn.snp.trailing).offset(30)
        }
        
        tableView.snp.makeConstraints { make in
            make.top.equalTo(nameLabel.snp.bottom).offset(20)
            make.leading.trailing.bottom.equalTo(view.safeAreaLayoutGuide)
        }
        
    }
    
    // MARK:
    private func clickBtn(){
        backBtn.rx.tap.subscribe(onNext: {
            self.dismiss(animated: true)
        })
        .disposed(by: disposeBag)
    }
    
    /// MARK:
    private func getData(){
        guard let organizationName = organizationName else {return}
        addUI()
        nameLabel.text = organizationName
        viewModel.getMemberList(name: organizationName, check: true)
            .subscribe(onNext: { list in
                self.memberList = list
                self.tableView.reloadData()
            })
            .disposed(by: disposeBag)
    }
    
}

extension OrganizationListController: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: OrganizationListTableViewCell.identfier, for: indexPath) as? OrganizationListTableViewCell else {return UITableViewCell()}
        cell.inputData(rank: (indexPath.row + 1), userData: memberList[indexPath.row])
        cell.backgroundColor = .clear
        cell.selectionStyle = .none
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let nextPage = YourProfileController()
        nextPage.userName = memberList[indexPath.row].github_id
        nextPage.modalPresentationStyle = .fullScreen
        present(nextPage, animated: false)
        
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return memberList.count
    }
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 100
    }
}
