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
    let settingData = ["토큰 부여 기준", "각 티어 종류","FAQ","도움말","버전 정보","로그아웃"]
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.navigationController?.navigationBar.isHidden = false
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
    
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUIToView(){
        self.view.addSubview(settingTableView)
        
        self.settingTableView.delegate = self
        self.settingTableView.dataSource = self
        self.settingTableView.register(SettingTableView.self, forCellReuseIdentifier: SettingTableView.identifier)
        
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    // tableview Autolayout 설정
    private func settingTableViewSetLayout(){
        settingTableView.snp.makeConstraints({ make in
            make.top.bottom.leading.trailing.equalTo(0)
        })
    }
    
}

// TableView 속성 설정
extension SettingController: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return settingData.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        var color = UIColor.black   // textColor 변경
        let cell = tableView.dequeueReusableCell(withIdentifier: SettingTableView.identifier,for: indexPath) as? SettingTableView ?? SettingTableView()
        
        // textColor 변경
        switch indexPath.row{
        case 5:
            color = UIColor.red
        default:
            color = UIColor.black
        }
        cell.inputDataTableView(text: settingData[indexPath.row],color: color)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        switch indexPath.row{
        case 0:
            self.navigationController?.pushViewController(TokenGivenCriteria(), animated: true)
        case 1:
            self.navigationController?.pushViewController(TierTypes(), animated: true)
        case 2:
            self.navigationController?.pushViewController(FAQPage(), animated: true)
        default:
            return
        }
    }
    
    
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
