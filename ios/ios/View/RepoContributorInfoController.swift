//
//  RepoContributorInfoController.swift
//  ios
//
//  Created by 정호진 on 2023/02/07.
//

import Foundation
import Charts
import SnapKit
import UIKit

final class RepoContributorInfoController: UIViewController{
    
    let deviceWidth = UIScreen.main.bounds.width    //기기의 너비를 받아옴
    let deviceHeight = UIScreen.main.bounds.height
    
    
    let num = [1,2,3,4,5]
    let name = ["a","b","c","d","e"]
    var dataColor:[[UIColor]] = []  // 랜덤 색상 설정
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        RepoContributorInfoService.repoShared.getRepoContriInfo()
        addUItoView()
        setAutoLayout()
    }
    
    /*
     UI 작성
     */
    
    lazy var barChart: BarChartView = {
        let chart = BarChartView()
        chart.backgroundColor = .white
        
        return chart
    }()
    
    lazy var userTableView: UITableView = {
        let tableview = UITableView()
        tableview.register(RepoContributorTableView.self, forCellReuseIdentifier: RepoContributorTableView.identifier)
        
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
    
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것 (추천)
     */
    
    private func setAutoLayout(){
        // Chart AutoLayout
        barChart.snp.makeConstraints({ make in
            make.height.equalTo(deviceWidth)
            make.leading.equalTo(10)
            make.trailing.equalTo(-10)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
        
        //tableview Autolayout
        userTableView.snp.makeConstraints({ make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(20)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(barChart.snp.top).offset(0)
        })
    }
    
    
    
    // 색상 랜덤 설정
    private func randomColor(){
        for _ in 0..<name.count{
            let r: CGFloat = CGFloat.random(in: 0...1)
            let g: CGFloat = CGFloat.random(in: 0...1)
            let b: CGFloat = CGFloat.random(in: 0...1)
            dataColor.append([UIColor(red: r, green: g, blue: b, alpha: 0.8)])
        }
    }
    
}

// tableview 설정
extension RepoContributorInfoController: UITableViewDelegate, UITableViewDataSource{
    
    // cell 설정
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: RepoContributorTableView.identifier, for: indexPath) as? RepoContributorTableView ?? RepoContributorTableView()
        
        randomColor()
        cell.setLabel(num: num[indexPath.row], name: name[indexPath.row], color: dataColor[indexPath.row][0])
        setchartOption()
        return cell
    }
    
    // 각 색션 내부 셀 개수
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return name.count }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat { return 60 }
    
}

// Chart 설정
extension RepoContributorInfoController: ChartViewDelegate {
    
    private func setchartOption(){
        
        var dataSet: [BarChartDataSet] = []
        
        for i in 0..<name.count{
            var contributorInfo = [ChartDataEntry]()
            
            let dataEntry = BarChartDataEntry(x: Double(i), y: Double(num[i]))
            contributorInfo.append(dataEntry)
            
            
            let set1 = BarChartDataSet(entries: contributorInfo, label: "\(name[i])")
            set1.colors = dataColor[i]
            dataSet.append(set1)
        }

        let data = BarChartData(dataSets: dataSet)
        data.setValueFont(UIFont.systemFont(ofSize: 12))    // 그래프 위 숫자
        barChart.data = data
        customChart()
    }
    
    private func customChart(){
        barChart.rightAxis.enabled = false
        barChart.animate(xAxisDuration: 1, yAxisDuration: 2)
        barChart.leftAxis.enabled = true
        barChart.doubleTapToZoomEnabled = false
        barChart.xAxis.enabled = false
        barChart.leftAxis.labelFont = .systemFont(ofSize: 15)
        barChart.noDataText = "출력 데이터가 없습니다."
        barChart.noDataFont = .systemFont(ofSize: 30)
        barChart.noDataTextColor = .lightGray
        
    }
    
}



/*
 SwiftUI preview 사용 코드      =>      Autolayout 및 UI 배치 확인용
 
 preview 실행이 안되는 경우 단축키
 Command + Option + Enter : preview 그리는 캠버스 띄우기
 Command + Option + p : preview 재실행
 */

import SwiftUI

struct VCPreViewRepoConriInfo:PreviewProvider {
    static var previews: some View {
        RepoContributorInfoController().toPreview().previewDevice("iPhone 14 pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}

struct VCPreViewRepoConriInfo2:PreviewProvider {
    static var previews: some View {
        RepoContributorInfoController().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
