//
//  RepositoryListUIView.swift
//  ios
//
//  Created by 정호진 on 2023/06/21.
//

import Foundation
import UIKit
import SnapKit

final class RepositoryListUIView: UIView{
    private var repoList: [String] = []
    private var userName: String?
    var delegate: ClickedRepos?
    private var userProfile: String?
    private var heightSize: CGFloat?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK:
    private lazy var tableView: UITableView = {
        let table = UITableView()
        table.separatorStyle = .none
        table.isScrollEnabled = false
        return table
    }()
    
    
    // MARK:
    private func addUI(){
        self.addSubview(tableView)
        tableView.dataSource = self
        tableView.delegate = self
        tableView.register(RepositoryListTableViewCell.self, forCellReuseIdentifier: RepositoryListTableViewCell.identfier)
        
        tableView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(10)
            make.leading.trailing.equalToSuperview()
            make.bottom.equalToSuperview().offset(-10)
        }
    }
    
    func inputData(img: String, userName: String, repoName: [String], height: CGFloat){
        self.userName = userName
        self.userProfile = img
        self.repoList = repoName
        heightSize = height
        
        addUI()
    }
    
}

extension RepositoryListUIView: UITableViewDelegate, UITableViewDataSource{
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: RepositoryListTableViewCell.identfier, for: indexPath) as? RepositoryListTableViewCell else { return UITableViewCell() }
        
        cell.inputData(title: self.userName, imgPath: self.userProfile, repoName: self.repoList[indexPath.row])
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        self.delegate?.clickedRepos(repoName: self.repoList[indexPath.row])
        print("?")
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return (heightSize ?? 0)/8
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return repoList.count }
}

protocol ClickedRepos{
    func clickedRepos(repoName: String)
}
