//
//  CompareGraphController.swift
//  ios
//
//  Created by 홍길동 on 2023/02/21.
//

import Foundation
import UIKit
import SnapKit
import RxCocoa
import RxSwift
import Charts


final class CompareUserController : UIViewController {
    let deviceWidth = UIScreen.main.bounds.width
    let deviceHeight = UIScreen.main.bounds.height
    var firstUserInfo : [FirstRepoResult] = []
    var secondUserInfo : [SecondRepoResult] = []
    let viewModel = CompareViewModel()
    let disposeBag = DisposeBag()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        addToView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        firstUserInfo = []
        secondUserInfo = []
        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true, block: { timer in
            self.viewModel.bringUserInfo()
            self.viewModel.firstUserInfo.subscribe(onNext: {
                self.firstUserInfo = $0
            })
            .disposed(by: self.disposeBag)
            self.viewModel.secondUserInfo.subscribe(onNext: {
                self.secondUserInfo = $0
            })
            .disposed(by: self.disposeBag)
            if self.firstUserInfo.count != 0 && self.secondUserInfo.count != 0 {
                timer.invalidate()
                print(self.firstUserInfo)
                print(self.secondUserInfo)
            }
        })
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
    
    lazy var chartCommit : BarChartView = {
        let chart1 = BarChartView()
        chart1.backgroundColor = .white
        return chart1
    }()
    
    lazy var chartAddDel : BarChartView = {
        let chart1 = BarChartView()
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
        self.view.addSubview(chartCommit)
        self.view.addSubview(chartAddDel)
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
        
        chartCommit.snp.makeConstraints ({ make in
            make.top.equalTo(user2Label.snp.bottom).offset(20)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.height.equalTo(deviceHeight/3)
        })
        
        chartAddDel.snp.makeConstraints ({ make in
            make.top.equalTo(chartCommit.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
        setChartCommit()
        setChartAddDel()
    }

    
}

extension CompareUserController : ChartViewDelegate {
    private func setChartCommit() {
//        var dataSet : [BarChartDataSet] = []
        let commitDataSet = BarChartDataSet(
            entries: [
                BarChartDataEntry(x: 2, y: 110),
                BarChartDataEntry(x: 3, y: 120),
            ]
        )
        commitDataSet.colors = [UIColor(red: 255/255, green: 0, blue: 0, alpha: 0.3)]
        
        
        let data = BarChartData(dataSets: [commitDataSet])
        chartCommit.data = data
        chartCommitAttribute()
    }
    
    private func chartCommitAttribute(){
        chartCommit.xAxis.enabled = false
        
    }
    
    private func setChartAddDel() {
//        var dataSet : [BarChartDataSet] = []
        let AddDelDataSet = BarChartDataSet(
            entries: [
                BarChartDataEntry(x: 2, y: 110),
                BarChartDataEntry(x: 3, y: 120),
            ]
        )
        AddDelDataSet.colors = [UIColor(red: 255/255, green: 0, blue: 0, alpha: 0.3)]
        
        
        let data = BarChartData(dataSets: [AddDelDataSet])
        chartAddDel.data = data
        chartAddDelAttribute()
    }
    
    private func chartAddDelAttribute(){
        chartAddDel.xAxis.enabled = false
        
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
        CompareUserController().toPreview().previewDevice("iPhone 14 pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}

struct VCPreViewCompareUserGraphController2:PreviewProvider {
    static var previews: some View {
        CompareUserController().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
