//
//  SettingController.swift
//  ios
//
//  Created by 정호진 on 2023/01/30.
//

import Foundation
import UIKit
import RxSwift

final class SettingController: UIViewController{
    // 설정화면에 출력될 종류들
    let settingData = ["토큰 부여 기준","FAQ","버전 정보","조직인증","로그아웃","회원탈퇴"]
    let adminSettingData = ["토큰 부여 기준","FAQ","버전 정보","조직인증","관리자","로그아웃"]
    private let disposeBag = DisposeBag()
    private var checkAdmin: Bool = false
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        /// UI view에 추가
        addUIToView()
        
        clickedBackBtn()
        
        /// table View AutoLayout
        settingTableViewSetLayout()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        AdminViewModel.admin.checkAdmin()
            .subscribe { check in
                self.checkAdmin = check
                self.settingTableView.reloadData()
            }
            .disposed(by: self.disposeBag)
    }
    
    /*
     UI 코드 작성
     */
    
    // MARK: 뒤로가기 버튼
    private lazy var backBtn: UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(named: "backBtn")?.resize(newWidth: 30), for: .normal)
        return btn
    }()
    
    // MARK: 설정 라벨
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.text = "설정"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 25)
        label.backgroundColor = .clear
        label.textColor = .black
        return label
    }()
    
    private lazy var settingTableView: UITableView = {
        let tableview = UITableView()
        tableview.backgroundColor = .white 
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
    
    // MARK: 로그아웃
    private func logOut(){
        UserDefaults.standard.removeObject(forKey: "Access")
        UserDefaults.standard.removeObject(forKey: "Refresh")
        
        LoginViewModel.loginService.logOutDone()
            .subscribe(onNext: { check in
                print("Called \(check)")
                if check{
                    (UIApplication.shared.connectedScenes.first?.delegate as? SceneDelegate)?.changeRootViewController(LoginController())
                }
            })
            .disposed(by: self.disposeBag)

    }
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUIToView(){
        view.addSubview(titleLabel)
        view.addSubview(backBtn)
        view.addSubview(settingTableView)
        
    
        self.settingTableView.delegate = self
        self.settingTableView.dataSource = self
        self.settingTableView.register(SettingTableViewCell.self, forCellReuseIdentifier: SettingTableViewCell.identifier)
        self.settingTableView.rowHeight = 50    //셀 높이 각 설정
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    // tableview Autolayout 설정
    private func settingTableViewSetLayout(){
        
        titleLabel.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.centerX.equalTo(view.snp.centerX)
        }
        
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(15)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
        }
        
        settingTableView.snp.makeConstraints({ make in
            
            make.top.equalTo(titleLabel.snp.bottom).offset(10)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(30)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-30)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
    }
    
    // MARK:
    private func clickedBackBtn(){
        backBtn.rx.tap.subscribe(onNext: {
            self.dismiss(animated: true)
        })
        .disposed(by: disposeBag)
    }
    
}


// TableView 속성 설정
extension SettingController: UITableViewDelegate, UITableViewDataSource{
    
    // 각 section 별 셀 개수
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    
    // 셀 속성 설정
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let color = UIColor.black   // label textColor 변경
        let cell = tableView.dequeueReusableCell(withIdentifier: SettingTableViewCell.identifier,for: indexPath) as? SettingTableViewCell ?? SettingTableViewCell()
        
        
        // 셀 속성 설정
        cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)   //셀 배경 색상
        cell.layer.cornerRadius = 15    //셀 모서리 둥글게
        cell.layer.borderWidth = 1  // 셀 바깥 선
        
        if checkAdmin{
            cell.inputDataTableView(text: adminSettingData[indexPath.section],color: color)
        }
        else{
            cell.inputDataTableView(text: settingData[indexPath.section],color: color)
        }
        
        
        return cell
    }
    
    // 셀이 선택되었을 때 실행
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        // 셀 눌렀을 때 기능
        switch indexPath.section{
        case 0:
            let nextPage = TokenStandards()
            nextPage.modalPresentationStyle = .fullScreen
            self.present(nextPage, animated: true)
        case 1:
            let nextPage = FAQPage()
            nextPage.modalPresentationStyle = .fullScreen
            self.present(nextPage, animated: true)
        case 2:
            versionInfo()   // 버전 정보
        case 3:
            
            let nextPage = OrganizationCertificationController()
            nextPage.modalPresentationStyle = .fullScreen
            self.present(nextPage, animated: true)
        case 4:
            if checkAdmin{
                let nextPage = AdminTabbarController()
                nextPage.modalPresentationStyle = .fullScreen
                self.present(nextPage, animated: true)
            }
            else{
                self.logOut()
            }
        case 5:
            if checkAdmin{
                self.logOut()
            }
            else{
                
            }
            
        default:
            return
        }
        
    }
    
    // Section 제목
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {return " " }
    
    // Section 개수
    func numberOfSections(in tableView: UITableView) -> Int {
        if checkAdmin{ return adminSettingData.count }
        else{ return settingData.count }
    }

    
    //Section 간격 설정
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat { return 1 }
}
