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
        getData()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        
    }
    
    /*
     UI 코드 작성
     */
    
    lazy var repo1Label : UILabel = {
        let label = UILabel()
        label.text = "test1"
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
        label.text = "test2"
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
    
//    lazy var repoCollectionView : UICollectionView = {
//        let repoCollection = UICollectionView()
//        repoCollection.backgroundColor = .white
//        return repoCollection
//    }()
    /*
     UI Action 작성
     */
    
    private func addToView(){
        self.view.addSubview(repo1Label)
        self.view.addSubview(repo1ColorButton)
        self.view.addSubview(repo2Label)
        self.view.addSubview(repo2ColorButton)
        
//        self.view.addSubview(chart)
//        self.view.addSubview(repoCollectionView)
        repoTableView.delegate = self
        repoTableView.dataSource = self
//        repoCollectionView.delegate = self
//        repoCollectionView.dataSource = self
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
//            make.top.equalTo(view.safeAreaLayoutGuide)
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
        
        
//        chart.snp.makeConstraints ({ make in
//            make.top.equalTo(repoTableView.snp.bottom).offset(20)
//            make.leading.equalTo(30)
//            make.trailing.equalTo(-30)
//            make.bottom.equalTo(view.safeAreaLayoutGuide)
//        })
        
        
        
        
        
//        repoCollectionView.snp.makeConstraints( { make in
//            make.top.equalTo(repoTableView.snp.bottom).offset(10)
//            make.leading.equalTo(30)
//            make.trailing.equalTo(-30)
//            make.bottom.equalTo(view.safeAreaLayoutGuide)
//        })
//        setChartOption()
    }
    
    
    private func viewAutoLayout(){
        repoTableView.snp.makeConstraints({ make in
            make.top.equalTo(repo2ColorButton.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
    }
    
    func getData(){
        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true, block: { timer in
            self.viewModel.bringRepoInfo()
            self.viewModel.repo1Info.subscribe(onNext: {
                self.repo1 = $0
            }).disposed(by: self.disposebag)
            self.viewModel.repo2Info.subscribe(onNext: {
                self.repo2 = $0
            }).disposed(by: self.disposebag)
            
            if self.repo1.count != 0 && self.repo2.count != 0 {
                timer.invalidate()
                self.repo1Label.text = self.repo1[0].gitRepo.full_name
                self.repo2Label.text = self.repo2[0].gitRepo.full_name
                self.view.addSubview(self.repoTableView)
                self.viewAutoLayout()
                self.repoTableView.reloadData()
            }
            
        })
    }
    
}

extension CompareGraphController : UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 8
    }
    
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
    
    
}

//extension CompareGraphController : UICollectionViewDelegate, UICollectionViewDataSource {
//    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
//        <#code#>
//    }
//
//    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
//        <#code#>
//    }
//
//
//}



extension CompareGraphController : ChartViewDelegate {
    private func setChartOption() {
//        var dataSet : [RadarChartDataSet] = []
        let redDataSet = RadarChartDataSet(
            entries: [
                RadarChartDataEntry(value: 210),
                RadarChartDataEntry(value: 120.0),
                RadarChartDataEntry(value: 90.0),
                RadarChartDataEntry(value: 150.0),
            ]
        )
        redDataSet.lineWidth = 3
        redDataSet.fillColor = UIColor(red: 255/255, green: 0, blue: 0, alpha: 1)
        redDataSet.drawFilledEnabled = true
        redDataSet.colors = [UIColor(red: 255/255, green: 0, blue: 0, alpha: 0.3)]
        
        let blueDataSet = RadarChartDataSet(
            entries: [
                RadarChartDataEntry(value: 120.0),
                RadarChartDataEntry(value: 160.0),
                RadarChartDataEntry(value: 210.0),
                RadarChartDataEntry(value: 110.0)
            ]
        )
        blueDataSet.lineWidth = 3
        blueDataSet.fillColor = UIColor(red: 0, green: 0, blue: 255/255, alpha: 1)
        blueDataSet.drawFilledEnabled = true
        blueDataSet.colors = [UIColor(red: 0, green: 0, blue: 255/255, alpha: 0.3)]
        
        let data = RadarChartData(dataSets: [redDataSet, blueDataSet])
        chart.data = data
    }
    
    private func chartAttribute(){
//        self.chart.yAxis.labelFont = .systemFont(ofSize: 30)
//        self.chart.yAxis.valueFormatter = yaxisfomatter
    }
}

/*
 SwiftUI preview 사용 코드      =>      Autolayout 및 UI 배치 확인용
 preview 실행이 안되는 경우 단축키
 Command + Option + Enter : preview 그리는 캠버스 띄우기
 Command + Option + p : preview 재실행
 */

import SwiftUI

struct VCPreViewCompareGraphController:PreviewProvider {
    static var previews: some View {
        CompareGraphController().toPreview().previewDevice("iPhone 14 pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}

struct VCPreViewCompareGraphController2:PreviewProvider {
    static var previews: some View {
        CompareGraphController().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
