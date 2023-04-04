//
//  CompanyRankingController.swift
//  ios
//
//  Created by 정호진 on 2023/04/05.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

// MARK: 회사 랭킹
final class CompanyRankingController: UIViewController{
    private let disposeBag = DisposeBag()
    private var allRankingList: [AllOrganizationRankingModel] = []
    var myOrganization: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        self.navigationController?.navigationBar.isHidden = false
        self.navigationItem.title = "회사 랭킹"
        
        addUItoView()
        settingAutoLayout()
        getData()
    }
    
    /*
     UI 작성
     */
    
    // MARK: 랭킹 표시할 테이블 뷰
    lazy var repoTableView: UITableView = {
        let repoTableView = UITableView()
        repoTableView.backgroundColor = .white
        return repoTableView
    }()
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    
    // MARK: View에 적용할 때 사용하는 함수
    private func addUItoView(){
        self.view.addSubview(repoTableView)   //tableview 적용
        
        /// 결과 출력하는 테이블 뷰 적용
        /// datasource는 reactive 적용
        self.repoTableView.delegate = self
        self.repoTableView.dataSource = self
        
        /// tableview 설치
        self.repoTableView.register(WatchRankingTableView.self, forCellReuseIdentifier: WatchRankingTableView.identifier)
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것 (추천)
     */
    
    // MARK: set AutoLayout
    private func settingAutoLayout(){
        repoTableView.snp.makeConstraints({ make in
            make.top.equalTo(self.view.safeAreaLayoutGuide).offset(10)
            make.leading.equalTo(20)
            make.trailing.equalTo(-20)
            make.bottom.equalTo(self.view.safeAreaLayoutGuide)
        })
        
    }

    /*
     UI Action
     */
    
    // MARK: 대학교 랭킹 가져오는 함수
    private func getData(){
        OrganizationRankingViewModel.viewModel.getTypeRanking(type: "COMPANY")
            .subscribe (onNext: { rankingList in
                rankingList.forEach { data in
                    self.allRankingList.append(data)
                }
                self.repoTableView.reloadData()
            })
            .disposed(by: self.disposeBag)
    }
    
}


extension CompanyRankingController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: WatchRankingTableView.identifier,for: indexPath) as! WatchRankingTableView
        let organizationName = self.allRankingList[indexPath.row].name
        
        cell.prepare(rank: indexPath.row + 1,
                     text: organizationName,
                     count: self.allRankingList[indexPath.row].tokenSum)
        
        if organizationName == self.myOrganization ?? ""{
            print(organizationName)
            cell.backgroundColor = .yellow
        }
        
        return cell
    }
    
    
    
    
    // MARK: tableview cell이 선택된 경우
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("selected \(indexPath.section)")
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    // MARK: section 간격 설정
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {  return 1 }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    
}

