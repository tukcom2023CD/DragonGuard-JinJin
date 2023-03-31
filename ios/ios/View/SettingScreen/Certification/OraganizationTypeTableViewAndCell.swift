//
//  OraganizationTypeTableViewAndCell.swift
//  ios
//
//  Created by 정호진 on 2023/03/30.
//

import Foundation
import UIKit
import SnapKit

final class OraganizationTypeTableView: UIViewController{
    private let typeList = ["대학교", "회사", "고등학교", "기타"]
    private let urlTypeList = ["UNIVERSITY", "COMPANY", "HIGH_SCHOOL", "ETC"]
    var delegate: SendingType?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        addUIToView()
    }
    
    private lazy var tableview: UITableView = {
       let tableview = UITableView()
        tableview.backgroundColor = .white
        tableview.separatorStyle = .none
        return tableview
    }()
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    // MARK: UI 등록
    private func addUIToView(){
        self.view.addSubview(tableview)
        tableview.delegate = self
        tableview.dataSource = self
        tableview.register(OraganizationTypeTableViewCell.self, forCellReuseIdentifier: OraganizationTypeTableViewCell.identifier)
        setAutoLayout()
    }
    
    // MARK: UI AutoLayout 적용
    private func setAutoLayout(){
        tableview.snp.makeConstraints({ make in
            make.top.bottom.equalTo(self.view.safeAreaLayoutGuide).offset(10)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
        })
    }
}

extension OraganizationTypeTableView: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: OraganizationTypeTableViewCell.identifier, for: indexPath) as! OraganizationTypeTableViewCell
        
        cell.layer.cornerRadius = 20
        cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)   //셀 배경 색상
        cell.inputText(text: typeList[indexPath.section])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        var type: String?
        var urlType: String?
        
        switch indexPath.section{
        case 0:
            type = typeList[indexPath.section]
            urlType = urlTypeList[indexPath.section]
        case 1:
            type = typeList[indexPath.section]
            urlType = urlTypeList[indexPath.section]
        case 2:
            type = typeList[indexPath.section]
            urlType = urlTypeList[indexPath.section]
        case 3:
            type = typeList[indexPath.section]
            urlType = urlTypeList[indexPath.section]
        default:
            print("잘못된 접근입니다.")
        }
        guard let type = type else { return }
        guard let urlType = urlType else { return }
        
        delegate?.sendType(type: type,urlType: urlType)
        self.dismiss(animated: true)
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " " }
    
    func numberOfSections(in tableView: UITableView) -> Int { return typeList.count }
}


// MARK: tableview cell
final class OraganizationTypeTableViewCell : UITableViewCell{
    static let identifier = "OraganizationTypeTableViewCell"
    
    // MARK: 타입 표시할 라벨
    private lazy var label: UILabel = {
        let label = UILabel()
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 30)
        
        contentView.addSubview(label)
        label.snp.makeConstraints({ make in
            make.center.equalToSuperview()
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
        })
        
        return label
    }()
    
    // MARK: cell에 데이터 대입
    func inputText(text: String){
        label.text = text
    }
    
}

// MARK: 사용자가 선택한 타입 전송
protocol SendingType {
    func sendType(type: String, urlType: String)
}
