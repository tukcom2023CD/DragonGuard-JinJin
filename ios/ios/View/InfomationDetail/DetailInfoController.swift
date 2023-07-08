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
    private let disposeBag = DisposeBag()
    private let viewModel = DetailInfoViewModel()
    
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
        table.backgroundColor = .white
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
        
        clickedSettingBtn()
    }
    
    // MARK:
    private func clickedSettingBtn(){
        settingBtn.rx.tap.subscribe(onNext: {
            let settingPage = SettingController()
            settingPage.modalPresentationStyle = .fullScreen
            self.present(settingPage, animated: true)
        })
        .disposed(by: disposeBag)
    }
    
    func getData() {
        viewModel.service.getData()
            .subscribe(onNext: { data in
                self.dataList = data
                self.tableView.reloadData()
            })
            .disposed(by: disposeBag)
        
        addUI()
    }
    
}

extension DetailInfoController: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: DetailInfoTableViewCell.identifier, for: indexPath) as? DetailInfoTableViewCell else {return UITableViewCell() }
        cell.backgroundColor = .clear
        if indexPath.section == 0{
            cell.inputData_Organizaion(data: dataList?.git_organizations?[indexPath.row])
        }
        else if indexPath.section == 1{
            cell.inputData_Repository(title: dataList?.git_repos?[indexPath.row] ?? "",
                                      imgPath: dataList?.member_profile_image ?? "",
                                      myId: "")
        }
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if section == 0{
            return self.dataList?.git_organizations?.count ?? 0
        }
        else{
            return self.dataList?.git_repos?.count ?? 0
        }
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        /// Organization
        if indexPath.section == 0{
            let nextPage = OrganizationDetailController()
            nextPage.name = dataList?.git_organizations?[indexPath.row].name
            nextPage.modalPresentationStyle = .fullScreen
            self.present(nextPage, animated: true)
        }
        else{   /// Repository
            let nextPage = RepoDetailController()
            nextPage.modalPresentationStyle = .fullScreen
            nextPage.selectedTitle = dataList?.git_repos?[indexPath.row] ?? ""
            self.present(nextPage,animated: true)
        }
        
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return view.safeAreaLayoutGuide.layoutFrame.height/6
    }
    
    func numberOfSections(in tableView: UITableView) -> Int { return 2 }
    
    func tableView(_ tableView: UITableView, viewForHeaderInSection section: Int) -> UIView? {
        let headerView = UIView()
        headerView.backgroundColor = .white
        let header = ["Organization", "Repository"]
        let label = UILabel(frame: CGRect(x: 15, y: 5, width: tableView.bounds.size.width, height: 20))
        label.text = header[section]
        label.textColor = .gray
        headerView.addSubview(label)

        return headerView
    }

}
