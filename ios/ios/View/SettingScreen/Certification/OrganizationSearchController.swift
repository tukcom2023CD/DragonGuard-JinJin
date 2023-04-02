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
    private var isInfiniteScroll = false
    private var searchText = ""
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.searchResultList = []
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
        self.navigationItem.titleView = searchBar
        self.searchBar.delegate = self
        
        self.view.addSubview(self.resultTableView)
        resultTableView.delegate = self
        resultTableView.dataSource = self
        resultTableView.register(OrganizationSearchTableViewCell.self, forCellReuseIdentifier: OrganizationSearchTableViewCell.identifier)
        
        self.view.addSubview(addOrganizationBtn)
        
    }
    
    // MARK: set UI AutoLayout
    private func setAutoLayout(){
        addOrganizationBtn.snp.makeConstraints({ make in
            make.top.equalTo(self.view.safeAreaLayoutGuide)
            make.trailing.equalTo(-10)
        })
        
        resultTableView.snp.makeConstraints({ make in
            make.top.equalTo(self.addOrganizationBtn.snp.bottom).offset(10)
            make.bottom.equalTo(self.view.safeAreaLayoutGuide)
            make.leading.equalTo(10)
            make.trailing.equalTo(-10)
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
        self.navigationController?.pushViewController(addOrganization, animated: true)
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
            
            self.searchResultList = resultList
            self.setAutoLayout()
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
                    for data in resultList{
                        self.searchResultList.append(SearchOrganizationListModel(id: data.id,
                                                                                 name: data.name,
                                                                                 type: data.type,
                                                                                 emailEndpoint: data.emailEndpoint))
                    }
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
