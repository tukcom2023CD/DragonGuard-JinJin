////
////  UniversityRankingController.swift
////  ios
////
////  Created by 정호진 on 2023/04/05.
////
//
//import Foundation
//import UIKit
//import RxSwift
//import SnapKit
//
//// MARK: 대학교 전체 랭킹
//final class UniversityRankingController: UIViewController{
//    private let disposeBag = DisposeBag()
//    private var allRankingList: [AllOrganizationRankingModel] = []
//    var myOrganization: String?
//    
//    override func viewDidLoad() {
//        super.viewDidLoad()
//        self.view.backgroundColor = .white
//        
//        self.navigationController?.navigationBar.isHidden = false
//        self.navigationItem.title = "대학교 랭킹"
//        self.navigationController?.navigationBar.titleTextAttributes = [.foregroundColor: UIColor.black]
//        
//        addUItoView()
//        settingAutoLayout()
//        getData()
//    }
//    
//    /*
//     UI 작성
//     */
//    
//    // MARK: 랭킹 표시할 테이블 뷰
//    lazy var repoTableView: UITableView = {
//        let repoTableView = UITableView()
//        repoTableView.backgroundColor = .white
//        return repoTableView
//    }()
//    
//    /*
//     UI 추가할 때 작성하는 함수
//     */
//    
//    
//    // MARK: View에 적용할 때 사용하는 함수
//    private func addUItoView(){
//        self.view.addSubview(repoTableView)   //tableview 적용
//        
//        /// 결과 출력하는 테이블 뷰 적용
//        /// datasource는 reactive 적용
//        self.repoTableView.delegate = self
//        self.repoTableView.dataSource = self
//        
//        /// tableview 설치
//        self.repoTableView.register(WatchRankingTableView.self, forCellReuseIdentifier: WatchRankingTableView.identifier)
//    }
//    
//    /*
//     UI AutoLayout 코드 작성
//     
//     함수 실행시 private으로 시작할 것 (추천)
//     */
//    
//    // MARK: set AutoLayout
//    private func settingAutoLayout(){
//        repoTableView.snp.makeConstraints({ make in
//            make.top.equalTo(self.view.safeAreaLayoutGuide).offset(10)
//            make.leading.equalTo(20)
//            make.trailing.equalTo(-20)
//            make.bottom.equalTo(self.view.safeAreaLayoutGuide)
//        })
//        
//    }
//
//    /*
//     UI Action
//     */
//    
//    // MARK: 대학교 랭킹 가져오는 함수
//    private func getData(){
//        OrganizationRankingViewModel.viewModel.getTypeRanking(type: "UNIVERSITY")
//            .subscribe (onNext: { rankingList in
//                rankingList.forEach { data in
//                    self.allRankingList.append(data)
//                }
//                self.repoTableView.reloadData()
//            })
//            .disposed(by: self.disposeBag)
//    }
//    
//}
//
//
//extension UniversityRankingController: UITableViewDelegate, UITableViewDataSource {
//    
//    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//        let cell = tableView.dequeueReusableCell(withIdentifier: WatchRankingTableView.identifier,for: indexPath) as! WatchRankingTableView
//        let organizationName = self.allRankingList[indexPath.section].name
//        
//        cell.prepare(rank: indexPath.section + 1,
//                     text: self.allRankingList[indexPath.section].name,
//                     count: self.allRankingList[indexPath.section].tokenSum)
//        
//        cell.layer.cornerRadius = 20
//        cell.layer.borderWidth = 1
//        
//        if organizationName == self.myOrganization ?? ""{
//            cell.backgroundColor = .yellow
//        }
//        else{
//            cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)
//        }
//        
//        return cell
//    }
//    
//    
//    
//    
//    // MARK: tableview cell이 선택된 경우
//    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
//        print("selected \(indexPath.section)")
//        tableView.deselectRow(at: indexPath, animated: true)
//    }
//    
//    // MARK: section 간격 설정
//    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {  return 1 }
//    
//    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
//    
//    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " " }
//    
//    func numberOfSections(in tableView: UITableView) -> Int { return allRankingList.count }
//}
//
