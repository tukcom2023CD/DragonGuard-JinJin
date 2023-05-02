//
//  RepositoryListView.swift
//  ios
//
//  Created by 정호진 on 2023/04/14.
//

import Foundation
import UIKit
import SnapKit
//
//// MARK: 레포지토리 tableview 넣을 view
//final class RepositoryListView: UIView {
//    private var repoList: [String] = []
//
//    override init(frame: CGRect) {
//        super.init(frame: frame)
//        addUIToView()
//    }
//
//    required init?(coder: NSCoder) {
//        super.init(coder: coder)
//    }
//
//    /*
//     UI
//     */
//    
//    private lazy var repoTableView: UITableView = {
//        let tableview = UITableView()
//        tableview.backgroundColor = .white
//        return tableview
//    }()
//
//    /*
//     Add UI & AutoLayout
//     */
//
//    // MARK: Add UI To view
//    private func addUIToView(){
//        self.addSubview(repoTableView)
//        repoTableView.dataSource = self
//        repoTableView.delegate = self
//        repoTableView.register(RepoTableViewCell.self, forCellReuseIdentifier: RepoTableViewCell.identifier)
//
//    }
//
//}
//
//
//// MARK: 소유한 레포지토리 리스트 출력하는 tableview
//extension UserProfileController: UITableViewDelegate, UITableViewDataSource{
//
//    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//        let cell = tableView.dequeueReusableCell(withIdentifier: RepoTableViewCell.identifier,for: indexPath) as! RepoTableViewCell
//
//        return cell
//    }
//
//    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return repoList.count }
//}
