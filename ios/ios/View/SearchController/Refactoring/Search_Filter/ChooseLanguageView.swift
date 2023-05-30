//
//  ChooseLanguageView.swift
//  ios
//
//  Created by 정호진 on 2023/05/30.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

final class ChooseLanguageView: UIViewController{
    private let popularLanguage = ["C", "C#", "C++", "CoffeeScript", "CSS", "Dart", "DM", "Elixir", "Go", "Groovy", "HTML", "Java", "JavaScript", "Kotlin", "Objective-C", "Perl", "PHP", "PowerShell", "Python", "Ruby", "Rust", "Scala", "Shell", "Swift", "TypeScript"]
    var selectLanguage: [String] = []
    let selectedLanguage: BehaviorSubject<[String]> = BehaviorSubject(value: [])
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = UIColor(red: 239/255, green: 242/255, blue: 240/255, alpha: 1.0) /* #eff2f0 */
        addUIAndAutoLayout()
    }
    
    // MARK: 언어 보여줄 tableview
    private lazy var tableview: UITableView = {
        let table = UITableView()
        table.backgroundColor = UIColor(red: 239/255, green: 242/255, blue: 240/255, alpha: 1.0) /* #eff2f0 */
        table.separatorStyle = .none
        return table
    }()
    
    
    
    // MARK: Add UI and AutoLayout
    private func addUIAndAutoLayout(){
        self.view.addSubview(tableview)
        
        tableview.dataSource = self
        tableview.delegate = self
        tableview.register(LanguageUITableViewCell.self, forCellReuseIdentifier: LanguageUITableViewCell.identifier)
        
        tableview.snp.makeConstraints { make in
            make.top.equalTo(self.view.safeAreaLayoutGuide)
            make.leading.equalTo(self.view.safeAreaLayoutGuide).offset(10)
            make.trailing.equalTo(self.view.safeAreaLayoutGuide).offset(-10)
            make.bottom.equalTo(self.view.safeAreaLayoutGuide)
        }
    }
    
    
}

extension ChooseLanguageView: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: LanguageUITableViewCell.identifier, for: indexPath) as? LanguageUITableViewCell else {return UITableViewCell()}
        cell.layer.shadowOpacity = 0.5
        cell.layer.shadowOffset = CGSize(width: 1, height: 2)
        cell.layer.borderWidth = 1
        cell.layer.borderColor = CGColor(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        cell.layer.cornerRadius = 20
        cell.layer.masksToBounds = true
        
        if self.selectLanguage.contains(self.popularLanguage[indexPath.section]){
            cell.accessoryType = .checkmark
        }
        cell.inputText(self.popularLanguage[indexPath.section])
        
        cell.selectionStyle = .none
        cell.backgroundColor = .white
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        if selectLanguage.contains(self.popularLanguage[indexPath.section]){
            let languageIndex = self.selectLanguage.firstIndex(of: self.popularLanguage[indexPath.section])
            self.selectLanguage.remove(at: languageIndex ?? -1)
            if let cell = tableView.cellForRow(at: indexPath){
                cell.accessoryType = .none
            }
        }
        else{
            selectLanguage.append(self.popularLanguage[indexPath.section])
            if let cell = tableView.cellForRow(at: indexPath){
                cell.accessoryType = .checkmark
            }
        }
        selectedLanguage.onNext(selectLanguage)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    func numberOfSections(in tableView: UITableView) -> Int { return self.popularLanguage.count }
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " " }
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat { return 0.5 }
}



final class LanguageUITableViewCell: UITableViewCell{
    static let identifier = "LanguageUITableViewCell"
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        addUI()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: 언어 라벨
    private lazy var label: UILabel = {
        let label = UILabel()
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 18)
        label.textColor = .black
        label.backgroundColor = .clear
        return label
    }()
    
    // MARK: Add UI
    private func addUI(){
        self.addSubview(label)
        
        label.snp.makeConstraints { make in
            make.center.equalToSuperview()
        }
    }
    
    func inputText(_ text: String){
        label.text = text
    }
}


import SwiftUI
struct VCPreViewChooseLanguageView:PreviewProvider {
    static var previews: some View {
        ChooseLanguageView()        .toPreview().previewDevice("iPhone 14 Pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
struct VCPreViewChooseLanguageView2:PreviewProvider {
    static var previews: some View {
        ChooseLanguageView().toPreview().previewDevice("iPhone 11")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
