////
////  CompareRepoCollectionView.swift
////  ios
////
////  Created by 정호진 on 2023/02/28.
////
//
//import Foundation
//import UIKit
//import Charts
//import SnapKit
//
//final class CompareRepoCollectionViewCell : UICollectionViewCell{
//    static let identifier = "CompareRepoCollectionView"
//    
//    // 레이더 차트
//    lazy var radarChart: RadarChartView = {
//        let chart = RadarChartView()
//        chart.backgroundColor = .white
//        return chart
//    }()
//    
//    // 파이 차트
//    lazy var repo1PieChart: PieChartView = {
//        let chart = PieChartView()
//        chart.backgroundColor = .white
//        return chart
//    }()
//    
//    // 파이 차트
//    lazy var repo2PieChart: PieChartView = {
//        let chart = PieChartView()
//        chart.backgroundColor = .white
//        return chart
//    }()
//    
//    func settingRadarChart(repo1: [Double?], repo2: [Double?], values: [String]?, repoList: [String]?){
//        self.addSubview(radarChart)
//        radarChart.snp.makeConstraints({ make in
//            make.top.bottom.leading.trailing.equalTo(0)
//        })
//        setRadarChartOptions(repo1: repo1, repo2: repo2, values: values, repoList: repoList)
//    }
//    
//    func settingRepo1Chart(repo1: [Double?], values: [String]?){
//        self.addSubview(repo1PieChart)
//        repo1PieChart.snp.makeConstraints({ make in
//            make.top.bottom.leading.trailing.equalTo(0)
//        })
//        setRepo1PieChartOptions(repo1: repo1, values: values)
//    }
//    
//    func settingRepo2Chart(repo2: [Double?], values: [String]?){
//        self.addSubview(repo2PieChart)
//        repo2PieChart.snp.makeConstraints({ make in
//            make.top.bottom.leading.trailing.equalTo(0)
//        })
//        setRepo2PieChartOptions(repo2: repo2, values: values)
//    }
//    
//    func setChartsInDiffCell(index: Int, repo1: [Double?], repo2: [Double?], values: [String]?, repoList: [String]?){
//        switch index{
//        case 0:
//            settingRadarChart(repo1: repo1, repo2: repo2, values: values, repoList: repoList)
//        case 1:
//            settingRepo1Chart(repo1: repo1, values: values)
//        case 2:
//            settingRepo2Chart(repo2: repo2, values: values)
//        default:
//            print("Done")
//        }
//        
//        
//    }
//    
//}
//
//
//extension CompareRepoCollectionViewCell : ChartViewDelegate {
//    
//    // Radar Chart
//    private func setRadarChartOptions(repo1: [Double?], repo2: [Double?], values: [String]?, repoList: [String]?){
//        guard let repoList = repoList else { return }
//        guard let xAxisFont = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15) else {return}
//        guard let yAxisFont = UIFont(name: "IBMPlexSansKR-SemiBold", size: 12) else {return}
//        
//        // red chart
//        let redDataSet = RadarChartDataSet()
//        redDataSet.lineWidth = 3
//        redDataSet.fillColor = UIColor(red: 255/255, green: 0, blue: 0, alpha: 1)
//        redDataSet.drawFilledEnabled = true
//        redDataSet.colors = [.red]
//        redDataSet.label = repoList[0]
//        redDataSet.drawValuesEnabled = false
//        
//        for data in repo1{
//            guard let data = data else {return}
//            let entry = RadarChartDataEntry(value: data)
//            redDataSet.append(entry)
//        }
//        
//        // blue chart
//        let blueDataSet = RadarChartDataSet()
//        blueDataSet.lineWidth = 3
//        blueDataSet.fillColor = .blue
//        blueDataSet.drawFilledEnabled = true
//        blueDataSet.colors = [.blue]
//        blueDataSet.label = repoList[1]
//        blueDataSet.drawValuesEnabled = false
//        
//        for data in repo2{
//            guard let data = data else {return}
//            
//            let entry = RadarChartDataEntry(value: data)
//            blueDataSet.append(entry)
//        }
//        
//        let data = RadarChartData(dataSets: [redDataSet, blueDataSet])
//        guard let values = values else { return }
//        
//        radarChart.xAxis.valueFormatter = IndexAxisValueFormatter(values: values)
//        radarChart.xAxis.labelTextColor = .black
//        radarChart.xAxis.labelFont = xAxisFont
//        radarChart.yAxis.enabled = true
//        radarChart.yAxis.labelTextColor = .black
//        radarChart.yAxis.labelFont = yAxisFont
//        radarChart.data = data
//        radarChart.isMultipleTouchEnabled = false
//        radarChart.rotationEnabled = false
//        radarChart.highlightPerTapEnabled = false
//        radarChart.legend.textColor = .black
//        radarChart.legend.font = xAxisFont
//    }
//    
//    // Repository 1 Pie Chart
//    private func setRepo1PieChartOptions(repo1: [Double?], values: [String]?){
//        var dataEntries: [PieChartDataEntry] = []
//        guard let values = values else {return}
//        guard let font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15) else {return}
//        
//        for dataCount in 0..<values.count{
//            guard let data = repo1[dataCount] else {return}
//            let dataEntry = PieChartDataEntry(value: data, label: values[dataCount])
//            dataEntries.append(dataEntry)
//        }
//        
//        let pieChartDataSet = PieChartDataSet(entries: dataEntries, label: " ")
//        pieChartDataSet.colors = colorsOfCharts(numbersOfColor: repo1.count)
//        pieChartDataSet.valueFont = font
//        
//        let pieChartData = PieChartData(dataSet: pieChartDataSet)
//        let format = NumberFormatter()
//        format.numberStyle = .none
//        
//        repo1PieChart.data = pieChartData
//        repo1PieChart.isMultipleTouchEnabled = false
//        repo1PieChart.rotationEnabled = false
//        repo1PieChart.legend.textColor = .black
//        repo1PieChart.legend.font = font
//    }
//    
//    // Repository 2 Pie Chart
//    private func setRepo2PieChartOptions(repo2: [Double?], values: [String]?){
//        var dataEntries: [PieChartDataEntry] = []
//        guard let values = values else {return}
//        guard let font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15) else {return}
//        
//        for dataCount in 0..<values.count{
//            guard let data = repo2[dataCount] else {return}
//            let dataEntry = PieChartDataEntry(value: data, label: values[dataCount])
//            dataEntries.append(dataEntry)
//        }
//        
//        let pieChartDataSet = PieChartDataSet(entries: dataEntries,label: " ")
//        pieChartDataSet.colors = colorsOfCharts(numbersOfColor: repo2.count)
//        pieChartDataSet.valueFont = font
//        
//        let pieChartData = PieChartData(dataSet: pieChartDataSet)
//        let format = NumberFormatter()
//        format.numberStyle = .none
//        
//        repo2PieChart.data = pieChartData
//        repo2PieChart.isMultipleTouchEnabled = false
//        repo2PieChart.rotationEnabled = false
//        repo2PieChart.legend.textColor = .black
//        repo2PieChart.legend.font = font
//    }
//    
//    private func colorsOfCharts(numbersOfColor: Int) -> [UIColor] {
//      var colors: [UIColor] = []
//      for _ in 0..<numbersOfColor {
//        let red = Double(arc4random_uniform(256))
//        let green = Double(arc4random_uniform(256))
//        let blue = Double(arc4random_uniform(256))
//        let color = UIColor(red: CGFloat(red/255), green: CGFloat(green/255), blue: CGFloat(blue/255), alpha: 1)
//        colors.append(color)
//      }
//        return colors
//    }
//}
