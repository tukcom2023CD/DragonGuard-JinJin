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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        setchartOption()
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
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUItoView(){
        
        self.barChart.delegate = self
        
        
    }
    
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것 (추천)
     */
    
    private func setAutoLayout(){
        barChart.snp.makeConstraints({ make in
            make.height.equalTo(deviceWidth)
            make.leading.equalTo(10)
            make.trailing.equalTo(-10)
            make.bottom.equalTo(0)
        })
    }
    
    
    
}

extension RepoContributorInfoController: ChartViewDelegate {
    
    private func setchartOption(){
        
        var dataSet: [BarChartDataSet] = []
        let dataColor = [[NSUIColor.cyan],[NSUIColor.blue],[NSUIColor.yellow],[NSUIColor.red],[NSUIColor.brown]]
        
        for i in 0..<name.count{
            var contributorInfo = [ChartDataEntry]()
            
            let dataEntry = BarChartDataEntry(x: Double(i), y: Double(num[i]))
            contributorInfo.append(dataEntry)
            
            let set1 = BarChartDataSet(entries: contributorInfo, label: "\(name[i])")
            set1.colors = dataColor[i]
            dataSet.append(set1)
        }

        let data = BarChartData(dataSets: dataSet)
        
        barChart.data = data
        
        customChart()
        view.addSubview(barChart)
        
    }
    
    private func customChart(){
        barChart.rightAxis.enabled = false
        barChart.animate(xAxisDuration: 1, yAxisDuration: 2)
        barChart.leftAxis.enabled = true
        barChart.xAxis.enabled = false
        
        
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
