//
//  OrganizationSearchController.swift
//  ios
//
//  Created by 정호진 on 2023/03/30.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

// MARK: 조직 검색하는 클래스
final class OrganizationSearchController: UIViewController{
    private var searchResultList: [SearchOrganizationListModel] = []    /// 검색 결과 저장할 배열
    private let disposeBag = DisposeBag()
    var type: String?   /// 타입 선택한 경우
    var delegate: SendingOrganizationName?
    private var isInfiniteScroll = false
    private var searchText = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.searchResultList = []
        addUIToView()
        clickedBackBtn()
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
    
    // MARK: 조직 검색하는 검색 창
    private lazy var searchBar: UISearchBar = {
        let searchBar = UISearchBar()
        searchBar.searchTextField.textColor = .black
        searchBar.searchTextField.attributedPlaceholder = NSAttributedString(string: "Find Organization", attributes: [NSAttributedString.Key.foregroundColor: UIColor.lightGray])
        searchBar.searchTextField.backgroundColor = .white
        searchBar.searchBarStyle = .minimal
        searchBar.layer.cornerRadius = 10
        searchBar.searchTextField.tintColor = .gray
        searchBar.searchTextField.leftView?.tintColor = .black  //돋보기 색상 변경
        return searchBar
    }()
    
    // MARK: 조직 검색 결과 보여줄 테이블 뷰
    private lazy var resultTableView: UITableView = {
        let tableview = UITableView()
        tableview.backgroundColor = .white
        return tableview
    }()
    
    // MARK: 사용자의 조직이 없는 경우 조직 등록하는 버튼
    private lazy var addOrganizationBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("조직 등록하기", for: .normal)
        btn.setTitleColor(UIColor(red: 100/255, green: 100/255, blue: 200/255, alpha: 1), for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15)
        btn.addTarget(self, action: #selector(clickedAddOrganizationBtn), for: .touchUpInside)
        return btn
    }()
    
    /*
     UI AutoLayout & Add UI
     */
    
    // MARK: Add UI To View
    private func addUIToView(){
        view.addSubview(backBtn)
        view.addSubview(searchBar)
        self.searchBar.delegate = self
        
        view.addSubview(self.resultTableView)
        resultTableView.delegate = self
        resultTableView.dataSource = self
        resultTableView.register(OrganizationSearchTableViewCell.self, forCellReuseIdentifier: OrganizationSearchTableViewCell.identifier)
        
        view.addSubview(addOrganizationBtn)
        setAutoLayout()
    }
    
    // MARK: set UI AutoLayout
    private func setAutoLayout(){
        
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(15)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
        }
        
        searchBar.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(30)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-30)
        }
        
        addOrganizationBtn.snp.makeConstraints({ make in
            make.top.equalTo(searchBar.snp.bottom).offset(20)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-30)
        })
        
        resultTableView.snp.makeConstraints({ make in
            make.top.equalTo(addOrganizationBtn.snp.bottom).offset(10)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-10)
        })
    }
    
    /*
     UI Action
     */
    
    // MARK: 조직 등록 버튼 누른 경우
    @objc
    private func clickedAddOrganizationBtn(){
        let addOrganization = AddOrganizationController()
        addOrganization.type = self.type
        addOrganization.modalPresentationStyle = .fullScreen
        present(addOrganization, animated: false)
    }
    
    // MARK:
    private func clickedBackBtn(){
        backBtn.rx.tap.subscribe(onNext: {
            self.dismiss(animated: true)
        })
        .disposed(by: disposeBag)
    }
    
}

// MARK: SearchBar Delegate
extension OrganizationSearchController: UISearchBarDelegate{
    
    // MARK: 검색 바 검색하기 시작할 때
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        searchBar.text = ""
    }
    
    // MARK: Cancel 취소 버튼 눌렀을 때
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        searchBar.text = ""
        searchBar.resignFirstResponder()
    }
    
    // MARK: 검색 버튼을 눌렀을 때 실행
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        self.searchResultList = []
        searchBar.resignFirstResponder()
        
        guard let searchText = searchBar.text else{ return }
        self.searchText = searchText
        guard let type = self.type else {return}
        self.searchResultList = []
        
        /// 검색 api 통신 보냄
        CertifiedOrganizationViewModel.viewModel.getOrganizationList(name: searchText,
                                                                     type: type,
                                                                     check: false)
        .subscribe { resultList in
            
            self.setAutoLayout()
            self.searchResultList = resultList
           
            self.resultTableView.reloadData()
        }
        .disposed(by: disposeBag)
    }
}


extension OrganizationSearchController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: OrganizationSearchTableViewCell.identifier, for: indexPath) as! OrganizationSearchTableViewCell
    
        cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
        cell.layer.cornerRadius = 20
        cell.inputName(name: self.searchResultList[indexPath.section].name)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.delegate?.sendName(name: self.searchResultList[indexPath.section].name,
                                organizationId: self.searchResultList[indexPath.section].id)
        dismiss(animated: false)
    }
    
    // MARK: 무한 스크롤하면서 API 호출하는 기능
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let position = scrollView.contentOffset.y
        
        if position > (resultTableView.contentSize.height - scrollView.frame.size.height){
            if self.isInfiniteScroll{
                guard let type = self.type else {return}
                /// 검색 api 통신 보냄
                CertifiedOrganizationViewModel.viewModel.getOrganizationList(name: searchText,
                                                                             type: type,
                                                                             check: true)
                .subscribe { resultList in
                    self.searchResultList.append(contentsOf: resultList)
                    self.resultTableView.reloadData()
                }
                .disposed(by: disposeBag)
                self.isInfiniteScroll = false
            }
        }
        
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " " }
    
    func numberOfSections(in tableView: UITableView) -> Int { return self.searchResultList.count }
}

protocol SendingOrganizationName{
    func sendName(name: String, organizationId: Int)
}
