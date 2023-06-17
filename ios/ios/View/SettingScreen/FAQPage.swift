//
//  FAQPage.swift
//  ios
//
//  Created by 정호진 on 2023/01/30.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

class FAQPage: UIViewController {
    var tableViewData = [CellData(opened: false,
                                  title: "Q. 로딩이 너무 느려요...",
                                  sectionData: "서버에서 열심히 계산 중이예요~🙏"),
                    CellData(opened: false,
                             title: "Q. 조직별 랭킹이 보이지 않아요",
                             sectionData: "조직 인증을 통해 소속 학교를 인증해야 해요"),
                    CellData(opened: false,
                             title: "Q. 조직 인증은 어디서 하나요?",
                             sectionData: "설정 → 조직 인증에서 가능합니다!"),
                    CellData(opened: false,
                             title: "Q. 정보가 업데이트 되지 않았어요",
                             sectionData: "일정 주기마다 업데이트 중이니 조금만 기다려주세요!!"),
                    CellData(opened: false,
                             title: "Q. 원하는 조직이 리스트에 없어요",
                             sectionData: "메일 주시면 추가하겠습니다. 죄송합니다!"),
                    CellData(opened: false,
                             title: "Q. 랭킹/티어를 올리고 싶어요!!",
                             sectionData: "commit, issue, PR, code review 등\ngithub 활동을 많이 많이 해보아요~!"),
                    CellData(opened: false,
                             title: "Q. 토큰은 어디에 쓰나요?",
                             sectionData: "현재 토큰은 다른 사람의 포인트를 탈취함을 \n방지하기 위해 쓰이고 있어요! \n금전적인 가치를 지니고 있지는 않답니다."),
                    CellData(opened: false,
                             title: "Q. 후원하고 싶어요 ^^",
                             sectionData: "감사하지만 마음만 받을게요!")]
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.navigationItem.title = "FAQ"
        self.navigationController?.interactivePopGestureRecognizer?.isEnabled = true
        addUIToView()
    }
    
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
        return label
    }()
    
    private lazy var tableView: UITableView = {
        let table = UITableView()
        table.backgroundColor = .white
        return table
    }()
    
    private func addUIToView(){
        view.addSubview(tableView)
        view.addSubview(titleLabel)
        view.addSubview(backBtn)
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.register(FAQTableViewCell.self, forCellReuseIdentifier: FAQTableViewCell.identifier)
        setAutoLayout()
        clickedBackBtn()
    }
    
    private func setAutoLayout(){
        
        titleLabel.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.centerX.equalTo(view.snp.centerX)
        }
        
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(15)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
        }
        
        self.tableView.snp.makeConstraints({ make in
            make.top.equalTo(titleLabel.snp.bottom).offset(10)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
            make.leading.equalTo(20)
            make.trailing.equalTo(-20)
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

extension FAQPage: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: FAQTableViewCell.identifier, for: indexPath) as! FAQTableViewCell
        
        if indexPath.row == 0 {
            cell.inputText(text: tableViewData[indexPath.section].title)
            cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)   //셀 배경 색상

        }
        else {
            cell.inputText(text: tableViewData[indexPath.section].sectionData)
            cell.backgroundColor = .white
        }
        
        cell.selectionStyle = .none
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if tableViewData[section].opened{
            return 2
        } else {
            return 1
        }
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return tableViewData.count
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if indexPath.row == 0 {
            tableViewData[indexPath.section].opened = !tableViewData[indexPath.section].opened
            tableView.reloadSections([indexPath.section], with: .none)
        }
        
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        if indexPath.section == 5 && indexPath.row == 1{
            return (tableView.frame.height) / 10
        }
        else if  indexPath.section == 6 && indexPath.row == 1{
            return (tableView.frame.height) / 9
        }
        
        return tableView.frame.height / 15
        
    }
    
}

struct CellData {
    var opened = Bool()
    var title = String()
    var sectionData = String()
}
