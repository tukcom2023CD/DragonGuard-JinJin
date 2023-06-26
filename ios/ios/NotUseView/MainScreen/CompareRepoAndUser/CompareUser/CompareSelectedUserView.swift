////
////  CompareSelectedUserView.swift
////  ios
////
////  Created by 정호진 on 2023/02/27.
////
//
//import Foundation
//import UIKit
//import SnapKit
//import RxSwift
//
///// 유저를 선택하는 화면 Modal View 로 구성되어 있음
//final class CompareSelectedUserView: UIViewController{
//    var userArray: [String] = []
//    let beforePage = CompareUserController()
//    var whereComeFrom: String?
//    var beforeUser = ""
//    var delegate: SendingProtocol?
//
//    override func viewDidLoad() {
//        super.viewDidLoad()
//        self.view.backgroundColor = .white
//
//        if let num = whereComeFrom{
//            self.beforeUser = num
//        }
//        addToView()
//
//    }
//
//    lazy var tableview: UITableView = {
//        let tb = UITableView()
//        tb.backgroundColor = .white
//        tb.register(CompareSelectedUserTableView.self, forCellReuseIdentifier: CompareSelectedUserTableView.identifier)
//        return tb
//    }()
//
//
//    /// UI를 View에 붙이는 함수
//    private func addToView(){
//        self.view.addSubview(tableview)
//        self.tableview.delegate = self
//        self.tableview.dataSource = self
//        setAutoLayout()
//    }
//
//    /// AutoLayout 설정하는 함수
//    private func setAutoLayout(){
//        tableview.snp.makeConstraints({ make in
//            make.top.equalTo(view.safeAreaLayoutGuide)
//            make.leading.equalTo(30)
//            make.trailing.equalTo(-30)
//            make.bottom.equalTo(view.safeAreaLayoutGuide)
//        })
//    }
//
//
//}
//
//
//extension CompareSelectedUserView: UITableViewDelegate, UITableViewDataSource{
//
//    ///  유저 리스트를 생성하는 함수
//    /// - Parameters:
//    ///   - tableView: tableview UI
//    ///   - indexPath: 생성한 셀의 인덱스
//    /// - Returns: 생성한 셀을 반환
//    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
//        let cell = tableview.dequeueReusableCell(withIdentifier: CompareSelectedUserTableView.identifier, for: indexPath) as? CompareSelectedUserTableView ?? CompareSelectedUserTableView()
//        cell.setText(text: userArray[indexPath.section])
//        cell.backgroundColor = .white
//        return cell
//    }
//
//    /// 셀을 선택했을 때 실행되는 함수
//    /// - Parameters:
//    ///   - tableView: tableview UI
//    ///   - indexPath: Selected index of Cell or Section
//    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
//        tableView.deselectRow(at: indexPath, animated: true)
//        let beforePage = CompareUserController()
//
//        if whereComeFrom == "user1"{
//            beforePage.user1Index = indexPath.section
//            delegate?.dataSend(index: indexPath.section ,user: "user1")
//        }
//        else if whereComeFrom == "user2"{
//            beforePage.user2Index = indexPath.section
//            delegate?.dataSend(index: indexPath.section, user: "user2" )
//        }
//
//        dismiss(animated: true)
//
//    }
//
//    /// cell 높이 설정
//    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat { return 60 }
//
//    /// - Parameters:
//    ///   - tableView: tableview UI
//    ///   - section: tableview의 섹션
//    /// - Returns: 섹션 내부의 셀 개수
//    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
//
//    /// - Parameter tableView: tableview UI
//    /// - Returns: 섹션의 개수
//    func numberOfSections(in tableView: UITableView) -> Int { return userArray.count }
//
//}
//
///// 첫 번쨰 유저를 고르는지 두 번째 유저를 고르는지 확인 후 데이터를 전송
//protocol SendingProtocol {
//    func dataSend(index: Int, user: String)
//}
