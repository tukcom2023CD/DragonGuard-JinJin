//
//  AllRankingController.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import UIKit
import SnapKit
import RxSwift
import Lottie

// MARK: 전체 사용자 랭킹
final class AllUserRankingController: UIViewController{
    private let allRankingViewModel = ALLUserInfoViewModel()
    private let disposeBag = DisposeBag()
    private let selectionList: [String] = ["사용자 전체", "조직 전체" ,"회사", "대학교", "고등학교", "ETC"]
    private var topTierData: [AllUserRankingModel] = []
    private var userTierData: [AllUserRankingModel] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
//        addUIIndicator()
        collectionViewAdd()
        
    }
    
    /*
     UI code
     */
    
    // MARK: 랭킹 종류 선택하는 collectionview
    private lazy var selectionCollectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
        cv.backgroundColor = .white
        cv.showsHorizontalScrollIndicator = false
        return cv
    }()
    
    // MARK: 스크롤 뷰
    private lazy var scrollView: UIScrollView = {
        let scroll = UIScrollView()
        scroll.backgroundColor = .white
        return scroll
    }()
    
    // MARK:
    private lazy var contentView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        return view
    }()
    
    // MARK: 상위 랭킹 보여주는 뷰
    private lazy var topView: CustomTopView = {
        let view = CustomTopView()
        view.getData(list: self.topTierData)
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 0, height: 3)
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK: 4등 부터 나타낼 tableview
    private lazy var tableView: UITableView = {
        let table = UITableView()
        table.isScrollEnabled = false
        table.separatorStyle = .none
        return table
    }()
    
    // MARK: 로딩 UI
    private lazy var indicatorView: LottieAnimationView = {
        let view = LottieAnimationView(name: "graphLottie")
        view.center = self.view.center
        view.loopMode = .loop
        return view
    }()
    
    /*
     Add UI and AutoLayout
     */
    
    // MARK: Add UI Indicator
    private func addUIIndicator(){
        self.view.addSubview(indicatorView)
        setIndicactorAutoLayout()
        indicatorView.play()
    }
    
    // MARK: Indicator View AutoLayout
    private func setIndicactorAutoLayout(){
        indicatorView.snp.makeConstraints { make in
            make.leading.equalTo(self.view.safeAreaLayoutGuide).offset(40)
            make.trailing.equalTo(self.view.safeAreaLayoutGuide).offset(-40)
            make.centerY.equalToSuperview()
        }
    }
    
    // MARK: CollectionView Add
    private func collectionViewAdd(){
        view.addSubview(selectionCollectionView)
        selectionCollectionView.dataSource = self
        selectionCollectionView.delegate = self
        selectionCollectionView.register(SelectionCollectionViewCell.self, forCellWithReuseIdentifier: SelectionCollectionViewCell.identfier)
        collectionViewAutoLayout()
    }
    
    // MARK: CollectionView AutoLayout
    private func collectionViewAutoLayout(){
        selectionCollectionView.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-10)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/10)
        }
    }
    
    // MARK: 로딩 후 뷰 생성
    private func addUI_AutoLayout_About_Ranking(){
        view.addSubview(scrollView)
        scrollView.addSubview(contentView)
        contentView.addSubview(topView)
        contentView.addSubview(tableView)
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.register(AllUserTableviewCell.self, forCellReuseIdentifier: AllUserTableviewCell.identifier)
        setAutoLayout()
    }
    
    // MARK: AutoLayout After loading
    private func setAutoLayout(){
 
        scrollView.snp.makeConstraints { make in
            make.top.equalTo(selectionCollectionView.snp.bottom)
            make.leading.trailing.equalTo(view.safeAreaLayoutGuide)
            make.bottom.equalToSuperview()
        }
        
        contentView.snp.makeConstraints { make in
            make.top.equalTo(scrollView.snp.top)
            make.leading.equalTo(scrollView.snp.leading)
            make.trailing.equalTo(scrollView.snp.trailing)
            make.bottom.equalTo(scrollView.snp.bottom)
            make.width.equalTo(scrollView.snp.width)
        }
        
        topView.snp.makeConstraints { make in
            make.top.equalTo(contentView.snp.top)
            make.leading.equalTo(contentView.snp.leading)
            make.trailing.equalTo(contentView.snp.trailing)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/3)
        }
        
        tableView.snp.makeConstraints { make in
            make.top.equalTo(topView.snp.bottom).offset(5)
            make.leading.equalTo(contentView.snp.leading)
            make.trailing.equalTo(contentView.snp.trailing)
            make.bottom.equalTo(contentView.snp.bottom)
            let height = view.safeAreaLayoutGuide.layoutFrame.height/6
            make.height.equalTo(Int(height)*(userTierData.count-1)+10)
        }
        
    }
    
    /*
     Get Data
     */
    
    // MARK: 전체 사용자 랭킹 데이터 가져옴
    private func loadAllRankingData(){
        
        allRankingViewModel.getAllRanking()
            .subscribe(onNext:{ list in
                print("?A")
                print(list)
                self.addUI_AutoLayout_About_Ranking()
                
                for i in 0..<list.count{
                    if(i<3){
                        self.topTierData.append(list[i])
                    }
                    else{
                        self.userTierData.append(list[i])
                    }
                }
                
                self.topView.updateData(list: list)
                self.tableView.reloadData()
            })
            .disposed(by: disposeBag)
        
        
    }

    // MARK: 전체 조직 랭킹 데이터 가져옴
    private func loadAllOrganizationData(){
//        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "AllOrganization", num: 34, link: "s",tier: "GOLD"))
//        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo2", num: 2222, link: "s",tier: "GOLD"))
//        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo3", num: 333, link: "s",tier: "BRONZE"))
//
//        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "AllOrganization", num: 333, link: "s",tier: "SILVER"))
//        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo5", num: 33113, link: "s",tier: "SILVER"))
//        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo6", num: 333, link: "s",tier: "GOLD"))
//        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo7", num: 333, link: "s",tier: "BRONZE"))
//        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo8", num: 333, link: "s",tier: "SILVER"))
//        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo9", num: 333, link: "s",tier: "BRONZE"))
//        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo10", num: 333, link: "s",tier: "GOLD"))
//        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo11", num: 333, link: "s",tier: "SILVER"))
//        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo12", num: 333, link: "s",tier: "GOLD"))
//        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo13", num: 333, link: "s",tier: "GOLD"))
//        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo14", num: 333, link: "s",tier: "BRONZE"))
        
        indicatorView.removeFromSuperview()
        addUI_AutoLayout_About_Ranking()
        topView.updateData(list: topTierData)
        tableView.reloadData()
    }

    // MARK: 회사 데이터 가져옴
    private func loadCompanyData(){
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "company", num: 34, link: "s",tier: "GOLD"))
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo2", num: 2222, link: "s",tier: "GOLD"))
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo3", num: 333, link: "s",tier: "BRONZE"))
        
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "company", num: 333, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo5", num: 33113, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo6", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo7", num: 333, link: "s",tier: "BRONZE"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo8", num: 333, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo9", num: 333, link: "s",tier: "BRONZE"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo10", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo11", num: 333, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo12", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo13", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo14", num: 333, link: "s",tier: "BRONZE"))
        
        addUI_AutoLayout_About_Ranking()
        topView.updateData(list: topTierData)
        tableView.reloadData()
    }
    
    // MARK: 대학교 데이터 가져옴
    private func loadUniversityData(){
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "University", num: 34, link: "s",tier: "GOLD"))
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo2", num: 2222, link: "s",tier: "GOLD"))
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo3", num: 333, link: "s",tier: "BRONZE"))
        
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "University", num: 333, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo5", num: 33113, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo6", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo7", num: 333, link: "s",tier: "BRONZE"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo8", num: 333, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo9", num: 333, link: "s",tier: "BRONZE"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo10", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo11", num: 333, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo12", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo13", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo14", num: 333, link: "s",tier: "BRONZE"))
        
        addUI_AutoLayout_About_Ranking()
        topView.updateData(list: topTierData)
        tableView.reloadData()
    }
    
    // MARK: 고등학교 데이터 가져옴
    private func loadHighSchoolData(){
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "HighSchool", num: 34, link: "s",tier: "GOLD"))
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo2", num: 2222, link: "s",tier: "GOLD"))
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo3", num: 333, link: "s",tier: "BRONZE"))
        
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "HighSchool", num: 333, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo5", num: 33113, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo6", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo7", num: 333, link: "s",tier: "BRONZE"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo8", num: 333, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo9", num: 333, link: "s",tier: "BRONZE"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo10", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo11", num: 333, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo12", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo13", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo14", num: 333, link: "s",tier: "BRONZE"))
        
        addUI_AutoLayout_About_Ranking()
        topView.updateData(list: topTierData)
        tableView.reloadData()
    }
    
    // MARK: ETC 데이터 가져옴
    private func loadETCData(){
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "ETC", num: 34, link: "s",tier: "GOLD"))
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo2", num: 2222, link: "s",tier: "GOLD"))
        topTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo3", num: 333, link: "s",tier: "BRONZE"))
        
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "ETC", num: 333, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo5", num: 33113, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo6", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo7", num: 333, link: "s",tier: "BRONZE"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo8", num: 333, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo9", num: 333, link: "s",tier: "BRONZE"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo10", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo11", num: 333, link: "s",tier: "SILVER"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo12", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo13", num: 333, link: "s",tier: "GOLD"))
        userTierData.append(AllUserRankingModel(profileImg: "aa", userName: "heelo14", num: 333, link: "s",tier: "BRONZE"))
        
        addUI_AutoLayout_About_Ranking()
        topView.updateData(list: topTierData)
        tableView.reloadData()
    }
    
}


extension AllUserRankingController: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: AllUserTableviewCell.identifier, for: indexPath) as? AllUserTableviewCell else { return UITableViewCell()}
        cell.inputData(rank: indexPath.row+4, userData: userTierData[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        
        return userTierData.count
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return view.safeAreaLayoutGuide.layoutFrame.height/6
    }
    
}

extension AllUserRankingController: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout{
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: SelectionCollectionViewCell.identfier, for: indexPath) as? SelectionCollectionViewCell else { return UICollectionViewCell() }
        cell.inputData(text: selectionList[indexPath.row])
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
        if indexPath.row == 0{
            scrollView.removeFromSuperview()
            userTierData = []
            topTierData = []
            loadAllRankingData()
        }
        else if indexPath.row == 1{
            scrollView.removeFromSuperview()
            userTierData = []
            topTierData = []
            addUIIndicator()
            loadAllOrganizationData()
        }
        else if indexPath.row == 2{
            scrollView.removeFromSuperview()
            userTierData = []
            topTierData = []
            loadCompanyData()
        }
        else if indexPath.row == 3{
            scrollView.removeFromSuperview()
            userTierData = []
            topTierData = []
            loadUniversityData()
        }
        else if indexPath.row == 4{
            scrollView.removeFromSuperview()
            userTierData = []
            topTierData = []
            loadHighSchoolData()
        }
        else if indexPath.row == 5{
            scrollView.removeFromSuperview()
            userTierData = []
            topTierData = []
            loadETCData()
        }
        
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {

        return CGSize(width: collectionView.frame.width/5, height: collectionView.frame.height)
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int { return selectionList.count }
}
