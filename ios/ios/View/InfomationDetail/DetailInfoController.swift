//
//  DetailInfoController.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import SnapKit
import RxSwift
import UIKit

final class DetailInfoController: UIViewController{
    private var dataList: DetailInfoModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        getData()
        
    }
    
    /*
     UI Code
     */
    
    // MARK: 설정 화면
    private lazy var settingBtn: UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(systemName: "gearshape.fill")?.resize(newWidth: 20), for: .normal)
        return btn
    }()
    
    // MARK: Oraganization Repository 보여줄 TableView
    private lazy var tableView: UITableView = {
        let table = UITableView()
        table.separatorStyle = .none
        return table
    }()
    
    /*
     Add UI and AutoLayout
     */
    
    // MARK:
    private func addUI(){
        view.addSubview(settingBtn)
        view.addSubview(tableView)
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.register(DetailInfoTableViewCell.self, forCellReuseIdentifier: DetailInfoTableViewCell.identifier)
            
        
        settingBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-10)
        }
        
        tableView.snp.makeConstraints { make in
            make.top.equalTo(settingBtn.snp.bottom).offset(5)
            make.leading.trailing.equalTo(view.safeAreaLayoutGuide)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        }
        
    }
    
    func getData() {
        
        for i in 1...10{
            self.dataList = DetailInfoModel(gitOrganizations: [Organ_InfoModel(imgPath: "s", title: "a\(i)"),
                                                                    Organ_InfoModel(imgPath: "s", title: "aa\(i+1)"),
                                                                    Organ_InfoModel(imgPath: "s", title: "aaa\(i+2)"),
                                                                    Organ_InfoModel(imgPath: "s", title: "aaaa\(i+3)")],
                                                 gitRepos: ["aa\\b\(i)","aa\\bb\(i+1)","aa\\bbb\(i+2)","aa\\bbbb\(i+3)"],
                                                 memberProfileImage: "ss")
        }
        
        addUI()
    }
    
}

extension DetailInfoController: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: DetailInfoTableViewCell.identifier, for: indexPath) as? DetailInfoTableViewCell else {return UITableViewCell() }
        
        if indexPath.section == 0{
            cell.inputData_Organizaion(data: dataList?.gitOrganizations?[indexPath.row])
        }
        else if indexPath.section == 1{
            cell.inputData_Repository(title: dataList?.gitRepos?[indexPath.row] ?? "", imgPath: dataList?.memberProfileImage ?? "" )
        }
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0{
            return self.dataList?.gitOrganizations?.count ?? 0
        }
        else{
            return self.dataList?.gitRepos?.count ?? 0
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        /// Organization
        if indexPath.section == 0{
            let nextPage = OrganizationDetailController()
            nextPage.modalPresentationStyle = .fullScreen
            self.present(nextPage, animated: true)
        }
        else{   /// Repository
            
        }
        
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return view.safeAreaLayoutGuide.layoutFrame.height/8
    }
    func numberOfSections(in tableView: UITableView) -> Int { return 2 }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        let header = ["Organization", "Repository"]
        return header[section]
    }
}
