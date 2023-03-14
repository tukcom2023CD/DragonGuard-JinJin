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
    var languagesArray: [String]?
    var filtering = ""  //언어 필터링 테스트용
    var languageFilterIndex: [Int] = [] // 언어 index 받아옴
    private let starsArray = ["10 미만","50 미만","100 미만","500 미만","500 이상"]
    private let forksArray = ["10 미만","50 미만","100 미만","500 미만","500 이상"]
    private let topicArray = ["0","1","2","3","4 이상"]
    private let popularLanguage = ["C", "C#", "C++", "CoffeeScript ", "CSS", "Dart", "DM", "Elixir", "Go", "Groovy", "HTML", "Java", "JavaScript", "Kotlin", "Objective-C", "Perl", "PHP", "PowerShell", "Python", "Ruby", "Rust", "Scala", "Shell", "Swift", "TypeScript"]
    
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
        
        setAutoLayout()
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것 (추천)
     */
    

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
//            make.height.equalTo(self.deviceHeight/10)
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
    
    
}

extension FilteringController: CheckLanguage {
    func sendCheckingLangugae(languageList: [String], index: [Int]) {
        for i in 0..<languageList.count{
            self.filtering.append("language:\(languageList[i])")
            self.languageFilterIndex.append(index[i])

            // 마지막 요소인경우 ,를 붙이지 않음
            if i != languageList.count-1 {
                self.filtering.append(",")
            }
        }
        print("index \(self.languageFilterIndex)")
        print("asdf \(self.filtering)")
        self.selectedLanguages.reloadData()
        
    }
}

extension FilteringController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == self.selectedLanguages{
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: LanguageSelectionsCollectionViewCell.identifier, for: indexPath) as? LanguageSelectionsCollectionViewCell ?? LanguageSelectionsCollectionViewCell()
            print("called \(popularLanguage[self.languageFilterIndex[indexPath.row]])")
            cell.inputText(text: popularLanguage[self.languageFilterIndex[indexPath.row]])
            cell.backgroundColor = .white
            cell.layer.cornerRadius = cell.bounds.height/2
            cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
            return cell
        }
        else if collectionView == self.starSelections{
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: StarSelectionCollectionViewCell.identifier, for: indexPath) as! StarSelectionCollectionViewCell
            
            cell.inputText(text: starsArray[indexPath.row])
            cell.backgroundColor = .white
            cell.layer.cornerRadius = cell.bounds.height/2
            cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
            return cell
        }
        else if collectionView == self.forkSelections{
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ForkSelectionCollectionViewCell.identifier, for: indexPath) as! ForkSelectionCollectionViewCell
            
            cell.backgroundColor = .white
            cell.inputText(text: forksArray[indexPath.row])
            cell.layer.cornerRadius = cell.bounds.height/2
            cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
            return cell
        }
        else if collectionView == self.topicSelections{
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ForkSelectionCollectionViewCell.identifier, for: indexPath) as! ForkSelectionCollectionViewCell
            
            cell.backgroundColor = .white
            cell.inputText(text: topicArray[indexPath.row])
            cell.layer.cornerRadius = cell.bounds.height/2
            cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
            return cell
        }
        else {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ForkSelectionCollectionViewCell.identifier, for: indexPath) as! ForkSelectionCollectionViewCell
            
            cell.backgroundColor = .white
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
//        else if collectionView == self.starSelections{
//            return starsArray.count
//        }
//        else if collectionView == self.forkSelections{
//            return forksArray.count
//        }
//        else if collectionView == self.topicSelections{
//            return topicArray.count
//        }
        else{
            return 5
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let cellHeight = collectionView.bounds.height
        let cellWidth = collectionView.bounds.width/6
        
        if collectionView == self.selectedLanguages{
            let cellHeight = collectionView.bounds.height
            let cellWidth = collectionView.bounds.width/6
            return CGSize(width: cellWidth, height: cellHeight)
        }
//        else if collectionView == self.starSelections{
//            let cellHeight = collectionView.bounds.height
//            let cellWidth = collectionView.bounds.width/6
//            return CGSize(width: cellWidth, height: cellHeight)
//        }
//        else if collectionView == self.forkSelections{
//            let cellHeight = collectionView.bounds.height
//            let cellWidth = collectionView.bounds.width/6
//            return CGSize(width: cellWidth, height: cellHeight)
//        }
//        else if collectionView == self.topicSelections{
//            let cellHeight = collectionView.bounds.height
//            let cellWidth = collectionView.bounds.width/6
//            return CGSize(width: cellWidth, height: cellHeight)
//        }
        else{
            return CGSize(width: cellWidth, height: cellHeight)
        }


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

