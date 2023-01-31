//
//  WatchRankingController.swift
//  ios
//
//  Created by 정호진 on 2023/01/30.
//

import Foundation
import UIKit

final class WatchRankingController: UIViewController{
    
    let rankingBtns = ["내 Repository 랭킹", "내 Organization 목록", "대학교 내부 랭킹", "전국 대학교 랭킹", "전체 랭킹", "전체 Repository랭킹"]
    let deviceWidth = UIScreen.main.bounds.width
    let deviceHeight = UIScreen.main.bounds.height
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.isHidden = true   // navigation bar 생성
        self.navigationItem.backButtonTitle = "랭킹 보러가기"
        self.view.backgroundColor = .white
        
        
        addUItoView()
        settingAutoLayout()
        configureCollectionView()
    }
    
    
    /*
     UI 코드 작성
     */
    
    // 유지 이름 버튼 누르면 설정 화면으로 이동
    lazy var settingUI: UIButton = {
        let settingUI = UIButton()
        settingUI.setTitle("DragonGuard-JinJin", for: .normal)
        settingUI.setTitleColor(.black, for: .normal)
        settingUI.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        settingUI.addTarget(self, action: #selector(settingUIClicked), for: .touchUpInside)
        return settingUI
    }()
    
    // 버튼들 나열할 collectionView
    lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
        cv.backgroundColor = .white
        return cv
    }()
    
    
    /*
     UI Action 작성
     */
    
    // 유저 이름 누르는 경우 네비게이션 뷰 방식으로 이동
    @objc func settingUIClicked(){
        self.navigationController?.pushViewController(SettingController(), animated: true)
    }
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUItoView(){
        self.view.addSubview(settingUI)
        self.view.addSubview(collectionView)
    }
    
    // collectionView 설정
    private func configureCollectionView(){
        collectionView.register(WatchRankingCollectionView.self, forCellWithReuseIdentifier: WatchRankingCollectionView.identifier)
        collectionView.dataSource = self
        collectionView.delegate = self
    }
    
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    //AutoLayout 설정
    private func settingAutoLayout(){
        
        // 사용자 이름 버튼 AutoLayout
        settingUI.snp.makeConstraints({ make in
            make.top.equalTo(50)
            make.leading.equalTo(10)
        })
        
        collectionView.snp.makeConstraints({make in
            make.top.equalTo(settingUI.snp_bottomMargin).offset(20)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(-30)
        })
        
    }
    
    
}

// CollectionView DataSource, Delegate 설정
extension WatchRankingController: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: WatchRankingCollectionView.identifier, for: indexPath) as? WatchRankingCollectionView ?? WatchRankingCollectionView()
        
        cell.btn.setTitle(rankingBtns[indexPath.row], for: .normal)
        cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)
        cell.layer.cornerRadius = 20    //테두리 둥글게
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return rankingBtns.count
    }
    
    // cell 선택되었을 때
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
    }
    
    // collectionview 크기 지정
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let width = collectionView.bounds.width / 2
        let height = collectionView.bounds.height / 3
        
        let size = CGSize(width: width - 5 , height: height - 10)
        return size
    }
    
    
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
        WatchRankingController().toPreview().previewDevice("iPhone 14 pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}

struct VCPreViewWatchRanking2:PreviewProvider {
    static var previews: some View {
        WatchRankingController().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
