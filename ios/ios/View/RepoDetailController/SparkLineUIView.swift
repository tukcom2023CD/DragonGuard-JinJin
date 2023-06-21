//
//  SparkLineUIView.swift
//  ios
//
//  Created by 정호진 on 2023/06/21.
//

import Foundation
import SnapKit
import UIKit
import Charts

final class SparkLineUIView: UIView{
    private var sparkLineList: [Int] = []
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK:
    private lazy var sparkLine: LineChartView = {
        let chart = LineChartView()
        chart.backgroundColor = .clear
        return chart
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(sparkLine)
        
        sparkLine.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(20)
            make.leading.trailing.bottom.equalToSuperview()
            
        }
    }
    
    func inputData(lineList: [Int]){
        sparkLineList = lineList
        addUI()
        setSparkLineChart()
    }
    
}

extension SparkLineUIView: ChartViewDelegate{
    // MARK:
    private func setSparkLineChart(){
        var lineChartEntry = [ChartDataEntry]()
        
        for i in 0..<sparkLineList.count{
            let value = ChartDataEntry(x: Double(i), y: Double(self.sparkLineList[i]))
            
            lineChartEntry.append(value)
        }
        
        let line1 = LineChartDataSet(entries: lineChartEntry, label: "Number")
        line1.colors = [NSUIColor.red]
        line1.drawCirclesEnabled = false
        line1.drawValuesEnabled = false
        line1.lineWidth = 5
        line1.mode = .cubicBezier
        
        let data = LineChartData(dataSet: line1)
        
        sparkLine.data = data
        setOptions()
    }
    
    // MARK:
    private func setOptions(){
        sparkLine.leftAxis.enabled = false
        sparkLine.xAxis.enabled = false
        sparkLine.rightAxis.enabled = false
        sparkLine.legend.enabled = false
        sparkLine.animate(xAxisDuration: 2, yAxisDuration: 2)
        sparkLine.doubleTapToZoomEnabled = false
        sparkLine.highlightPerDragEnabled = false
        sparkLine.highlightPerTapEnabled = false
        
        sparkLine.noDataText = "출력 데이터가 없습니다."
        sparkLine.noDataFont = .systemFont(ofSize: 30)
        sparkLine.noDataTextColor = .lightGray
        
    }
    
}
