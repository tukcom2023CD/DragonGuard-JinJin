//
//  CustomUIView.swift
//  ios
//
//  Created by 정호진 on 2023/06/19.
//

import Foundation
import UIKit
import SnapKit
import Charts

final class CustomUIView: UIView{
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: 참여자 상위 4명 프로필 사진
    private lazy var contributorView: ContributorUIView = {
        let view = ContributorUIView()
        view.backgroundColor = .white
        view.layer.cornerRadius = 20
        view.layer.shadowOffset = CGSize(width:-3, height: 3)
        view.layer.shadowOpacity = 0.5
        return view
    }()
    
    // MARK: 레포 제목
    private lazy var repoLabel: UILabel = {
        let label = UILabel()
        label.sizeToFit()
        label.text = "aa"
        return label
    }()
   
    // MARK: 파이 차트
    private lazy var repoPieChart: PieChartView = {
        let chart = PieChartView()
        chart.backgroundColor = .white
        return chart
    }()
    
    // MARK: Add UI
    private func addUI(){
        self.addSubview(contributorView)
        self.addSubview(repoLabel)
        self.addSubview(repoPieChart)
        
        contributorView.snp.makeConstraints { make in
            make.leading.equalToSuperview().offset(30)
            make.trailing.equalToSuperview().offset(-30)
            make.top.equalToSuperview().offset(50)
        }
        
        repoLabel.snp.makeConstraints { make in
            make.centerX.equalTo(contributorView.snp.centerX)
            make.top.equalTo(contributorView.snp.bottom).offset(10)
        }
        
        repoPieChart.snp.makeConstraints { make in
            make.top.equalTo(repoLabel.snp.bottom).offset(30)
            make.leading.trailing.equalToSuperview()
            make.bottom.equalToSuperview()
        }
    }
    
    // MARK: Repo1 차트 설정
    func inputData(repo1: [Double?], values: [String]?, repoName: String, imgList: [String]){
        addUI()
        setRepoPieChartOptions(repo1: repo1, values: values)
        repoLabel.text = repoName
        contributorView.inputData(imgList: imgList)
    }
    
}

extension CustomUIView : ChartViewDelegate {
        
    // MARK: Repository Pie Chart
    private func setRepoPieChartOptions(repo1: [Double?], values: [String]?){
        var dataEntries: [PieChartDataEntry] = []
        guard let values = values else {return}
        guard let font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15) else {return}
        
        for dataCount in 0..<values.count{
            guard let data = repo1[dataCount] else {return}
            let dataEntry = PieChartDataEntry(value: data, label: values[dataCount])
            dataEntries.append(dataEntry)
        }
        
        let pieChartDataSet = PieChartDataSet(entries: dataEntries, label: " ")
        pieChartDataSet.colors = colorsOfCharts(numbersOfColor: repo1.count)
        pieChartDataSet.valueFont = font
        
        let pieChartData = PieChartData(dataSet: pieChartDataSet)
        let format = NumberFormatter()
        format.numberStyle = .none
        
        repoPieChart.data = pieChartData
        repoPieChart.isMultipleTouchEnabled = false
        repoPieChart.rotationEnabled = false
        repoPieChart.legend.textColor = .black
        repoPieChart.legend.font = font
    }
    
    // MARK: 랜덤 색상 설정
    private func colorsOfCharts(numbersOfColor: Int) -> [UIColor] {
      var colors: [UIColor] = []
      for _ in 0..<numbersOfColor {
        let red = Double(arc4random_uniform(256))
        let green = Double(arc4random_uniform(256))
        let blue = Double(arc4random_uniform(256))
        let color = UIColor(red: CGFloat(red/255), green: CGFloat(green/255), blue: CGFloat(blue/255), alpha: 1)
        colors.append(color)
      }
        return colors
    }
}
