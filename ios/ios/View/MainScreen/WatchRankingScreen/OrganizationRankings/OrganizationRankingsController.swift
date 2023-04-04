//
//  OrganizationRankingsController.swift
//  ios
//
//  Created by 정호진 on 2023/04/05.
//

import Foundation
import SnapKit
import UIKit

// MARK: 조직 관련 랭킹들 버튼 클릭 시
final class OrganizationRankingsController: UIViewController{
    private let rankingBtns = ["조직 내 나의 랭킹", "조직 전체 랭킹", "대학교 랭킹", "회사 랭킹", "그외 조직 랭킹"]
    var myOrganization: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        addUIToView()
    }
    
    /*
     UI 코드 작성
     */
    
    private lazy var listTableView: UITableView = {
       let tableview = UITableView()
        tableview.backgroundColor = .white
        return tableview
    }()
    
    /*
     UI 추가 && AutoLayout
     */
    
    private func addUIToView(){
        self.view.addSubview(listTableView)
        listTableView.dataSource = self
        listTableView.delegate = self
        setAutoLayout()
    }
    
    
    private func setAutoLayout(){
        listTableView.snp.makeConstraints { make in
            make.top.bottom.equalTo(view.safeAreaLayoutGuide)
            make.leading.equalTo(20)
            make.trailing.equalTo(-20)
        }
    }
}

extension OrganizationRankingsController: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: WatchRankingCollectionView.identifier, for: indexPath) as? WatchRankingCollectionView ?? WatchRankingCollectionView()
        
        cell.customLabel.text = rankingBtns[indexPath.section]
        cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)
        cell.layer.cornerRadius = 20    //테두리 둥글게
        cell.layer.borderWidth = 1
        return cell
    }
    
    // cell 선택되었을 때
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        switch indexPath.section{
        case 0:
            self.navigationController?.pushViewController(OrganizationInRankingController(), animated: true)
        case 1:
            let allOrganiRankingController = AllOrganiRankingController()
            allOrganiRankingController.myOrganization = self.myOrganization
            self.navigationController?.pushViewController(allOrganiRankingController, animated: true)
        case 2:
            let universityRankingController = UniversityRankingController()
            universityRankingController.myOrganization = self.myOrganization
            self.navigationController?.pushViewController(universityRankingController, animated: true)
        case 3:
            let companyRankingController = CompanyRankingController()
            companyRankingController.myOrganization = self.myOrganization
            self.navigationController?.pushViewController(companyRankingController, animated: true)
        case 4:
            let eTCRankingController = ETCRankingController()
            eTCRankingController.myOrganization = self.myOrganization
            self.navigationController?.pushViewController(eTCRankingController, animated: true)
        default:
            return
        }
     
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat { return tableView.bounds.height / 10 }
    
    // 섹션 당 셀 개수
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    
    // 색션에 들어갈 문구
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " " }
    
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat { return 2 }
    
    // 섹션 개수
    func numberOfSections(in tableView: UITableView) -> Int { return rankingBtns.count }
    
}
