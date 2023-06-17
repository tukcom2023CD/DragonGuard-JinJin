//
//  OrganizationDetailController.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

// MARK: 내 조직을 눌렀을 때 내부 레포지토리들 보여주는 화면
final class OrganizationDetailController: UIViewController{
    
    var data: RepositoryListInOrganizationModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        getData()
        
    }
    
    // MARK:
    private lazy var repoLabel: UILabel = {
        let label = UILabel()
        label.text = "Repository"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 30)
        return label
    }()
    
    // MARK: Oraganization Repository 보여줄 TableView
    private lazy var tableView: UITableView = {
        let table = UITableView()
        table.separatorStyle = .none
        return table
    }()
    
    // MARK:
    private func addUI(){
        view.addSubview(repoLabel)
        view.addSubview(tableView)
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.register(OrganizationDetailTableViewCell.self, forCellReuseIdentifier: OrganizationDetailTableViewCell.identifier)
            
        
        repoLabel.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(30)
        }
        
        tableView.snp.makeConstraints { make in
            make.top.equalTo(repoLabel.snp.bottom).offset(15)
            make.leading.trailing.equalTo(view.safeAreaLayoutGuide)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        }
        
    }
    
    // MARK:
    private func getData(){
        
        data = RepositoryListInOrganizationModel(gitRepos: ["aa","bb","cccccccc","ddddddddddddd","e","fffff"],
                                                 imgPath: "")
        addUI()
    }
    
    
}

extension OrganizationDetailController: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: OrganizationDetailTableViewCell.identifier, for: indexPath) as? OrganizationDetailTableViewCell else {return UITableViewCell()}
        
        cell.inputData(title: data?.gitRepos?[indexPath.row] ?? "", imgPath: data?.imgPath ?? "", organizationTitle: "ab")
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return view.safeAreaLayoutGuide.layoutFrame.height/8
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return data?.gitRepos?.count ?? 0 }
}
