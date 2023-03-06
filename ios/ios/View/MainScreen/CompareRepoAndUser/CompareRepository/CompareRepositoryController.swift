//
//  CompareGraphController.swift
//  ios
//
//  Created by 홍길동 on 2023/02/21.
//

import Foundation
import UIKit
import SnapKit
import Charts
import RxSwift


final class CompareRepositoryController : UIViewController {
    let deviceWidth = UIScreen.main.bounds.width
    let deviceHeight = UIScreen.main.bounds.height
    let disposebag = DisposeBag()
    var nameLabel : [String] = ["forks", "closed\nissues", "open\nissues", "stars", "contributers", "deletions\naverage", "languages", "codes\naverage"]
    var repo1 : [FirstRepoModel] = []
    var repo2 : [secondRepoModel] = []
    var firstRepo: FirstRepoModel?
    var secondRepo: secondRepoModel?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white

        addIndicator()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        getData()
    }
    
    /*
     UI 코드 작성
     */
    
    lazy var repo1Label : UILabel = {
        let label = UILabel()
        label.text = "Repository 1"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        label.textColor = .black
        label.textAlignment = .center
        label.layer.borderWidth = 2
        label.layer.cornerRadius = 20
        return label
    }()
    
    lazy var repo1ColorButton : UIButton = {
        let btn = UIButton()
        btn.backgroundColor = .red
        btn.layer.cornerRadius = deviceWidth/24
        btn.isEnabled = false
        return btn
    }()
    
    lazy var repo2Label : UILabel = {
        let label = UILabel()
        label.text = "Repository 2"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        label.textColor = .black
        label.textAlignment = .center
        label.layer.borderWidth = 2
        label.layer.cornerRadius = 20
        return label
    }()
    
    lazy var repo2ColorButton : UIButton = {
        let btn = UIButton()
        btn.backgroundColor = .blue
        btn.layer.cornerRadius = deviceWidth/24
        btn.isEnabled = false
        return btn
    }()
    
    lazy var repoTableView : UITableView = {
        let repoTable = UITableView()
        repoTable.backgroundColor = .white
        return repoTable
    }()
    
    lazy var chart : RadarChartView = {
        let chart1 = RadarChartView()
        chart1.backgroundColor = .white
        return chart1
    }()
    
    lazy var repoCollectionView : UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
        cv.backgroundColor = .white
        
        return cv
    }()
    
    // 로딩 UI
    lazy var indicator: UIActivityIndicatorView = {
        let indicator = UIActivityIndicatorView()
        indicator.color = .gray
        indicator.isHidden = false
        indicator.startAnimating()
        indicator.style = .large
        
        return indicator
    }()
    
    /*
     UI Action 작성
     */
    
    private func addToView(){
        self.view.addSubview(repo1Label)
        self.view.addSubview(repo1ColorButton)
        self.view.addSubview(repo2Label)
        self.view.addSubview(repo2ColorButton)
        self.view.addSubview(self.repoTableView)
        self.view.addSubview(self.repoCollectionView)
        
        repoTableView.delegate = self
        repoTableView.dataSource = self
        repoCollectionView.delegate = self
        repoCollectionView.dataSource = self
        
        repoCollectionView.register(CompareRepoCollectionViewCell.self, forCellWithReuseIdentifier: CompareRepoCollectionViewCell.identifier)
        repoTableView.register(CompareRepositoryTableViewCell.self, forCellReuseIdentifier: CompareRepositoryTableViewCell.identifier)
        
        setAutoLayout()
    }
    
    // 로딩 UI 추가
    private func addIndicator(){
        self.view.addSubview(indicator)
        setIndicatorAutoLayout()
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func setAutoLayout(){
        repo1Label.snp.makeConstraints ({ make in
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.leading.equalTo(30)
            make.trailing.equalTo(repo1ColorButton.snp.leading).offset(-10)
            make.height.equalTo(deviceWidth/10)
        })
        
        repo1ColorButton.snp.makeConstraints ({ make in
            make.centerY.equalTo(repo1Label)
            make.trailing.equalTo(-30)
            make.width.equalTo(deviceWidth/12)
            make.height.equalTo(deviceWidth/12)
        })
        
        repo2Label.snp.makeConstraints ({ make in
            make.top.equalTo(repo1Label.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.height.equalTo(deviceWidth/10)
        })
        
        repo2ColorButton.snp.makeConstraints ({ make in
            make.centerY.equalTo(repo2Label)
            make.leading.equalTo(repo2Label.snp.trailing).offset(10)
            make.trailing.equalTo(-30)
            make.width.equalTo(deviceWidth/12)
            make.height.equalTo(deviceWidth/12)
        })
 
        repoTableView.snp.makeConstraints({ make in
            make.top.equalTo(repo2ColorButton.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.height.equalTo(deviceHeight/4)
        })
        
        repoCollectionView.snp.makeConstraints( { make in
            make.top.equalTo(repoTableView.snp.bottom).offset(5)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
        
    }
    
    /// 데이터가 로드 되기 전에 로딩화면 AutoLayout
    private func setIndicatorAutoLayout(){
        indicator.snp.makeConstraints({ make in
            make.center.equalToSuperview()
        })
    }
    
//    /// ViewModel을 통해 API 통신 후 데이터를 가져오는 함수
//    func getData(){
//        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true, block: { timer in
//            self.repo1 = []
//            self.repo2 = []
////            self.viewModel.bringRepoInfo()
//            self.viewModel.repo1Info.subscribe(onNext: {
//                self.repo1 = $0
//            }).disposed(by: self.disposebag)
//            self.viewModel.repo2Info.subscribe(onNext: {
//                self.repo2 = $0
//            }).disposed(by: self.disposebag)
//
//            if self.repo1.count != 0 && self.repo2.count != 0 {
//                timer.invalidate()
//
//                self.indicator.stopAnimating()
//
//                if !self.indicator.isAnimating{
//                    self.addToView()    // 로딩화면이 사라지고 정보를 보는 데이터 로드
//
//                    // label 이름 변경
//                    self.repo1Label.text = self.repo1[0].gitRepo.full_name
//                    self.repo2Label.text = self.repo2[0].gitRepo.full_name
//
//                }
//
//            }
//
//        })
//    }
    
    func getData(){
        
        CompareViewModel.viewModel.getRepoInfo()
            .subscribe(onNext: { infomation in
                self.indicator.stopAnimating()
                
                if !self.indicator.isAnimating{
                    self.firstRepo = infomation.firstRepo
                    self.secondRepo = infomation.secondRepo
                    
                    self.addToView()    // 로딩화면이 사라지고 정보를 보는 데이터 로드

                    // label 이름 변경
                    self.repo1Label.text = self.firstRepo?.gitRepo.full_name
                    self.repo2Label.text = self.secondRepo?.gitRepo.full_name
                }
            })
            .disposed(by: self.disposebag)
        
        
    }
    
    
}

extension CompareRepositoryController : UITableViewDelegate, UITableViewDataSource {
    /// tableview 생성하는 함수
    /// - Parameters:
    ///   - tableView: 유저 정보를 불러오는 함수
    ///   - indexPath: 셀의 인덱스
    /// - Returns: 생성된 셀
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: CompareRepositoryTableViewCell.identifier, for: indexPath) as? CompareRepositoryTableViewCell ?? CompareRepositoryTableViewCell()
        cell.backgroundColor = .white
        switch indexPath.row {
        case 0 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(self.firstRepo?.gitRepo.forks_count ?? 0), repo2Label: String(self.secondRepo?.gitRepo.forks_count ?? 0))
        case 1 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(self.firstRepo?.gitRepo.closed_issues_count ?? 0), repo2Label: String(self.secondRepo?.gitRepo.closed_issues_count ?? 0))
        case 2 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(self.firstRepo?.gitRepo.open_issues_count ?? 0), repo2Label: String(self.secondRepo?.gitRepo.open_issues_count ?? 0))
        case 3 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(self.firstRepo?.gitRepo.stargazers_count ?? 0), repo2Label: String(self.secondRepo?.gitRepo.stargazers_count ?? 0))
        case 4 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(self.firstRepo?.gitRepo.watchers_count ?? 0), repo2Label: String(self.secondRepo?.gitRepo.watchers_count ?? 0))
        case 5 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(self.firstRepo?.statistics.deletionStats.average ?? 0), repo2Label: String(self.secondRepo?.statistics.deletionStats.average ?? 0))
        case 6 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(self.firstRepo?.languagesStats.count ?? 0), repo2Label: String(self.secondRepo?.languagesStats.count ?? 0))
        case 7 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(self.firstRepo?.languagesStats.average ?? 0), repo2Label: String(self.secondRepo?.languagesStats.average ?? 0))
        default :
            print("error")
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return tableView.frame.height/4
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 8 }
    
}

extension CompareRepositoryController : UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: CompareRepoCollectionViewCell.identifier, for: indexPath) as? CompareRepoCollectionViewCell ?? CompareRepoCollectionViewCell()
        
        let repo1Info: [Double] = [self.firstRepo?.statistics.additionStats.average ?? 0, Double(self.firstRepo?.languagesStats.min ?? 0), self.firstRepo?.statistics.deletionStats.average ?? 0]
        let repo2Info: [Double] = [self.secondRepo?.statistics.additionStats.average ?? 0, Double(self.secondRepo?.languagesStats.min ?? 0), self.secondRepo?.statistics.deletionStats.average ?? 0]
        let repo1Language: [String] = self.firstRepo?.languages.language ?? []
        var repo1LanguagesCount: [Double] = []
        for count in self.firstRepo?.languages.count ?? []{
            repo1LanguagesCount.append(Double(count))
        }
        
        let repo2Language: [String] = self.secondRepo?.languages.language ?? []
        var repo2LanguagesCount: [Double] = []
        for count in self.secondRepo?.languages.count ?? []{
            repo2LanguagesCount.append(Double(count))
        }
        let repoNameList: [String] = [self.firstRepo?.gitRepo.full_name ?? "", self.secondRepo?.gitRepo.full_name ?? ""]
        
        switch indexPath.row{
        case 0:
            cell.setChartsInDiffCell(index: indexPath.row,repo1: repo1Info, repo2: repo2Info, values: ["addtion", "language", "deletion"], repoList: repoNameList)
        case 1:
            cell.setChartsInDiffCell(index: indexPath.row,repo1: repo1LanguagesCount, repo2: [nil], values: repo1Language, repoList: nil)
        case 2:
            cell.setChartsInDiffCell(index: indexPath.row, repo1: [nil], repo2: repo2LanguagesCount, values: repo2Language, repoList: nil)
        default:
            print("error!!")
        }
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
            return CGSize(width: collectionView.frame.width, height: collectionView.frame.height)
        }
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int { return 3 }
}
