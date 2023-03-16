//
//  LanguageFillteringTableView.swift
//  ios
//
//  Created by 정호진 on 2023/03/07.
//

import Foundation
import UIKit
import SnapKit

// 사용자 언어 선택하는 tableview
final class LanguageFillteringTableView: UIViewController{
    private let popularLanguage = ["C", "C#", "C++", "CoffeeScript ", "CSS", "Dart", "DM", "Elixir", "Go", "Groovy", "HTML", "Java", "JavaScript", "Kotlin", "Objective-C", "Perl", "PHP", "PowerShell", "Python", "Ruby", "Rust", "Scala", "Shell", "Swift", "TypeScript"]
    var selectedLanguage: [String] = [] //선택된 언어
    var selectedLangugaeIndex: [Int] = []
    var delegate: CheckLanguage?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        addToView()
    }
    
    /*
     UI 코드 작성
     */
    
    lazy var finishBtn: UIButton = {
       let btn = UIButton()
        btn.setTitleColor(.white, for: .normal)
        btn.setTitle("완료", for: .normal)
        btn.backgroundColor = .blue
        btn.layer.cornerRadius = 10
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 18)
        btn.addTarget(self, action: #selector(clickedFinishBtn), for: .touchUpInside)
        return btn
    }()
    
    lazy var langTableView: UITableView = {
        let tableView = UITableView()
        tableView.backgroundColor = .white
        tableView.register(LanguageFillteringTableViewCell.self, forCellReuseIdentifier: LanguageFillteringTableViewCell.identifier)
        tableView.allowsMultipleSelection = true
        return tableView
    }()
    
    /*
     UI Action 작성
     */
    
    // 사용자가 누른 언어 리스트 검색화면으로 전송
    @objc func clickedFinishBtn(){
        let uniqueLanguageArray = Set(self.selectedLanguage)
        let uniqueLanguageIndexArray = Set(self.selectedLangugaeIndex)
        
        self.delegate?.sendCheckingLangugae(languageList: Array(uniqueLanguageArray), index: Array(uniqueLanguageIndexArray))
        self.dismiss(animated: true)
    }
    
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addToView(){
        self.view.addSubview(self.langTableView)
        self.view.addSubview(self.finishBtn)
        self.langTableView.delegate = self
        self.langTableView.dataSource = self
        setAutoLayout()
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func setAutoLayout(){
        self.langTableView.snp.makeConstraints({ make in
            make.top.equalTo(finishBtn.snp.bottom).offset(10)
            make.bottom.equalTo(self.view.safeAreaLayoutGuide)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
        })
        
        self.finishBtn.snp.makeConstraints({ make in
            make.top.equalTo(self.view.safeAreaLayoutGuide)
            make.trailing.equalTo(-30)
        })
    }
    
    
    
}

extension LanguageFillteringTableView: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: LanguageFillteringTableViewCell.identifier, for: indexPath) as? LanguageFillteringTableViewCell ?? LanguageFillteringTableViewCell()
        cell.setLangugae(text: self.popularLanguage[indexPath.row])
        cell.backgroundColor = .white
        tableView.sectionIndexColor = .black;
        
        for index in self.selectedLangugaeIndex{
            if index == indexPath.row{
                cell.accessoryType = .checkmark
                self.selectedLanguage.append(self.popularLanguage[index])
            }
        }
        
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let index = self.selectedLangugaeIndex.firstIndex(of: indexPath.row)

        // 다시 선택한 경우
        if self.selectedLanguage.contains(self.popularLanguage[indexPath.row]){
            let languageIndex = self.selectedLanguage.firstIndex(of: self.popularLanguage[indexPath.row])
            self.selectedLanguage.remove(at: languageIndex ?? 0)
            
            self.selectedLangugaeIndex.remove(at: index ?? 0)
            
            // 다시 선택시 체크 모양 해제
            if let cell = tableView.cellForRow(at: indexPath) {
               cell.accessoryType = .none
             }
        }
        else{   // 처음 선택한 경우
            self.selectedLanguage.append(self.popularLanguage[indexPath.row])
            self.selectedLangugaeIndex.append(indexPath.row)
            
            // 선택시 체크 모양 표시
            if let cell = tableView.cellForRow(at: indexPath){
                cell.accessoryType = .checkmark
            }
        }
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return self.popularLanguage.count }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return "Popular Language"}
}


protocol CheckLanguage{
    func sendCheckingLangugae(languageList: [String], index: [Int])
}
