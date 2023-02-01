//
//  SettingController.swift
//  ios
//
//  Created by 정호진 on 2023/01/30.
//

import Foundation
import UIKit

final class SettingController: UIViewController{
    // 설정화면에 출력될 종류들
    let settingData = ["티어 종류","FAQ","버전 정보","로그아웃"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.navigationController?.navigationBar.isHidden = false   // navigation bar 생성
        self.navigationItem.title = "설정"
        
        // UI view에 추가
        addUIToView()
        
        // table View AutoLayout
        settingTableViewSetLayout()
        
    }
    
    /*
     UI 코드 작성
     */
    
    lazy var settingTableView: UITableView = {
        let tableview = UITableView()
        return tableview
    }()
    
    /*
     UI Action 작성
     */
    
    // 버전 정보
    private func versionInfo(){
        // 팝업창 띄움
        let sheet = UIAlertController(title: "버전 정보", message: "v1.0", preferredStyle: .alert)
        // 팝업창 확인 버튼
        sheet.addAction(UIAlertAction(title: "확인", style: .default))
        // 화면에 표시
        present(sheet,animated: true)
    }
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUIToView(){
        self.view.addSubview(settingTableView)
        
        self.settingTableView.delegate = self
        self.settingTableView.dataSource = self
        self.settingTableView.register(SettingTableView.self, forCellReuseIdentifier: SettingTableView.identifier)
        self.settingTableView.rowHeight = 50    //셀 높이 각 설정
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    // tableview Autolayout 설정
    private func settingTableViewSetLayout(){
        settingTableView.snp.makeConstraints({ make in
            make.leading.equalTo(20)
            make.trailing.equalTo(-20)
            make.top.equalTo(30)
            make.bottom.equalTo(0)
        })
    }
    
}


// TableView 속성 설정
extension SettingController: UITableViewDelegate, UITableViewDataSource{
    
    // 각 section 별 셀 개수
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    
    // 셀 속성 설정
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var color = UIColor.black   // label textColor 변경
        let cell = tableView.dequeueReusableCell(withIdentifier: SettingTableView.identifier,for: indexPath) as? SettingTableView ?? SettingTableView()
        
        
        // 셀 속성 설정
        cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)   //셀 배경 색상
        cell.layer.cornerRadius = 15    //셀 모서리 둥글게
        cell.layer.borderWidth = 1  // 셀 바깥 선
        
        // textColor 변경
        switch indexPath.row {
        case 3:
            color = UIColor.red
        default:
            color = UIColor.black
        }
        
        cell.inputDataTableView(text: settingData[indexPath.section],color: color)
        return cell
    }
    
    // 셀이 선택되었을 때 실행
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        // 셀 눌렀을 때 기능
        switch indexPath.section{
        case 0:
            self.navigationController?.pushViewController(TierTypes(), animated: true)
        case 1:
            self.navigationController?.pushViewController(FAQPage(), animated: true)
        case 2:
            versionInfo()   // 버전 정보
        default:
            return
        }
        
    }
    
    // Section 제목
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {return " " }
    
    // Section 개수
    func numberOfSections(in tableView: UITableView) -> Int { settingData.count }
    
    //Section 간격 설정
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat { return 1 }
}





/*
 SwiftUI preview 사용 코드      =>      Autolayout 및 UI 배치 확인용
 
 preview 실행이 안되는 경우 단축키
 Command + Option + Enter : preview 그리는 캠버스 띄우기
 Command + Option + p : preview 재실행
 */

import SwiftUI

struct VCPreViewSetting:PreviewProvider {
    static var previews: some View {
        SettingController().toPreview().previewDevice("iPhone 14 pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}

struct VCPreViewSetting2:PreviewProvider {
    static var previews: some View {
        SettingController().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
