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

/// 유저 비교하는 화면
final class CompareUserController : UIViewController, SendingProtocol {
    let deviceWidth = UIScreen.main.bounds.width
    let deviceHeight = UIScreen.main.bounds.height
    var repoUserInfo: CompareUserModel = CompareUserModel(firstResult: [], secondResult: [])
    let disposeBag = DisposeBag()
    var chooseArray: [String] = []
    var user1Index: Int?
    var user2Index: Int?
    var lastIndexOfFisrtArray: Int?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        addIndicator()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        getUserInfo()
    }
    
    /*
     UI 코드 작성
     */
    
    lazy var user1Button : UIButton = {
        let btn = UIButton()
        btn.setTitle("choice user1", for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.titleLabel?.textAlignment = .center
        btn.layer.borderWidth = 2
        btn.layer.cornerRadius = 20
        btn.addTarget(self, action: #selector(clickedChooseUser1), for: .touchUpInside)
        return btn
    }()
    
    lazy var user1ColorButton : UIButton = {
        let btn = UIButton()
        btn.backgroundColor = .red
        btn.layer.cornerRadius = deviceWidth/24
        btn.isEnabled = false
        return btn
    }()
    
    lazy var user2Button : UIButton = {
        let btn = UIButton()
        btn.setTitle("choice user2", for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.titleLabel?.textAlignment = .center
        btn.layer.borderWidth = 2
        btn.layer.cornerRadius = 20
        btn.addTarget(self, action: #selector(clickedChooseUser2), for: .touchUpInside)
        return btn
    }()
    
    lazy var user2ColorButton : UIButton = {
        let btn = UIButton()
        btn.backgroundColor = .blue
        btn.layer.cornerRadius = deviceWidth/24
        btn.isEnabled = false
        return btn
    }()
    
    lazy var commitLabel: UILabel = {
        let label = UILabel()
        label.text = "Commits"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        label.backgroundColor = .white
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    lazy var chartCommit : BarChartView = {
        let chart1 = BarChartView()
        chart1.backgroundColor = .white
        return chart1
    }()
    
    lazy var addDelLabel: UILabel = {
        let label = UILabel()
        label.text = "Additions & Deletions"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        label.backgroundColor = .white
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    lazy var chartAddDel : BarChartView = {
        let chart1 = BarChartView()
        chart1.backgroundColor = .white
        return chart1
    }()
    
    // 로딩 UI
    lazy var indicator: UIActivityIndicatorView = {
        let indicator = UIActivityIndicatorView()
        indicator.color = .gray
        indicator.isHidden = false
        indicator.startAnimating()
        indicator.style = .large
        
        return indicator
    }()
    
    /*
     UI Action 작성
     */
    
    private func addToView(){
        self.view.addSubview(user1Button)
        self.view.addSubview(user1ColorButton)
        self.view.addSubview(user2Button)
        self.view.addSubview(user2ColorButton)
        self.view.addSubview(chartCommit)
        self.view.addSubview(chartAddDel)
        self.view.addSubview(commitLabel)
        self.view.addSubview(addDelLabel)
        setAutoLayout()
    }
    
    // 로딩 UI 추가
    private func addIndicator(){
        self.view.addSubview(indicator)
        setIndicatorAutoLayout()
    }
    
    /// 첫 번째 유저 선택하는 버튼 누른 경우
    @objc func clickedChooseUser1(){
        
        let mvNext = CompareSelectedUserView()
        
        if chooseArray.count == 0{
            for data in self.repoUserInfo.firstResult{
                chooseArray.append(data.githubId)
            }
            self.lastIndexOfFisrtArray = chooseArray.count
            for data in self.repoUserInfo.secondResult{
                chooseArray.append(data.githubId)
            }
        }
        
        mvNext.userArray = chooseArray
        mvNext.whereComeFrom = "user1"
        mvNext.delegate = self
        self.present(mvNext, animated: true)
        
    }
    
    /// 두 번째 유저 선택하는 버튼 누른 경우
    @objc func clickedChooseUser2(){
        let mvNext = CompareSelectedUserView()
        
        if chooseArray.count == 0{
            for data in self.repoUserInfo.firstResult{
                chooseArray.append(data.githubId)
            }
            self.lastIndexOfFisrtArray = chooseArray.count
            for data in self.repoUserInfo.secondResult{
                chooseArray.append(data.githubId)
            }
        }
        
        mvNext.userArray = chooseArray
        mvNext.whereComeFrom = "user2"
        mvNext.delegate = self
        self.present(mvNext, animated: true)
    }
    
    
    ///  Modal 뷰에서 유저를 선택했을 떄 데이터를 받아오는 부분
    /// - Parameters:
    ///   - index: 선택한 유저의 인덱스
    ///   - user: 첫 번쨰 유저인지 두 번쨰 유저인지 구분, user1 & user2 존재
    func dataSend(index: Int, user: String) {
        if user == "user1"{
            self.user1Index = index
            self.user1Button.setTitle(self.chooseArray[index], for: .normal)
        }
        else if user == "user2"{
            self.user2Index = index
            self.user2Button.setTitle(self.chooseArray[index], for: .normal)
        }
        
        if self.user1Index != nil && self.user2Index != nil{
            self.setChartCommit()
            self.setChartAddDel()
        }
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func setAutoLayout(){
        user1Button.snp.makeConstraints ({ make in
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.leading.equalTo(30)
            make.trailing.equalTo(user1ColorButton.snp.leading).offset(-10)
            make.height.equalTo(deviceWidth/10)
        })
        
        user1ColorButton.snp.makeConstraints ({ make in
            make.centerY.equalTo(user1Button)
            make.trailing.equalTo(-30)
            make.width.equalTo(deviceWidth/12)
            make.height.equalTo(deviceWidth/12)
        })
        
        user2Button.snp.makeConstraints ({ make in
            make.top.equalTo(user1Button.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.height.equalTo(deviceWidth/10)
        })
        
        user2ColorButton.snp.makeConstraints ({ make in
            make.centerY.equalTo(user2Button)
            make.leading.equalTo(user2Button.snp.trailing).offset(10)
            make.trailing.equalTo(-30)
            make.width.equalTo(deviceWidth/12)
            make.height.equalTo(deviceWidth/12)
        })
        
        commitLabel.snp.makeConstraints({ make in
            make.top.equalTo(user2Button.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
        })
        
        chartCommit.snp.makeConstraints ({ make in
            make.top.equalTo(commitLabel.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.height.equalTo(deviceHeight/4)
        })
        
        addDelLabel.snp.makeConstraints({ make in
            make.top.equalTo(chartCommit.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
        })
        
        chartAddDel.snp.makeConstraints ({ make in
            make.top.equalTo(addDelLabel.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
    }
    
    /// 데이터가 들어오기 전에 로딩화면 구성하는 부분
    private func setIndicatorAutoLayout(){
        indicator.snp.makeConstraints({ make in
            make.center.equalToSuperview()
        })
    }

    /// API 통신을 한 부분을 viewModel을 통해 데이터를 받아오는 부분
    func getUserInfo(){
//        CompareViewModel.viewModel.getUserInfo()
//            .subscribe(onNext: { userInfo in
//                print(userInfo)
//                self.repoUserInfo = userInfo
//                self.indicator.stopAnimating()
//                
//                if !self.indicator.isAnimating{
//                    self.addToView()
//                }
//            })
//            .disposed(by: disposeBag)
      
        
    }
    
    
}

extension CompareUserController : ChartViewDelegate {
    
    /// Commit 차트를 그리는 부분
    /// BarChart 사용
    private func setChartCommit() {
        var dataSet : [BarChartDataSet] = []
        guard let lastIndexOfFisrtArray = self.lastIndexOfFisrtArray else {return}
        guard let font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14) else {return}
        
        // data set 1
        var userInfo = [ChartDataEntry]()
        guard let user1Index = self.user1Index else {return}
        var set1 = BarChartDataSet()
        var repo = ""
        var newUser1Index = 0
        
        if lastIndexOfFisrtArray > user1Index{  // 첫 번째 유저 선택이 첫번쨰 배열 안에 있는 경우
            newUser1Index = user1Index
            repo = "first"
        }
        else if lastIndexOfFisrtArray <= user1Index{ // 첫 번째 유저 선택이 두 번째 배열 안에 있는 경우
            newUser1Index = user1Index - lastIndexOfFisrtArray
            repo = "second"
        }
        
        if repo == "first"{
            let dataEntry1 = BarChartDataEntry(x: 0, y: Double(self.repoUserInfo.firstResult[newUser1Index].commits))
            userInfo.append(dataEntry1)
            set1 = BarChartDataSet(entries: userInfo, label: self.repoUserInfo.firstResult[newUser1Index].githubId)
        }
        else if repo == "second"{
            let dataEntry1 = BarChartDataEntry(x: 0, y: Double(self.repoUserInfo.secondResult[newUser1Index].commits))
            userInfo.append(dataEntry1)
            set1 = BarChartDataSet(entries: userInfo, label: self.repoUserInfo.secondResult[newUser1Index].githubId)
        }
        set1.valueTextColor = .black
        set1.valueFont = font
        set1.colors = [.red]
        dataSet.append(set1)
        
        
        // data set 2
        guard let user2Index = self.user2Index else {return}
        var newUser2Index = 0
        var set2 = BarChartDataSet()
        var userInfo2 = [ChartDataEntry]()
        repo = ""
        if lastIndexOfFisrtArray > user2Index{  // 두 번째 유저 선택이 첫번째 배열 안에 있는 경우
            newUser2Index = user2Index
            repo = "first"
        }
        else if lastIndexOfFisrtArray <= user2Index{ // 두 번째 유저 선택이 두 번째 배열 안에 있는 경우
            newUser2Index = user2Index - lastIndexOfFisrtArray
            repo = "second"
        }
        
        if repo == "first"{
            let dataEntry2 = BarChartDataEntry(x: 1, y: floor(Double(self.repoUserInfo.firstResult[newUser2Index].commits)))
            userInfo2.append(dataEntry2)
            set2 = BarChartDataSet(entries: userInfo2, label: self.repoUserInfo.firstResult[newUser2Index].githubId)
        }
        else if repo == "second"{
            let dataEntry2 = BarChartDataEntry(x: 1, y: floor(Double(self.repoUserInfo.secondResult[newUser2Index].commits)))
            userInfo2.append(dataEntry2)
            set2 = BarChartDataSet(entries: userInfo2, label: self.repoUserInfo.secondResult[newUser2Index].githubId)
        }
        
        
        set2.valueTextColor = .black
        set2.valueFont = font
        set2.colors = [.blue]
        dataSet.append(set2)
        let data = BarChartData(dataSets: dataSet)
        chartCommit.data = data
        chartCommitAttribute()
    }
    
    
    ///  Commit 차트 속성을 설정하는 부분
    private func chartCommitAttribute(){
        guard let font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15) else {return}
        chartCommit.xAxis.enabled = false
        chartCommit.animate(xAxisDuration: 1, yAxisDuration: 2)
        chartCommit.leftAxis.enabled = true
        chartCommit.leftAxis.labelTextColor = .black
        chartCommit.doubleTapToZoomEnabled = false
        chartCommit.leftAxis.labelFont = font
        chartCommit.noDataText = "출력 데이터가 없습니다."
        chartCommit.noDataFont = .systemFont(ofSize: 30)
        chartCommit.noDataTextColor = .lightGray
        chartCommit.legend.textColor = .black
        chartCommit.legend.font = font
    }
    
    
    ///  Addtion, Deletion 차트를 그리는 부분
    ///  BarChart 사용
    private func setChartAddDel() {
        var dataSet : [BarChartDataSet] = []
        guard let lastIndexOfFisrtArray = self.lastIndexOfFisrtArray else {return}
        guard let font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14) else {return}
        
        // data set 1
        var userInfo = [ChartDataEntry]()
        guard let user1Index = self.user1Index else {return}
        var set1 = BarChartDataSet()
        var repo = ""
        var newUser1Index = 0
        
        if lastIndexOfFisrtArray > user1Index{  // 첫 번째 유저 선택이 첫번쨰 배열 안에 있는 경우
            newUser1Index = user1Index
            repo = "first"
        }
        else if lastIndexOfFisrtArray <= user1Index{ // 첫 번째 유저 선택이 두 번째 배열 안에 있는 경우
            newUser1Index = user1Index - lastIndexOfFisrtArray
            repo = "second"
        }
        
        if repo == "first"{
            let dataEntry1 = BarChartDataEntry(x: 0, y: Double(self.repoUserInfo.firstResult[newUser1Index].additions))
            let dataEntry2 = BarChartDataEntry(x: 2, y: Double(self.repoUserInfo.firstResult[newUser1Index].deletions))
            userInfo.append(dataEntry1)
            userInfo.append(dataEntry2)
            set1 = BarChartDataSet(entries: userInfo, label: self.repoUserInfo.firstResult[newUser1Index].githubId)
        }
        else if repo == "second"{
            let dataEntry1 = BarChartDataEntry(x: 0, y: Double(self.repoUserInfo.secondResult[newUser1Index].additions))
            let dataEntry2 = BarChartDataEntry(x: 2, y: Double(self.repoUserInfo.secondResult[newUser1Index].deletions))
            userInfo.append(dataEntry1)
            userInfo.append(dataEntry2)
            set1 = BarChartDataSet(entries: userInfo, label: self.repoUserInfo.secondResult[newUser1Index].githubId)
        }
        set1.valueTextColor = .black
        set1.valueFont = font
        set1.colors = [.red]
        dataSet.append(set1)
        
        
        // data set 2
        guard let user2Index = self.user2Index else { return }
        var newUser2Index = 0
        var set2 = BarChartDataSet()
        var userInfo2 = [ChartDataEntry]()
        repo = ""
        if lastIndexOfFisrtArray > user2Index{  // 두 번째 유저 선택이 첫번째 배열 안에 있는 경우
            newUser2Index = user2Index
            repo = "first"
        }
        else if lastIndexOfFisrtArray <= user2Index{ // 두 번째 유저 선택이 두 번째 배열 안에 있는 경우
            newUser2Index = user2Index - lastIndexOfFisrtArray
            repo = "second"
        }
        
        if repo == "first"{
            let dataEntry1 = BarChartDataEntry(x: 1, y: Double(self.repoUserInfo.firstResult[newUser2Index].additions))
            let dataEntry2 = BarChartDataEntry(x: 3, y: Double(self.repoUserInfo.firstResult[newUser2Index].deletions))
            userInfo2.append(dataEntry1)
            userInfo2.append(dataEntry2)
            set2 = BarChartDataSet(entries: userInfo2, label: self.repoUserInfo.firstResult[newUser2Index].githubId)
        }
        else if repo == "second"{
            let dataEntry1 = BarChartDataEntry(x: 1, y: Double(self.repoUserInfo.secondResult[newUser2Index].additions))
            let dataEntry2 = BarChartDataEntry(x: 3, y: Double(self.repoUserInfo.secondResult[newUser2Index].deletions))
            userInfo2.append(dataEntry1)
            userInfo2.append(dataEntry2)
            set2 = BarChartDataSet(entries: userInfo2, label: self.repoUserInfo.secondResult[newUser2Index].githubId)
        }
        set2.valueTextColor = .black
        set2.valueFont = font
        set2.colors = [.blue]
        dataSet.append(set2)
        
        
        
        let data = BarChartData(dataSets: dataSet)
        
        chartAddDel.data = data
        
        chartAddDelAttribute()
    }
    
    /// Addtion Deletion 그리는 차트 속성
    private func chartAddDelAttribute(){
        guard let font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15) else {return}
        
        chartAddDel.xAxis.enabled = false
        chartAddDel.animate(xAxisDuration: 1, yAxisDuration: 2)
        chartAddDel.leftAxis.enabled = true
        chartAddDel.leftAxis.labelTextColor = .black
        chartAddDel.doubleTapToZoomEnabled = false
        chartAddDel.leftAxis.labelFont = font
        chartAddDel.noDataText = "출력 데이터가 없습니다."
        chartAddDel.noDataFont = .systemFont(ofSize: 30)
        chartAddDel.noDataTextColor = .lightGray
        chartAddDel.legend.textColor = .black
        chartAddDel.legend.font = font
    }
}
