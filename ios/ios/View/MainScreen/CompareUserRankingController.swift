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


final class CompareUserRankingController : UIViewController {
    let deviceWidth = UIScreen.main.bounds.width
    let deviceHeight = UIScreen.main.bounds.height
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        addToView()
    }
    
    /*
     UI 코드 작성
     */
    
    lazy var user1Label : UILabel = {
        let label = UILabel()
        label.text = "test1"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        label.textColor = .black
        label.textAlignment = .center
        label.layer.borderWidth = 2
        label.layer.cornerRadius = 20
        return label
    }()
    
    lazy var user1ColorButton : UIButton = {
        let btn = UIButton()
        btn.backgroundColor = .red
        btn.layer.cornerRadius = deviceWidth/24
        btn.isEnabled = false
        return btn
    }()
    
    lazy var user2Label : UILabel = {
        let label = UILabel()
        label.text = "test2"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        label.textColor = .black
        label.textAlignment = .center
        label.layer.borderWidth = 2
        label.layer.cornerRadius = 20
        return label
    }()
    
    lazy var user2ColorButton : UIButton = {
        let btn = UIButton()
        btn.backgroundColor = .blue
        btn.layer.cornerRadius = deviceWidth/24
        btn.isEnabled = false
        return btn
    }()
    
    lazy var chart : RadarChartView = {
        let chart1 = RadarChartView()
        chart1.backgroundColor = .white
        return chart1
    }()
    
    /*
     UI Action 작성
     */
    
    private func addToView(){
        self.view.addSubview(user1Label)
        self.view.addSubview(user1ColorButton)
        self.view.addSubview(user2Label)
        self.view.addSubview(user2ColorButton)
        self.view.addSubview(chart)
        setAutoLayout()
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func setAutoLayout(){
        user1Label.snp.makeConstraints ({ make in
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.leading.equalTo(30)
            make.trailing.equalTo(user1ColorButton.snp.leading).offset(-10)
            make.height.equalTo(deviceWidth/10)
        })
        
        user1ColorButton.snp.makeConstraints ({ make in
//            make.top.equalTo(view.safeAreaLayoutGuide)
            make.centerY.equalTo(user1Label)
            make.trailing.equalTo(-30)
            make.width.equalTo(deviceWidth/12)
            make.height.equalTo(deviceWidth/12)
        })
        
        user2Label.snp.makeConstraints ({ make in
            make.top.equalTo(user1Label.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.height.equalTo(deviceWidth/10)
        })
        
        user2ColorButton.snp.makeConstraints ({ make in
            make.centerY.equalTo(user2Label)
            make.leading.equalTo(user2Label.snp.trailing).offset(10)
            make.trailing.equalTo(-30)
            make.width.equalTo(deviceWidth/12)
            make.height.equalTo(deviceWidth/12)
        })
        
        chart.snp.makeConstraints ({ make in
            make.top.equalTo(user2Label.snp.bottom).offset(20)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
        setChartOption()
    }

    
}

extension CompareUserRankingController : ChartViewDelegate {
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

struct VCPreViewCompareUserGraphController:PreviewProvider {
    static var previews: some View {
        CompareUserRankingController().toPreview().previewDevice("iPhone 14 pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}

struct VCPreViewCompareUserGraphController2:PreviewProvider {
    static var previews: some View {
        CompareUserRankingController().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
