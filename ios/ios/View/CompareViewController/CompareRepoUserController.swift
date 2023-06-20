//
//  CompareViewController.swift
//  ios
//
//  Created by 정호진 on 2023/06/19.
//

import Foundation
import UIKit
import SnapKit
import RxSwift
import Charts
import Lottie

final class CompareRepoUserController: UIViewController{
    private let nameList: [String] = ["forks", "closed issues", "open issues", "stars", "contributers", "deletions average", "languages", "code average"]
    private let selectionList: [String] = ["Repository", "User"]
    private let disposeBag = DisposeBag()
    var repoUserInfo: CompareUserModel = CompareUserModel(firstResult: [], secondResult: [])
    var user1Index: Int?
    var user2Index: Int?
    var lastIndexOfFisrtArray: Int?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        
//        addUIIndicator()
        addUIBase()
    }
    
    // MARK: 로딩 UI
    private lazy var indicatorView: LottieAnimationView = {
        let view = LottieAnimationView(name: "graphLottie")
        view.center = self.view.center
        view.loopMode = .loop
        return view
    }()
    
    
    // MARK: 뒤로가기 버튼
    private lazy var backBtn: UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(named: "backBtn")?.resize(newWidth: 30), for: .normal)
        return btn
    }()
    
    // MARK: Repository User 선택하는 화면
    private lazy var selectionCollectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
        
        return cv
    }()
    
    
    /*
     Respository UI
     */
    
    // MARK: 스크롤 뷰
    private lazy var scrollView: UIScrollView = {
        let scroll = UIScrollView()
        return scroll
    }()
    
    // MARK: contentView
    private lazy var contentView: UIView = {
        let view = UIView()
        
        return view
    }()
    
    // MARK: 첫 번째 레포 정보
    private lazy var leftView: CustomUIView = {
        let view = CustomUIView()
        
        return view
    }()
    
    // MARK: 두 번째 레포 정보
    private lazy var rightView: CustomUIView = {
        let view = CustomUIView()
        
        return view
    }()
    
    // MARK: 세로 줄
    private lazy var lineView: UIView = {
        let view = UIView()
        view.backgroundColor = .black
        return view
    }()
    
    // MARK: repository info
    private lazy var tableView: UITableView = {
        let table = UITableView()
        table.separatorStyle = .none
        table.isScrollEnabled = false
        table.layer.shadowOffset = CGSize(width: 3, height: 3)
        table.layer.cornerRadius = 20
        table.layer.shadowOpacity = 0.5
        table.clipsToBounds = true
        return table
    }()
    
    /*
     User UI
     */
    
    // MARK: 스크롤 뷰
    private lazy var scrollView_User: UIScrollView = {
        let scroll = UIScrollView()
        return scroll
    }()
    
    // MARK: contentView
    private lazy var contentView_User: UIView = {
        let view = UIView()
        
        return view
    }()
    
    // MARK:
    private lazy var leftUserButton: UserUIButton = {
        let btn = UserUIButton()
        btn.backgroundColor = .white
        btn.layer.shadowOffset = CGSize(width: 3, height: 3)
        btn.layer.shadowOpacity = 1
        btn.layer.shadowColor = .init(red: 0/255, green: 0/255, blue: 0/255, alpha: 1)
        btn.layer.cornerRadius = 20
        btn.clipsToBounds = true
        btn.layer.masksToBounds = true
        return btn
    }()
    
    // MARK:
    private lazy var rightUserButton: UserUIButton = {
        let btn = UserUIButton()
        btn.backgroundColor = .white
        btn.layer.shadowOffset = CGSize(width: 3, height: 3)
        btn.layer.shadowOpacity = 1
        btn.layer.shadowColor = .init(red: 100/255, green: 100/255, blue: 100/255, alpha: 1)
        btn.layer.cornerRadius = 20
        btn.layer.masksToBounds = true
        btn.clipsToBounds = true
        return btn
    }()
    
    // MARK:
    private lazy var stack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [leftUserButton, rightUserButton])
        stack.axis = .horizontal
        stack.spacing = 40
        stack.distribution = .fillEqually
        stack.backgroundColor = .clear
        return stack
    }()
    
    // MARK: Commit chart
    private lazy var chartCommit: BarChartView = {
        let chart1 = BarChartView()
        chart1.backgroundColor = .white
        return chart1
    }()
    
    // MARK: Addition Deletion Chart
    private lazy var chartAddDel: BarChartView = {
        let chart1 = BarChartView()
        chart1.backgroundColor = .white
        return chart1
    }()
    
    /*
     Add UI
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
    
    // MARK: CollectionView and BackBtn
    private func addUIBase(){
        view.addSubview(backBtn)
        view.addSubview(selectionCollectionView)
        selectionCollectionView.dataSource = self
        selectionCollectionView.delegate = self
        selectionCollectionView.register(CompareCollectionViewCell.self, forCellWithReuseIdentifier: CompareCollectionViewCell.identfier)
        
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
        }
        
        selectionCollectionView.snp.makeConstraints { make in
            make.top.equalTo(backBtn.snp.bottom).offset(10)
            make.leading.trailing.equalToSuperview()
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/10)
        }
        clickedBtn()
    }
    
    // MARK: Add UI (레포지토리 UI)
    private func addUI(){
        view.addSubview(scrollView)
        scrollView.addSubview(contentView)
        contentView.addSubview(leftView)
        contentView.addSubview(rightView)
        contentView.addSubview(lineView)
        contentView.addSubview(tableView)
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.register(CompareTableViewCell.self, forCellReuseIdentifier: CompareTableViewCell.identfier)
        setAutoLayout()
    }
    
    // MARK: Setting AutoLayout (레포지토리 UI)
    private func setAutoLayout(){
        scrollView.snp.makeConstraints { make in
            make.top.equalTo(selectionCollectionView.snp.bottom).offset(10)
            make.leading.trailing.bottom.equalTo(view.safeAreaLayoutGuide)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        }
        
        contentView.snp.makeConstraints { make in
            make.top.equalTo(scrollView.snp.top)
            make.leading.equalTo(scrollView.snp.leading)
            make.trailing.equalTo(scrollView.snp.trailing)
            make.bottom.equalTo(scrollView.snp.bottom)
            make.width.equalTo(scrollView.snp.width)
        }
        
        leftView.snp.makeConstraints { make in
            make.top.equalTo(contentView.snp.top).offset(10)
            make.leading.equalTo(contentView.snp.leading)
            make.bottom.equalTo(lineView.snp.bottom)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/2-1)
        }
        
        lineView.snp.makeConstraints { make in
            make.top.equalTo(leftView.snp.top).offset(10)
            make.leading.equalTo(leftView.snp.trailing)
            make.width.equalTo(2)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height*3/5)
        }
        
        rightView.snp.makeConstraints { make in
            make.top.equalTo(contentView.snp.top).offset(10)
            make.trailing.equalTo(contentView.snp.trailing)
            make.bottom.equalTo(lineView.snp.bottom)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/2-1)
        }
        
        tableView.snp.makeConstraints { make in
            make.top.equalTo(lineView.snp.bottom).offset(10)
            make.leading.equalTo(contentView.snp.leading).offset(30)
            make.trailing.equalTo(contentView.snp.trailing).offset(-30)
            make.bottom.equalTo(contentView.snp.bottom).offset(-20)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height*8/14)
        }
        
        
    }
    
    // MARK:
    private func addUI_User(){
        view.addSubview(scrollView_User)
        scrollView_User.addSubview(contentView_User)
        contentView_User.addSubview(stack)
        contentView_User.addSubview(chartCommit)
        contentView_User.addSubview(chartAddDel)
        
        scrollView_User.snp.makeConstraints { make in
            make.top.equalTo(selectionCollectionView.snp.bottom).offset(10)
            make.leading.trailing.bottom.equalTo(view.safeAreaLayoutGuide)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        }
        
        contentView_User.snp.makeConstraints { make in
            make.top.equalTo(scrollView_User.snp.top)
            make.leading.equalTo(scrollView_User.snp.leading)
            make.trailing.equalTo(scrollView_User.snp.trailing)
            make.bottom.equalTo(scrollView_User.snp.bottom)
            make.width.equalTo(scrollView_User.snp.width)
        }
        
        stack.snp.makeConstraints { make in
            make.top.equalTo(contentView_User.snp.top)
            make.leading.equalTo(contentView_User.snp.leading).offset(40)
            make.trailing.equalTo(contentView_User.snp.trailing).offset(-40)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/5)
        }
        
        chartCommit.snp.makeConstraints { make in
            make.top.equalTo(stack.snp.bottom).offset(20)
            make.leading.equalTo(contentView_User.snp.leading).offset(10)
            make.trailing.equalTo(contentView_User.snp.trailing).offset(-10)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/2)
        }
        
        chartAddDel.snp.makeConstraints { make in
            make.top.equalTo(chartCommit.snp.bottom).offset(20)
            make.leading.equalTo(contentView_User.snp.leading).offset(10)
            make.trailing.equalTo(contentView_User.snp.trailing).offset(-10)
            make.bottom.equalTo(contentView_User.snp.bottom)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/2)
        }
        
    }
    
    // MARK: get repository Data
    private func getData(){
        addUI()
        
        leftView.inputData(repo1: [], values: nil, repoName: "abc", imgList: [])
        rightView.inputData(repo1: [], values: nil, repoName: "qwer", imgList: [])
    }
    
    // MARK:
    private func getData_User(){
        repoUserInfo.firstResult = [FirstRepoResult(githubId: "aa1", commits: 12, additions: 100, deletions: 100),
                                    FirstRepoResult(githubId: "aa2", commits: 12, additions: 100, deletions: 100),
                                    FirstRepoResult(githubId: "aa3", commits: 12, additions: 100, deletions: 100),
                                    FirstRepoResult(githubId: "aa4", commits: 12, additions: 100, deletions: 100)]
        
        repoUserInfo.secondResult = [SecondRepoResult(githubId: "aa1", commits: 122, additions: 10, deletions: 110),
                                     SecondRepoResult(githubId: "aa2", commits: 12, additions: 100, deletions: 100),
                                     SecondRepoResult(githubId: "aa3", commits: 12, additions: 100, deletions: 100),
                                     SecondRepoResult(githubId: "aa4", commits: 12, additions: 100, deletions: 100)]
        user1Index = 1
        user2Index = 0
        lastIndexOfFisrtArray = repoUserInfo.firstResult.count
        
        addUI_User()
        leftUserButton.inputData(img: UIImage(named: "2")!, name: "ttf")
        rightUserButton.inputData(img: UIImage(named: "2")!, name: "ttr")

        setChartCommit()
        setChartAddDel()
    }
    
    // MARK: Button Actions
    private func clickedBtn(){
        backBtn.rx.tap.subscribe(onNext: {
            self.dismiss(animated: false)
        })
        .disposed(by: disposeBag)
    }
    
    
}

extension CompareRepoUserController: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: CompareTableViewCell.identfier, for: indexPath) as? CompareTableViewCell else { return UITableViewCell() }
        cell.inputData(repo1: 1, title: nameList[indexPath.section], repo2: 2)
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat { return view.safeAreaLayoutGuide.layoutFrame.height/20 }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    func numberOfSections(in tableView: UITableView) -> Int { return nameList.count }
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " " }
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat { return 1 }
}


extension CompareRepoUserController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: CompareCollectionViewCell.identfier, for: indexPath) as? CompareCollectionViewCell else { return UICollectionViewCell() }
        cell.inputData(text: selectionList[indexPath.row])
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if indexPath.row == 0{
            scrollView_User.removeFromSuperview()
            getData()
        }
        else if indexPath.row == 1{
            scrollView.removeFromSuperview()
            getData_User()
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int { return selectionList.count }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize { return CGSize(width: collectionView.frame.width/2, height: collectionView.frame.height) }
    
}


extension CompareRepoUserController : ChartViewDelegate {
    
    /// Commit 차트를 그리는 부분
    /// BarChart 사용
    private func setChartCommit() {
        var dataSet : [BarChartDataSet] = []
        guard let lastIndexOfFisrtArray = self.lastIndexOfFisrtArray else {return}
        guard let font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14) else {return}
        
        // data set 1
        var userInfo = [ChartDataEntry]()
        guard let user1Index = self.user1Index else {return}
        var set1 = BarChartDataSet()
        var repo = ""
        var newUser1Index = 0

        if lastIndexOfFisrtArray > user1Index{  // 첫 번째 유저 선택이 첫번쨰 배열 안에 있는 경우
            newUser1Index = user1Index
            repo = "first"
        }
        else if lastIndexOfFisrtArray <= user1Index{ // 첫 번째 유저 선택이 두 번째 배열 안에 있는 경우
            newUser1Index = user1Index - lastIndexOfFisrtArray
            repo = "second"
        }
        
        if repo == "first"{
            let dataEntry1 = BarChartDataEntry(x: 0, y: Double(self.repoUserInfo.firstResult[newUser1Index].commits))
            userInfo.append(dataEntry1)
            set1 = BarChartDataSet(entries: userInfo, label: self.repoUserInfo.firstResult[newUser1Index].githubId)
        }
        else if repo == "second"{
            let dataEntry1 = BarChartDataEntry(x: 0, y: Double(self.repoUserInfo.secondResult[newUser1Index].commits))
            userInfo.append(dataEntry1)
            set1 = BarChartDataSet(entries: userInfo, label: self.repoUserInfo.secondResult[newUser1Index].githubId)
        }
        set1.valueTextColor = .black
        set1.valueFont = font
        set1.colors = [.red]
        dataSet.append(set1)
        
        
        // data set 2
        guard let user2Index = self.user2Index else {return}
        var newUser2Index = 0
        var set2 = BarChartDataSet()
        var userInfo2 = [ChartDataEntry]()
        repo = ""
        if lastIndexOfFisrtArray > user2Index{  // 두 번째 유저 선택이 첫번째 배열 안에 있는 경우
            newUser2Index = user2Index
            repo = "first"
        }
        else if lastIndexOfFisrtArray <= user2Index{ // 두 번째 유저 선택이 두 번째 배열 안에 있는 경우
            newUser2Index = user2Index - lastIndexOfFisrtArray
            repo = "second"
        }
        
        if repo == "first"{
            let dataEntry2 = BarChartDataEntry(x: 1, y: floor(Double(self.repoUserInfo.firstResult[newUser2Index].commits)))
            userInfo2.append(dataEntry2)
            set2 = BarChartDataSet(entries: userInfo2, label: self.repoUserInfo.firstResult[newUser2Index].githubId)
        }
        else if repo == "second"{
            let dataEntry2 = BarChartDataEntry(x: 1, y: floor(Double(self.repoUserInfo.secondResult[newUser2Index].commits)))
            userInfo2.append(dataEntry2)
            set2 = BarChartDataSet(entries: userInfo2, label: self.repoUserInfo.secondResult[newUser2Index].githubId)
        }
        
        
        set2.valueTextColor = .black
        set2.valueFont = font
        set2.colors = [.blue]
        dataSet.append(set2)
        let data = BarChartData(dataSets: dataSet)
        chartCommit.data = data
        chartCommitAttribute()
    }
    
    
    ///  Commit 차트 속성을 설정하는 부분
    private func chartCommitAttribute(){
        guard let font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15) else {return}
        chartCommit.xAxis.enabled = false
        chartCommit.animate(xAxisDuration: 1, yAxisDuration: 2)
        chartCommit.leftAxis.enabled = true
        chartCommit.leftAxis.labelTextColor = .black
        chartCommit.doubleTapToZoomEnabled = false
        chartCommit.leftAxis.labelFont = font
        chartCommit.noDataText = "출력 데이터가 없습니다."
        chartCommit.noDataFont = .systemFont(ofSize: 30)
        chartCommit.noDataTextColor = .lightGray
        chartCommit.legend.textColor = .black
        chartCommit.legend.font = font
        chartCommit.highlightFullBarEnabled = false
        chartCommit.highlightPerTapEnabled = false
        chartCommit.highlightPerDragEnabled = false
    }
    
    
    ///  Addtion, Deletion 차트를 그리는 부분
    ///  BarChart 사용
    private func setChartAddDel() {
        var dataSet : [BarChartDataSet] = []
        guard let lastIndexOfFisrtArray = self.lastIndexOfFisrtArray else {return}
        guard let font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14) else {return}
        
        // data set 1
        var userInfo = [ChartDataEntry]()
        guard let user1Index = self.user1Index else {return}
        var set1 = BarChartDataSet()
        var repo = ""
        var newUser1Index = 0
        
        if lastIndexOfFisrtArray > user1Index{  // 첫 번째 유저 선택이 첫번쨰 배열 안에 있는 경우
            newUser1Index = user1Index
            repo = "first"
        }
        else if lastIndexOfFisrtArray <= user1Index{ // 첫 번째 유저 선택이 두 번째 배열 안에 있는 경우
            newUser1Index = user1Index - lastIndexOfFisrtArray
            repo = "second"
        }
        
        if repo == "first"{
            let dataEntry1 = BarChartDataEntry(x: 0, y: Double(self.repoUserInfo.firstResult[newUser1Index].additions))
            let dataEntry2 = BarChartDataEntry(x: 2, y: Double(self.repoUserInfo.firstResult[newUser1Index].deletions))
            userInfo.append(dataEntry1)
            userInfo.append(dataEntry2)
            set1 = BarChartDataSet(entries: userInfo, label: self.repoUserInfo.firstResult[newUser1Index].githubId)
        }
        else if repo == "second"{
            let dataEntry1 = BarChartDataEntry(x: 0, y: Double(self.repoUserInfo.secondResult[newUser1Index].additions))
            let dataEntry2 = BarChartDataEntry(x: 2, y: Double(self.repoUserInfo.secondResult[newUser1Index].deletions))
            userInfo.append(dataEntry1)
            userInfo.append(dataEntry2)
            set1 = BarChartDataSet(entries: userInfo, label: self.repoUserInfo.secondResult[newUser1Index].githubId)
        }
        set1.valueTextColor = .black
        set1.valueFont = font
        set1.colors = [.red]
        dataSet.append(set1)
        
        
        // data set 2
        guard let user2Index = self.user2Index else { return }
        var newUser2Index = 0
        var set2 = BarChartDataSet()
        var userInfo2 = [ChartDataEntry]()
        repo = ""
        if lastIndexOfFisrtArray > user2Index{  // 두 번째 유저 선택이 첫번째 배열 안에 있는 경우
            newUser2Index = user2Index
            repo = "first"
        }
        else if lastIndexOfFisrtArray <= user2Index{ // 두 번째 유저 선택이 두 번째 배열 안에 있는 경우
            newUser2Index = user2Index - lastIndexOfFisrtArray
            repo = "second"
        }
        
        if repo == "first"{
            let dataEntry1 = BarChartDataEntry(x: 1, y: Double(self.repoUserInfo.firstResult[newUser2Index].additions))
            let dataEntry2 = BarChartDataEntry(x: 3, y: Double(self.repoUserInfo.firstResult[newUser2Index].deletions))
            userInfo2.append(dataEntry1)
            userInfo2.append(dataEntry2)
            set2 = BarChartDataSet(entries: userInfo2, label: self.repoUserInfo.firstResult[newUser2Index].githubId)
        }
        else if repo == "second"{
            let dataEntry1 = BarChartDataEntry(x: 1, y: Double(self.repoUserInfo.secondResult[newUser2Index].additions))
            let dataEntry2 = BarChartDataEntry(x: 3, y: Double(self.repoUserInfo.secondResult[newUser2Index].deletions))
            userInfo2.append(dataEntry1)
            userInfo2.append(dataEntry2)
            set2 = BarChartDataSet(entries: userInfo2, label: self.repoUserInfo.secondResult[newUser2Index].githubId)
        }
        set2.valueTextColor = .black
        set2.valueFont = font
        set2.colors = [.blue]
        dataSet.append(set2)
        
        
        
        let data = BarChartData(dataSets: dataSet)
        
        chartAddDel.data = data
        
        chartAddDelAttribute()
    }
    
    /// Addtion Deletion 그리는 차트 속성
    private func chartAddDelAttribute(){
        guard let font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15) else {return}
        
        chartAddDel.xAxis.enabled = false
        chartAddDel.animate(xAxisDuration: 1, yAxisDuration: 2)
        chartAddDel.leftAxis.enabled = true
        chartAddDel.leftAxis.labelTextColor = .black
        chartAddDel.doubleTapToZoomEnabled = false
        chartAddDel.leftAxis.labelFont = font
        chartAddDel.noDataText = "출력 데이터가 없습니다."
        chartAddDel.noDataFont = .systemFont(ofSize: 30)
        chartAddDel.noDataTextColor = .lightGray
        chartAddDel.legend.textColor = .black
        chartAddDel.legend.font = font
        chartAddDel.highlightFullBarEnabled = false
        chartAddDel.highlightPerTapEnabled = false
        chartAddDel.highlightPerDragEnabled = false
    }
}
