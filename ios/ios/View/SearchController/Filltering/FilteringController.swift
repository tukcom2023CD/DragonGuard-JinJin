//
//  FilteringController.swift
//  ios
//
//  Created by 정호진 on 2023/03/14.
//

import Foundation
import UIKit
import SnapKit

final class FilteringController: UIViewController{
    let deviceWidth = UIScreen.main.bounds.width
    let deviceHeight = UIScreen.main.bounds.height
    var filtering = ""  //언어 필터링 테스트용
    var languageFilterIndex: [Int] = [] // 언어 index 받아옴
    var languageFilter: [String] = []   // 선택한 언어 리스트
    var starFiltering = ""
    var forkFiltering = ""
    var topicFiltering = ""
    private let starsArray = ["10 미만","50 미만","100 미만","500 미만","500 이상"]
    private let forksArray = ["10 미만","50 미만","100 미만","500 미만","500 이상"]
    private let topicArray = ["0","1","2","3","4 이상"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        addToView()
        
    }
    
    // Languages
    lazy var languageSelections: UIButton = {
        let btn = UIButton()
        btn.setTitle("언어 선택하기", for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.layer.cornerRadius = 20
        btn.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
        btn.setTitleColor(.black, for: .normal)
        btn.addTarget(self, action: #selector(clickedLanguageSelections), for: .touchUpInside)
        return btn
    }()
    
    lazy var selectedLanguages: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
        cv.backgroundColor = .white
        cv.register(LanguageSelectionsCollectionViewCell.self, forCellWithReuseIdentifier: LanguageSelectionsCollectionViewCell.identifier)
        return cv
    }()
    
    // star label
    lazy var starLabel: UILabel = {
        let label = UILabel()
        label.text = "Star"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    lazy var starSelections: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
        cv.backgroundColor = .white
        cv.register(StarSelectionCollectionViewCell.self, forCellWithReuseIdentifier: StarSelectionCollectionViewCell.identifier)
        return cv
    }()
    
    
    // fork label
    lazy var forkLabel: UILabel = {
        let label = UILabel()
        label.text = "Fork"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    lazy var forkSelections: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
        cv.backgroundColor = .white
        cv.register(ForkSelectionCollectionViewCell.self, forCellWithReuseIdentifier: ForkSelectionCollectionViewCell.identifier)
        return cv
    }()
    
    // topic label
    lazy var topicLabel: UILabel = {
        let label = UILabel()
        label.text = "Topic"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    lazy var topicSelections: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
        cv.backgroundColor = .white
        cv.register(ForkSelectionCollectionViewCell.self, forCellWithReuseIdentifier: ForkSelectionCollectionViewCell.identifier)
        return cv
    }()
    
    // 선택 완료
    lazy var selectedDone: UIButton = {
        let btn = UIButton()
        btn.setTitle("선택 완료", for: .normal)
        btn.layer.cornerRadius = 20
        btn.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.addTarget(self, action: #selector(clickedSelectedDone), for: .touchUpInside)
        return btn
    }()
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addToView(){
        // Add Language
        self.view.addSubview(languageSelections)
        self.view.addSubview(selectedLanguages)
        self.selectedLanguages.delegate = self
        self.selectedLanguages.dataSource = self
        
        // Add Star
        self.view.addSubview(starLabel)
        self.view.addSubview(starSelections)
        self.starSelections.dataSource = self
        self.starSelections.delegate = self
        
        // Add Fork
        self.view.addSubview(forkLabel)
        self.view.addSubview(forkSelections)
        self.forkSelections.dataSource = self
        self.forkSelections.delegate = self
        
        // Add Topic
        self.view.addSubview(topicLabel)
        self.view.addSubview(topicSelections)
        self.topicSelections.dataSource = self
        self.topicSelections.delegate = self
        
        self.view.addSubview(selectedDone)
        
        setAutoLayout()
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것 (추천)
     */
    
    private func setLanguageAutoLayout(){
        self.selectedLanguages.snp.makeConstraints({ make in
            make.top.equalTo(self.languageSelections.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.height.equalTo(self.deviceHeight/15)
        })
    }
    private func setAutoLayout(){
        // Language AutoLayout
        self.languageSelections.snp.makeConstraints({ make in
            make.top.equalTo(self.view.safeAreaLayoutGuide).offset(10)
            make.leading.equalTo(30)
        })
        
        self.selectedLanguages.snp.makeConstraints({ make in
            make.top.equalTo(self.languageSelections.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
        })
        
        // Star AutoLayout
        self.starLabel.snp.makeConstraints({ make in
            make.top.equalTo(selectedLanguages.snp.bottom).offset(20)
            make.leading.equalTo(30)
        })
        
        self.starSelections.snp.makeConstraints({ make in
            make.top.equalTo(starLabel.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.height.equalTo(self.deviceHeight/20)
        })
        
        // Fork AutoLayout
        self.forkLabel.snp.makeConstraints({ make in
            make.top.equalTo(starSelections.snp.bottom).offset(20)
            make.leading.equalTo(30)
        })

        self.forkSelections.snp.makeConstraints({ make in
            make.top.equalTo(forkLabel.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.height.equalTo(self.deviceHeight/20)
        })
        
        // Topic AutoLayout
        self.topicLabel.snp.makeConstraints({ make in
            make.top.equalTo(forkSelections.snp.bottom).offset(20)
            make.leading.equalTo(30)
        })

        self.topicSelections.snp.makeConstraints({ make in
            make.top.equalTo(topicLabel.snp.bottom).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.height.equalTo(self.deviceHeight/20)
        })
        
        self.selectedDone.snp.makeConstraints({ make in
            make.top.equalTo(topicSelections.snp.bottom).offset(40)
            make.centerX.equalToSuperview()
            make.width.equalTo(deviceWidth/5)
        })
        
    }
    
    /*
     UI Action 작성
     */
    
    // 언어 선택 화면으로 이동
    @objc private func clickedLanguageSelections(){
        //언어 필터링
        let filter = LanguageFillteringTableView()
        filter.delegate = self
        filter.selectedLangugaeIndex = self.languageFilterIndex
        self.present(filter, animated: true)
    }
    
    // 필터링 선택 완료 시
    @objc private func clickedSelectedDone(){
        
        for i in 0..<self.languageFilterIndex.count{
            self.filtering.append("language:\(languageFilter[i])")
            
            // 마지막 요소인경우 ,를 붙이지 않음
            if i != self.languageFilterIndex.count-1 {
                self.filtering.append(",")
            }
        }
        
        if !self.starFiltering.isEmpty{
            if !self.filtering.isEmpty{
                self.filtering.append(",")
            }
            self.filtering.append("\(self.starFiltering)")
        }
        
        if !self.forkFiltering.isEmpty{
            if !self.filtering.isEmpty{
                self.filtering.append(",")
            }
            self.filtering.append("\(self.forkFiltering)")
        }
        
        if !self.topicFiltering.isEmpty{
            if !self.filtering.isEmpty{
                self.filtering.append(",")
            }
            self.filtering.append("\(self.topicFiltering)")
        }
    
        
        
        print("filter \(self.filtering)")
        self.dismiss(animated: true)
    }
}

extension FilteringController: CheckLanguage {
    func sendCheckingLangugae(languageList: [String], index: [Int]) {
        var indexArray:[Int] = []
        var stringArray:[String] = []
        for i in 0..<languageList.count{
            indexArray.append(index[i])
            stringArray.append(languageList[i])
        }

        let uniqueLanguageIndexArray = Set(indexArray)
        let uniqueLanguageArray = Set(stringArray)
        self.languageFilterIndex = Array(uniqueLanguageIndexArray)
        self.languageFilter = Array(uniqueLanguageArray)
        
        if !self.languageFilterIndex.isEmpty{
            setLanguageAutoLayout()
            self.selectedLanguages.reloadData()
        }
        
    }
}

extension FilteringController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == self.selectedLanguages{
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: LanguageSelectionsCollectionViewCell.identifier, for: indexPath) as? LanguageSelectionsCollectionViewCell ?? LanguageSelectionsCollectionViewCell()
            
            cell.inputText(text: languageFilter[indexPath.row])
            cell.layer.cornerRadius = cell.bounds.height/2
            cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
            return cell
        }
        else if collectionView == self.starSelections{
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: StarSelectionCollectionViewCell.identifier, for: indexPath) as! StarSelectionCollectionViewCell
            
            if indexPath.item == 0 {
               cell.isSelected = true
            }
            
            cell.inputText(text: starsArray[indexPath.row])
            cell.layer.cornerRadius = cell.bounds.height/2
            cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
            return cell
        }
        else if collectionView == self.forkSelections{
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ForkSelectionCollectionViewCell.identifier, for: indexPath) as! ForkSelectionCollectionViewCell
            
            if indexPath.item == 0 {
               cell.isSelected = true
            }
            
            cell.inputText(text: forksArray[indexPath.row])
            cell.layer.cornerRadius = cell.bounds.height/2
            cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
            return cell
        }
        else if collectionView == self.topicSelections{
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ForkSelectionCollectionViewCell.identifier, for: indexPath) as! ForkSelectionCollectionViewCell
            
            if indexPath.item == 0 {
               cell.isSelected = true
            }
            
            cell.inputText(text: topicArray[indexPath.row])
            cell.layer.cornerRadius = cell.bounds.height/2
            cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
            return cell
        }
        else {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ForkSelectionCollectionViewCell.identifier, for: indexPath) as! ForkSelectionCollectionViewCell
            
            if indexPath.item == 0 {
               cell.isSelected = true
            }
            
            cell.inputText(text: starsArray[indexPath.row])
            cell.layer.cornerRadius = cell.bounds.height/2
            cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
            return cell
        }
            
        
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == self.selectedLanguages{
                return languageFilterIndex.count
        }
        else{
            return 5
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let cellHeight = collectionView.bounds.height
        let cellWidth = collectionView.bounds.width/6
        
        if collectionView == self.selectedLanguages{
            let cellHeight = collectionView.bounds.height*3/4
            let cellWidth = collectionView.bounds.width/6
            
            return CGSize(width: cellWidth, height: cellHeight)
        }
        else{
            return CGSize(width: cellWidth, height: cellHeight)
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == self.selectedLanguages{
            
            for index in 0..<self.languageFilter.count{
                if index == indexPath.row{
                    self.languageFilter.remove(at: index)
                    self.languageFilterIndex.remove(at: index)
                    self.selectedLanguages.reloadData()
                }
            }
            
        }
        else if collectionView == self.starSelections{
            
            switch indexPath.row{
            case 0:
                let text = "stars:0..9"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.starFiltering){
                    self.starFiltering = ""
                }
                else{
                    self.starFiltering = text
                }
            case 1:
                let text = "stars:10..49"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.starFiltering){
                    self.starFiltering = ""
                }
                else{
                    self.starFiltering = text
                }
            case 2:
                let text = "stars:50..99"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.starFiltering){
                    self.starFiltering = ""
                }
                else{
                    self.starFiltering = text
                }
            case 3:
                let text = "stars:100..499"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.starFiltering){
                    self.starFiltering = ""
                }
                else{
                    self.starFiltering = text
                }
            case 4:
                let text = "stars:>=500"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.starFiltering){
                    self.starFiltering = ""
                }
                else{
                    self.starFiltering = text
                }
            default:
                print("잘못된 접근입니다.")
            }
          
        }
        else if collectionView == self.forkSelections{
            
            switch indexPath.row{
            case 0:
                let text = "forks:0..9"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.forkFiltering){
                    self.forkFiltering = ""
                }
                else{
                    self.forkFiltering = text
                }
            case 1:
                let text =  "forks:10..49"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.forkFiltering){
                    self.forkFiltering = ""
                }
                else{
                    self.forkFiltering = text
                }
            case 2:
                let text = "forks:50..99"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.forkFiltering){
                    self.forkFiltering = ""
                }
                else{
                    self.forkFiltering = text
                }
            case 3:
                let text = "forks:100..499"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.forkFiltering){
                    self.forkFiltering = ""
                }
                else{
                    self.forkFiltering = text
                }
            case 4:
                let text = "forks:>=500"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.forkFiltering){
                    self.forkFiltering = ""
                }
                else{
                    self.forkFiltering = text
                }
            default:
                print("잘못된 접근입니다.")
            }
        }
        else if collectionView == self.topicSelections{
            
            switch indexPath.row{
            case 0:
                let text = "topics:0"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.topicFiltering){
                    self.topicFiltering = ""
                }
                else{
                    self.topicFiltering = text
                }
            case 1:
                let text = "topics:1"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.topicFiltering){
                    self.topicFiltering = ""
                }
                else{
                    self.topicFiltering = text
                }
            case 2:
                let text = "topics:2"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.topicFiltering){
                    self.topicFiltering = ""
                }
                else{
                    self.topicFiltering = text
                }
            case 3:
                let text = "topics:3"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.topicFiltering){
                    self.topicFiltering = ""
                }
                else{
                    self.topicFiltering = text
                }
            case 4:
                let text = "topics:>3"
                if checkSelected(collectionView,indexPath: indexPath,text: text, choiceFiltering: self.topicFiltering){
                    self.topicFiltering = ""
                }
                else{
                    self.topicFiltering = text
                }
            default:
                print("잘못된 접근입니다.")
            }
        }
    }
    
    func checkSelected(_ collectionView: UICollectionView,indexPath: IndexPath, text: String, choiceFiltering: String) -> Bool{
        if choiceFiltering.contains(text){
            // 다시 선택시 체크 모양 해제
            if let cell = collectionView.cellForItem(at: indexPath) {
                cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
            }
            return true
        }
        return false
    }
    
}



import SwiftUI
struct VCPreViewFiltering:PreviewProvider {
    static var previews: some View {
        FilteringController().toPreview().previewDevice("iPhone 14 Pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
struct VCPreViewFiltering2:PreviewProvider {
    static var previews: some View {
        FilteringController().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}

