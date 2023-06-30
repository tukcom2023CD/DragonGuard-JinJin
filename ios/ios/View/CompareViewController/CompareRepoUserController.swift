//
//  CompareRepoUserController.swift
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
    private let repositoryInfoTitleList: [String] = ["forks", "closed issues", "open issues", "stars", "contributers", "additions average", "deletions average", "languages", "code average"]
    private let selectionTitleList: [String] = ["Repository", "User"]
    private let disposeBag = DisposeBag()
    private var repoInfo: CompareRepoModel?
    private var allUserList: [AllMemberInfoModel] = []
    private var user1Index: Int?
    private var user2Index: Int?
    private var checkUserData: Bool = false
    private var checkRepoData: Bool = false
    var firstRepo: String?
    var secondRepo: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        
        addUIBase()
        addUIIndicator()
        checkChooseTwoUser()
        getData_User()
        
    }
    
    // MARK: 로딩 UI
    private lazy var indicatorView: LottieAnimationView = {
        let view = LottieAnimationView(name: "graphLottie")
        view.backgroundColor = .white
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
        cv.isScrollEnabled = true
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
        btn.setTitleColor(.black, for: .normal)
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
        btn.setTitleColor(.black, for: .normal)
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
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(40)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-40)
            make.top.equalTo(backBtn.snp.bottom)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
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
        chartCommit.delegate = self
        chartAddDel.delegate = self
        
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
    private func getData_Repo(){
        
        CompareViewModel.viewModel.getRepositoryInfo(firstRepo: self.firstRepo ?? "", secondRepo: self.secondRepo ?? "")
            .subscribe(onNext:{ list in
                self.repoInfo = list
                print("getData_Repo success\n")
                self.checkRepoData = true
                
                let firstIndex = IndexPath(item: 0, section: 0)
                self.selectionCollectionView.selectItem(at: firstIndex, animated: false, scrollPosition: .init())
                self.collectionView(self.selectionCollectionView, didSelectItemAt: firstIndex)
                
                self.indicatorView.removeFromSuperview()
                self.leftView.sendingImgData(imgList: list.first_repo.profile_urls)
                self.rightView.sendingImgData(imgList: list.first_repo.profile_urls)
                self.leftView.inputData(repo1: list.first_repo.languages.count,
                                        values: list.first_repo.languages.language,
                                        repoName: list.first_repo.git_repo.full_name,
                                        imgList: list.first_repo.profile_urls)
                self.rightView.inputData(repo1: list.second_repo.languages.count,
                                        values: list.second_repo.languages.language,
                                        repoName: list.second_repo.git_repo.full_name,
                                        imgList: list.second_repo.profile_urls)
                
                self.tableView.reloadData()
            })
            .disposed(by: disposeBag)
        
    }
    
    // MARK:
    private func getData_User(){
        
        CompareViewModel.viewModel.getContributorInfo(firstRepoName: self.firstRepo ?? "", secondRepoName: self.secondRepo ?? "")
            .subscribe(onNext:{ list in
                self.getData_Repo()
                print("getData_User success\n")
                
                list.first_result.forEach { data in
                    self.allUserList.append(AllMemberInfoModel(github_id: data.github_id ?? "Unknown",
                                                               profile_url: data.profile_url ?? "",
                                                               commits: data.commits ?? 0,
                                                               additions: data.additions ?? 0,
                                                               deletions: data.deletions ?? 0,
                                                               is_service_member: data.is_service_member ?? false))
                }
                
                list.second_result.forEach { data in
                    self.allUserList.append(AllMemberInfoModel(github_id: data.github_id ?? "Unknown",
                                                               profile_url: data.profile_url ?? "",
                                                               commits: data.commits ?? 0,
                                                               additions: data.additions ?? 0,
                                                               deletions: data.deletions ?? 0,
                                                               is_service_member: data.is_service_member ?? false))
                }
                self.checkUserData = true
                self.selectionCollectionView.reloadData()
                self.clickedChooseUserBtn()
            })
            .disposed(by: disposeBag)
        
    }
    
    // MARK: 유저 2명을 선택했는지 확인하는 함수
    private func checkChooseTwoUser(){
        let user1 = CompareViewModel.viewModel.checkChooseUser1
        let user2 = CompareViewModel.viewModel.checkChooseUser2

        Observable.combineLatest(user1, user2)
            .subscribe(onNext: { first, second in
                if first && second{
                    self.setChartCommit()
                    self.setChartAddDel()
                }
            })
            .disposed(by: disposeBag)
        
    }
    
    // MARK: Button Actions
    private func clickedBtn(){
        backBtn.rx.tap.subscribe(onNext: {
            self.dismiss(animated: false)
        })
        .disposed(by: disposeBag)
        
    }
    
    // MARK:
    private func clickedChooseUserBtn(){
        leftUserButton.rx.tap.subscribe(onNext: {
            let nextPage = ChooseUserViewController()
            nextPage.beforeUser = "firstUser"
            nextPage.delegate = self
            nextPage.userList = self.allUserList
            nextPage.modalPresentationStyle = .formSheet
            self.present(nextPage,animated: true)
        })
        .disposed(by: disposeBag)
        
        rightUserButton.rx.tap.subscribe(onNext: {
            let nextPage = ChooseUserViewController()
            nextPage.beforeUser = "secondUser"
            nextPage.delegate = self
            nextPage.userList = self.allUserList
            nextPage.modalPresentationStyle = .formSheet
            self.present(nextPage, animated: true)
        })
        .disposed(by: disposeBag)
    }
 
    
}

// MARK: 선택한 유저 받아오는 부분
extension CompareRepoUserController: SendUser{
    func sendUser(choseRepo: String, index: Int) {
        if choseRepo == "firstUser"{
            self.user1Index = index
            CompareViewModel.viewModel.checkChooseUser1.onNext(true)
            self.leftUserButton.inputData(imgPath: self.allUserList[index].profile_url ?? "",
                                          name: self.allUserList[index].github_id ?? "")
        }
        else if choseRepo == "secondUser"{
            self.user2Index = index
            CompareViewModel.viewModel.checkChooseUser2.onNext(true)
            self.rightUserButton.inputData(imgPath: self.allUserList[index].profile_url ?? "",
                                          name: self.allUserList[index].github_id ?? "")
        }
    }
}


// MARK: Repository 정보 들
extension CompareRepoUserController: UITableViewDelegate, UITableViewDataSource{
    /*
     ["forks", "closed issues", "open issues", "stars", "contributers", "additions average", "deletions average", "languages", "code average"]
     */
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: CompareTableViewCell.identfier, for: indexPath) as? CompareTableViewCell else { return UITableViewCell() }
        
        switch indexPath.section{
        case 0:
            cell.inputData(repo1: self.repoInfo?.first_repo.git_repo.forks_count ?? 0,
                           title: repositoryInfoTitleList[indexPath.section],
                           repo2: self.repoInfo?.second_repo.git_repo.forks_count ?? 0)
        case 1:
            cell.inputData(repo1: self.repoInfo?.first_repo.git_repo.closed_issues_count ?? 0,
                           title: repositoryInfoTitleList[indexPath.section],
                           repo2: self.repoInfo?.second_repo.git_repo.closed_issues_count ?? 0)
        case 2:
            cell.inputData(repo1: self.repoInfo?.first_repo.git_repo.open_issues_count ?? 0,
                           title: repositoryInfoTitleList[indexPath.section],
                           repo2: self.repoInfo?.second_repo.git_repo.open_issues_count ?? 0)
        case 3:
            cell.inputData(repo1: self.repoInfo?.first_repo.git_repo.stargazers_count ?? 0,
                           title: repositoryInfoTitleList[indexPath.section],
                           repo2: self.repoInfo?.second_repo.git_repo.stargazers_count ?? 0)
        case 4:
            cell.inputData(repo1: self.repoInfo?.first_repo.git_repo.subscribers_count ?? 0,
                           title: repositoryInfoTitleList[indexPath.section],
                           repo2: self.repoInfo?.second_repo.git_repo.subscribers_count ?? 0)
        case 5:
            cell.inputData(repo1: Int(self.repoInfo?.first_repo.statistics.addition_stats.average ?? 0),
                           title: repositoryInfoTitleList[indexPath.section],
                           repo2: Int(self.repoInfo?.second_repo.statistics.addition_stats.average ?? 0))
        case 6:
            cell.inputData(repo1: Int(self.repoInfo?.first_repo.statistics.deletion_stats.average ?? 0),
                           title: repositoryInfoTitleList[indexPath.section],
                           repo2: Int(self.repoInfo?.second_repo.statistics.deletion_stats.average ?? 0))
        case 7:
            cell.inputData(repo1: self.repoInfo?.first_repo.languages.language.count ?? 0,
                           title: repositoryInfoTitleList[indexPath.section],
                           repo2: self.repoInfo?.second_repo.languages.language.count ?? 0)
        case 8:
            cell.inputData(repo1: Int(self.repoInfo?.first_repo.languages_stats.average ?? 0),
                           title: repositoryInfoTitleList[indexPath.section],
                           repo2: Int(self.repoInfo?.second_repo.languages_stats.average ?? 0))
        default:
            print("잘못된 접근! \n")
        }
        
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat { return view.safeAreaLayoutGuide.layoutFrame.height/20 }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    func numberOfSections(in tableView: UITableView) -> Int { return repositoryInfoTitleList.count }
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " " }
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat { return 1 }
}

// MARK: REPOSITORY, USER 선택
extension CompareRepoUserController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: CompareCollectionViewCell.identfier, for: indexPath) as? CompareCollectionViewCell else { return UICollectionViewCell() }
        cell.inputData(text: selectionTitleList[indexPath.row])
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        indicatorView.removeFromSuperview()
    
        if indexPath.row == 0{
            scrollView_User.removeFromSuperview()
            if checkRepoData{
                addUI()
            }
        }
        else if indexPath.row == 1{
            scrollView.removeFromSuperview()
            if checkUserData{
                self.addUI_User()
            }
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int { return selectionTitleList.count }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize { return CGSize(width: collectionView.frame.width/2, height: collectionView.frame.height) }
    
}


extension CompareRepoUserController : ChartViewDelegate {

    /// Commit 차트를 그리는 부분
    /// BarChart 사용
    private func setChartCommit() {
        var dataSet : [BarChartDataSet] = []
        guard let font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14) else {return}
        
        /// data set 1
        var userInfo = [ChartDataEntry]()
        var set1 = BarChartDataSet()
        
        if let user1Index = user1Index{
            let dataEntry1 = BarChartDataEntry(x: 0, y: Double(allUserList[user1Index].commits ?? 0))
            userInfo.append(dataEntry1)
            set1 = BarChartDataSet(entries: userInfo, label: allUserList[user1Index].github_id ?? "Unknown")
        }
        set1.valueTextColor = .black
        set1.valueFont = font
        set1.colors = [.red]
        dataSet.append(set1)
        
        /// data set 2
        var set2 = BarChartDataSet()
        var userInfo2 = [ChartDataEntry]()
        
        if let user2Index = user2Index{
            let dataEntry2 = BarChartDataEntry(x: 1, y: Double(allUserList[user2Index].commits ?? 0))
            userInfo2.append(dataEntry2)
            set2 = BarChartDataSet(entries: userInfo2, label: allUserList[user2Index].github_id ?? "Unknown")
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
        guard let font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14) else {return}

        // data set 1
        var userChartInfo = [ChartDataEntry]()
        var set1 = BarChartDataSet()
        
        if let user1Index = user1Index{
            let dataEntry1 = BarChartDataEntry(x: 0, y: Double(self.allUserList[user1Index].additions ?? 0))
            let dataEntry2 = BarChartDataEntry(x: 2, y: Double(self.allUserList[user1Index].deletions ?? 0))
            userChartInfo.append(dataEntry1)
            userChartInfo.append(dataEntry2)
            set1 = BarChartDataSet(entries: userChartInfo, label: self.allUserList[user1Index].github_id ?? "UnKnown")
        }
        set1.valueTextColor = .black
        set1.valueFont = font
        set1.colors = [.red]
        dataSet.append(set1)


        // data set 2
        var set2 = BarChartDataSet()
        var userChartInfo2 = [ChartDataEntry]()

        if let user2Index = user2Index{
            let dataEntry1 = BarChartDataEntry(x: 1, y: Double(self.allUserList[user2Index].additions ?? 0))
            let dataEntry2 = BarChartDataEntry(x: 3, y: Double(self.allUserList[user2Index].deletions ?? 0))
            userChartInfo2.append(dataEntry1)
            userChartInfo2.append(dataEntry2)
            set2 = BarChartDataSet(entries: userChartInfo2, label: self.allUserList[user2Index].github_id ?? "UnKnown")
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

import SwiftUI
struct VCPreViewCompareRepoUserController:PreviewProvider {
    static var previews: some View {
        CompareRepoUserController().toPreview().previewDevice("iPhone 14 Pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
struct VCPreViewCompareRepoUserController2:PreviewProvider {
    static var previews: some View {
        CompareRepoUserController().toPreview().previewDevice("iPhone 11")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
