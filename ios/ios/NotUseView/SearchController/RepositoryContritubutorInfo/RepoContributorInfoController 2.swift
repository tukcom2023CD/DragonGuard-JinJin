//
//  RepoContributorInfoController.swift
//  ios
//
//  Created by 정호진 on 2023/02/07.
//

import Foundation
import Charts
import SnapKit
import RxSwift
import UIKit

final class RepoContributorInfoController: UIViewController{
    
    let deviceWidth = UIScreen.main.bounds.width    //기기의 너비를 받아옴
    let deviceHeight = UIScreen.main.bounds.height
    let viewModel = RepoContributorInfoViewModel()
    let disposebag = DisposeBag()
    var selectedTitle: String?
    var contributorlist: [RepoContributorInfoModel] = []
    var userCommit: [Int] = []  // 사용자 커밋 횟수
    var userName: [String] = [] // 사용자 깃허브 아이디
    var dataColor:[[UIColor]] = []  // 랜덤 색상 설정
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        // 로딩화면 추가
        addIndicator()
        getData()
    }
    override func viewWillDisappear(_ animated: Bool) {
        userCommit = []
        userName = []
        dataColor = []
    }
    
    
    /*
     UI 작성
     */
    
    // 로딩 UI
    lazy var indicator: UIActivityIndicatorView = {
        let indicator = UIActivityIndicatorView()
        indicator.color = .gray
        indicator.isHidden = false
        indicator.startAnimating()
        indicator.style = .large
        
        return indicator
    }()
    
    // 차트 그리는 UI
    lazy var barChart: BarChartView = {
        let chart = BarChartView()
        chart.backgroundColor = .white
        return chart
    }()
    
    // 사용자 정보 나타내는 UI
    lazy var userTableView: UITableView = {
        let tableview = UITableView()
        tableview.register(RepoContributorTableView.self, forCellReuseIdentifier: RepoContributorTableView.identifier)
        tableview.backgroundColor = .white
        return tableview
    }()
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUItoView(){
        self.barChart.delegate = self
        self.userTableView.delegate = self
        self.userTableView.dataSource = self
        self.view.addSubview(barChart)
        self.view.addSubview(userTableView)
        
    }
    
    // 로딩 UI 추가
    private func addIndicator(){
        self.view.addSubview(indicator)
        setIndicatorAutoLayout()
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것 (추천)
     */
    
    private func setAutoLayout(){
        // Chart AutoLayout
        barChart.snp.makeConstraints({ make in
            make.top.equalTo(userTableView.snp.bottom)
            make.leading.equalTo(10)
            make.trailing.equalTo(-10)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
        
        //tableview Autolayout
        userTableView.snp.makeConstraints({ make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.height.equalTo(deviceHeight / 3)
        })
        
    }
    
    private func setIndicatorAutoLayout(){
        indicator.snp.makeConstraints({ make in
            make.center.equalToSuperview()
        })
    }
    
    // 색상 랜덤 설정
    private func randomColor(){
        for _ in 0..<userName.count{
            let r: CGFloat = CGFloat.random(in: 0...1)
            let g: CGFloat = CGFloat.random(in: 0...1)
            let b: CGFloat = CGFloat.random(in: 0...1)
            dataColor.append([UIColor(red: r, green: g, blue: b, alpha: 0.8)])
        }
    }
    
    private func getData(){
        guard let selectedTitle = self.selectedTitle else {return}
        
        self.viewModel.getContributorInfo(selectName: selectedTitle)
            .subscribe(onNext: { contributorList in
                self.indicator.stopAnimating()
                
                if !self.indicator.isAnimating{
                    self.addUItoView()
                    self.setAutoLayout()
                    
                    self.userCommit = []
                    self.userName = []
                    self.dataColor = []
                    
                    let name = selectedTitle.components(separatedBy: "/")
                    self.navigationItem.title = name[1]
                    self.navigationController?.navigationBar.titleTextAttributes = [NSAttributedString.Key.font: UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)!,.foregroundColor: UIColor.black]
                    
                    for data in contributorList{
                        self.userName.append(data.githubId)
                        self.userCommit.append(data.commits)
                    }
                    self.randomColor()
                    self.setchartOption()
                }
            })
            .disposed(by: disposebag)
    }
    
}

// tableview 설정
extension RepoContributorInfoController: UITableViewDelegate, UITableViewDataSource{
    
    // cell 설정
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: RepoContributorTableView.identifier, for: indexPath) as? RepoContributorTableView ?? RepoContributorTableView()
        
        cell.setLabel(num: userCommit[indexPath.row], name: userName[indexPath.row], color: dataColor[indexPath.row][0])
        cell.backgroundColor = .white
        return cell
    }
    
    // 각 색션 내부 셀 개수
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return userName.count }
    
    // cell 높이 설정
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat { return 60 }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        /// 유저 프로필 누른 경우
        for index in 0..<userName.count{
            if index == indexPath.row{
                let userProfileController = UserProfileController()
                userProfileController.githubId = userName[indexPath.row]
                self.navigationController?.pushViewController(userProfileController, animated: true)
            }
        }
        
    }
    
}

// Chart 설정
extension RepoContributorInfoController: ChartViewDelegate {
    
    // 차트 생성
    private func setchartOption(){
        
        var dataSet: [BarChartDataSet] = []
        
        for i in 0..<userName.count{
            var contributorInfo = [ChartDataEntry]()
            
            let dataEntry = BarChartDataEntry(x: Double(i), y: Double(userCommit[i]))
            contributorInfo.append(dataEntry)
            
            let set1 = BarChartDataSet(entries: contributorInfo, label: userName[i])
            
            set1.colors = dataColor[i]
            set1.valueTextColor = .black
            set1.valueColors = [.black]
            dataSet.append(set1)
        }

        let data = BarChartData(dataSets: dataSet)
        data.setValueFont(UIFont.systemFont(ofSize: 12))    // 그래프 위 숫자
        barChart.data = data
        customChart()
    }
    
    // 차트 옵션 설정
    private func customChart(){
        barChart.rightAxis.enabled = false
        barChart.animate(xAxisDuration: 2, yAxisDuration: 2)
        barChart.leftAxis.enabled = true
        barChart.doubleTapToZoomEnabled = false
        barChart.xAxis.enabled = false
        barChart.leftAxis.labelFont = .systemFont(ofSize: 15)
        barChart.legend.textColor = .black
        
        barChart.noDataText = "출력 데이터가 없습니다."
        barChart.noDataFont = .systemFont(ofSize: 30)
        barChart.noDataTextColor = .lightGray
        
    }
    
}


import SwiftUI
struct VCPreViewRepoContributorInfoController:PreviewProvider {
    static var previews: some View {
        RepoContributorInfoController().toPreview().previewDevice("iPhone 14 Pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
struct VCPreViewRepoContributorInfoController2:PreviewProvider {
    static var previews: some View {
        RepoContributorInfoController().toPreview().previewDevice("iPhone 11")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
