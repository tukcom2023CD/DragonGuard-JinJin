//
//  WatchRankingController.swift
//  ios
//
//  Created by 정호진 on 2023/01/30.
//

import Foundation
import UIKit

final class WatchRankingController: UIViewController{
    
    private let rankingBtns = ["내 Repository 랭킹", "내 Organization 목록", "조직 관련 랭킹들", "전체 랭킹", "전체 Repository랭킹"]
    private let deviceWidth = UIScreen.main.bounds.width
    private let deviceHeight = UIScreen.main.bounds.height
    var myOrganization: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.isHidden = false   // navigation bar 생성
        self.navigationItem.backButtonTitle = "랭킹 보러가기"
        self.view.backgroundColor = .white
        
        addUItoView()
        settingAutoLayout()
        configureCollectionView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = false
    }
    
    /*
     UI 코드 작성
     */
    
    // 버튼들 나열할 collectionView
    lazy var tableView: UITableView = {
        let tv = UITableView()
        
        tv.backgroundColor = .white
        return tv
    }()
    
    
    /*
     UI Action 작성
     */

    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUItoView(){
        self.view.addSubview(tableView)
    }
    
    // collectionView 설정
    private func configureCollectionView(){
        tableView.register(WatchRankingCollectionView.self, forCellReuseIdentifier: WatchRankingCollectionView.identifier)
        tableView.dataSource = self
        tableView.delegate = self
    }
    
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    //AutoLayout 설정
    private func settingAutoLayout(){
        
        tableView.snp.makeConstraints({ make in
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
        
    }
    
    
}

// CollectionView DataSource, Delegate 설정
extension WatchRankingController: UITableViewDelegate, UITableViewDataSource{
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
            self.navigationController?.pushViewController(MyRepositoryRankingController(), animated: true)
        case 1:
            self.navigationController?.pushViewController(MyOraganizationListController(), animated: true)
        case 2:
            let organizationRankingsController = OrganizationRankingsController()
            organizationRankingsController.myOrganization = self.myOrganization
            self.navigationController?.pushViewController(organizationRankingsController, animated: true)
        case 3:
            self.navigationController?.pushViewController(AllRankingController(), animated: true)
        case 4:
            self.navigationController?.pushViewController(AllRepositoryRankingController(), animated: true)
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



/*
 SwiftUI preview 사용 코드      =>      Autolayout 및 UI 배치 확인용
 
 preview 실행이 안되는 경우 단축키
 Command + Option + Enter : preview 그리는 캠버스 띄우기
 Command + Option + p : preview 재실행
 */

import SwiftUI

struct VCPreViewWatchRanking:PreviewProvider {
    static var previews: some View {
        WatchRankingController().toPreview().previewDevice("iPhone 14 Pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}

struct VCPreViewWatchRanking2:PreviewProvider {
    static var previews: some View {
        WatchRankingController().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
