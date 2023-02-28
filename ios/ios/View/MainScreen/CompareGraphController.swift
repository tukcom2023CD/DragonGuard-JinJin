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


final class CompareGraphController : UIViewController {
    let deviceWidth = UIScreen.main.bounds.width
    let deviceHeight = UIScreen.main.bounds.height
    let viewModel = CompareViewModel()
    let disposebag = DisposeBag()
    var nameLabel : [String] = ["forks", "closed\nissues", "open\nissues", "stars", "contributers", "deletions\naverage", "languages", "codes\naverage"]
    var repo1 : [FirstRepoModel] = []
    var repo2 : [secondRepoModel] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        addToView()
        
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
    
    /*
     UI Action 작성
     */
    
    private func addToView(){
        self.view.addSubview(repo1Label)
        self.view.addSubview(repo1ColorButton)
        self.view.addSubview(repo2Label)
        self.view.addSubview(repo2ColorButton)
        repoTableView.delegate = self
        repoTableView.dataSource = self
        repoCollectionView.delegate = self
        repoCollectionView.dataSource = self
        repoCollectionView.register(CompareRepoCollectionView.self, forCellWithReuseIdentifier: CompareRepoCollectionView.identifier)
        repoTableView.register(CompareRepoTableView.self, forCellReuseIdentifier: CompareRepoTableView.identifier)
        setAutoLayout()
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
 
    }
    
    
    private func viewAutoLayout(){
        repoTableView.snp.makeConstraints({ make in
            make.top.equalTo(repo2ColorButton.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.height.equalTo(deviceHeight*30/100)
        })
        
        repoCollectionView.snp.makeConstraints( { make in
            make.top.equalTo(repoTableView.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
    }
    
    func getData(){
        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true, block: { timer in
            self.repo1 = []
            self.repo2 = []
            self.viewModel.bringRepoInfo()
            self.viewModel.repo1Info.subscribe(onNext: {
                self.repo1 = $0
            }).disposed(by: self.disposebag)
            self.viewModel.repo2Info.subscribe(onNext: {
                self.repo2 = $0
            }).disposed(by: self.disposebag)
            
            if self.repo1.count != 0 && self.repo2.count != 0 {
                timer.invalidate()
                // label 이름 변경
                self.repo1Label.text = self.repo1[0].gitRepo.full_name
                self.repo2Label.text = self.repo2[0].gitRepo.full_name
                
                // tableview collectionview 등록
                self.view.addSubview(self.repoTableView)
                self.view.addSubview(self.repoCollectionView)
                
                self.viewAutoLayout()
                self.repoTableView.reloadData()
                self.repoCollectionView.reloadData()
            }
            
        })
    }
    
}

extension CompareGraphController : UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: CompareRepoTableView.identifier, for: indexPath) as? CompareRepoTableView ?? CompareRepoTableView()
        
        switch indexPath.row {
        case 0 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(repo1[0].gitRepo.forks_count), repo2Label: String(repo2[0].gitRepo.forks_count))
        case 1 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(repo1[0].gitRepo.closed_issues_count), repo2Label: String(repo2[0].gitRepo.closed_issues_count))
        case 2 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(repo1[0].gitRepo.open_issues_count), repo2Label: String(repo2[0].gitRepo.open_issues_count))
        case 3 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(repo1[0].gitRepo.stargazers_count), repo2Label: String(repo2[0].gitRepo.stargazers_count))
        case 4 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(repo1[0].gitRepo.watchers_count), repo2Label: String(repo2[0].gitRepo.watchers_count))
        case 5 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(repo1[0].statistics.deletionStats.average), repo2Label: String(repo2[0].statistics.deletionStats.average))
        case 6 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(repo1[0].languagesStats.count), repo2Label: String(repo2[0].languagesStats.count))
        case 7 :
            cell.prepare(nameLabel: nameLabel[indexPath.row], repo1Label: String(repo1[0].languagesStats.average), repo2Label: String(repo2[0].languagesStats.average))
        default :
            print("error")
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return tableView.frame.height/3
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 8 }
    
}

extension CompareGraphController : UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: CompareRepoCollectionView.identifier, for: indexPath) as? CompareRepoCollectionView ?? CompareRepoCollectionView()
        
        let repo1Info: [Double] = [self.repo1[0].statistics.additionStats.average, Double(self.repo1[0].languagesStats.min), self.repo1[0].statistics.deletionStats.average]
        let repo2Info: [Double] = [self.repo2[0].statistics.additionStats.average, Double(self.repo2[0].languagesStats.min), self.repo2[0].statistics.deletionStats.average]
        let repo1Language: [String] = self.repo1[0].languages.language
        var repo1LanguagesCount: [Double] = []
        for count in self.repo1[0].languages.count{
            repo1LanguagesCount.append(Double(count))
        }
        
        let repo2Language: [String] = self.repo2[0].languages.language
        var repo2LanguagesCount: [Double] = []
        for count in self.repo2[0].languages.count{
            repo2LanguagesCount.append(Double(count))
        }
        
        
        switch indexPath.row{
        case 0:
            cell.setChartsInDiffCell(index: indexPath.row,repo1: repo1Info, repo2: repo2Info, values: ["addtion average", "language minumum", "deletion average"])
        case 1:
            cell.setChartsInDiffCell(index: indexPath.row,repo1: repo1LanguagesCount, repo2: [nil], values: repo1Language)
        case 2:
            cell.setChartsInDiffCell(index: indexPath.row, repo1: [nil], repo2: repo2LanguagesCount, values: repo2Language)
        default:
            print("error!!")
        }
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
            return CGSize(width: collectionView.frame.width, height: collectionView.frame.height)
        }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int { return 3 }
}
