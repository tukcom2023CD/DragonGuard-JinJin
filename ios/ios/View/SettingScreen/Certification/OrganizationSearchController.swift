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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        addUIToView()
    }
    
    /*
     UI 코드 작성
     */
    
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
    
    /*
     UI AutoLayout & Add UI
     */
    
    // MARK: Add UI To View
    private func addUIToView(){
        self.navigationItem.titleView = searchBar
        self.searchBar.delegate = self
        
        self.view.addSubview(self.resultTableView)
        resultTableView.delegate = self
        resultTableView.dataSource = self
        resultTableView.register(OrganizationSearchTableViewCell.self, forCellReuseIdentifier: OrganizationSearchTableViewCell.identifier)
        setAutoLayout()
    }
    
    // MARK: set UI AutoLayout
    private func setAutoLayout(){
        
    }
    
}

// MARK: SearchBar Delegate
extension OrganizationSearchController: UISearchBarDelegate{
    
    // MARK: 검색 바 검색하기 시작할 때
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        
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
        guard let type = self.type else {return}
        
        /// 검색 api 통신 보냄
        CertifiedOrganizationViewModel.viewModel.getOrganizationList(name: searchText,
                                                                     type: type,
                                                                     check: false)
        .subscribe { resultList in
            self.searchResultList = resultList
            print(resultList)
        }
        .disposed(by: disposeBag)
    }
}


extension OrganizationSearchController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: OrganizationSearchTableViewCell.identifier, for: indexPath) as! OrganizationSearchTableViewCell
    
        cell.backgroundColor = .white
        
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " " }
    
    func numberOfSections(in tableView: UITableView) -> Int { return self.searchResultList.count }
}
