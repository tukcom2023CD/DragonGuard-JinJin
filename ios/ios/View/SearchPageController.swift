//
//  SearchPageController.swift
//  ios
//
//  Created by 정호진 on 2023/01/25.
//

import UIKit
import SnapKit
import RxSwift
import RxCocoa


// 검색창
final class SearchPageController: UIViewController {
    
    private let disposeBag = DisposeBag()
    private let searchViewModel = SearchPageViewModel()
    let deviceWidth = UIScreen.main.bounds.width    // 각 장치들의 가로 길이
    let deviceHeight = UIScreen.main.bounds.height  // 각 장치들의 세로 길이
    let refreshTable = UIRefreshControl()   //새로 고침 사용
    var resultData = [String]() // 결과 데이터 저장하는 배열
    var data = [SearchPageResultModel]()
    var timerThread: Timer?     //일정 시간동안 API 호출되는 응답 감시하는 타이머
    var searchText = ""         //검색하는 단어
    var fetchingMore = false    // 무한 스크롤 감지
    var sectionCount = 0        // 결과물 개수 저장
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.navigationController?.navigationBar.isHidden = false
        
        addUItoView()   //View에 적용할 UI 작성
        resultTableViewSetLayout()    // 검색 결과 출력할 tableview AutoLayout
        initRefreshTable()  //새로고침 함수
        
    }
    
    
    /*
     UI 작성
     */
    
    // 검색 UI
    lazy var searchUI: UISearchBar = {
        let searchBar = UISearchBar(frame: CGRect(x: 0, y: 0, width: deviceWidth, height: 0))
        searchBar.searchTextField.textColor = .black
        searchBar.searchTextField.backgroundColor = .white
        searchBar.searchBarStyle = .minimal
        searchBar.layer.cornerRadius = 10
        searchBar.placeholder = "Repository or User"
        searchBar.searchTextField.tintColor = .gray
        searchBar.showsCancelButton = true
        searchBar.searchTextField.leftView?.tintColor = .black  //돋보기 색상 변경
        return searchBar
    }()
    
    // 결과물 출력할 tableview
    lazy var resultTableView: UITableView = {
        let tableview = UITableView()
        tableview.backgroundColor = .white
        tableview.separatorStyle = .none
        return tableview
    }()
    
    /*
     UI Action 작성
     
     */
    
    // 새로고침하는 함수
    private func initRefreshTable(){
        refreshTable.addTarget(self, action: #selector(refreshing(refresh:)), for: .valueChanged)
        refreshTable.tintColor = .black
        resultTableView.refreshControl = refreshTable
    }
    
    @objc func refreshing(refresh: UIRefreshControl){
//        DispatchQueue.main.asyncAfter(deadline: .now()) {
//            self.resultTableView.reloadData()
//            refresh.endRefreshing()
//        }
    }
    
    @objc func timer(){
        self.searchViewModel.switchData()
        self.searchViewModel.searchResult
            .observe(on: MainScheduler.instance)
            .subscribe(onNext: {
                self.data = $0
            })
            .disposed(by: self.disposeBag)
        
        for i in self.data{
            self.resultData.append(i.name)
        }
        
        self.resultTableView.reloadData()
        fetchingMore = false
    }
    
    // 검색 결과 데이터 자동 쓰레드
    private func searchResultAutoThread(){
        timerThread = Timer.scheduledTimer(timeInterval: 3.0, target: self, selector: #selector(timer), userInfo: nil, repeats: false)
    }
    
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    
    //View에 적용할 때 사용하는 함수
    private func addUItoView(){
        
        self.navigationItem.titleView = searchUI
        self.view.addSubview(resultTableView)   //tableview 적용
        
        // 결과 출력하는 테이블 뷰 적용
        self.resultTableView.dataSource = self
        self.resultTableView.delegate = self
        
        // searchControllerDelegate
        self.searchUI.delegate = self
        
        // tableview 설치
        self.resultTableView.register(SearchPageTableView.self, forCellReuseIdentifier: SearchPageTableView.identifier)
        
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것 (추천)
     */
    
    // tableview Autolayout 설정
    private func resultTableViewSetLayout(){
        resultTableView.snp.makeConstraints({ make in
            make.top.equalTo(50)
            make.bottom.equalTo(0)
            make.leading.equalTo(10)
            make.trailing.equalTo(-10)
        })
    }
    
}

// SearchController Delegate
extension SearchPageController: UISearchBarDelegate{
    
    // 검색 바 검색하기 시작할 때
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        resultData = []
        data = []
    }
    
    // Cancel 취소 버튼 눌렀을 때
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        searchUI.text = ""
        searchUI.resignFirstResponder()
    }
    
    //검색 버튼을 눌렀을 때 실행
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchUI.resignFirstResponder()
        
        guard let searchText = searchUI.text else{ return }
        self.searchText = searchText
        
        searchViewModel.pageCount = 0
        callAPI(searchText: searchText)
        resultTableView.reloadData()
        
        searchResultAutoThread()    // API 감지 스레드
    }
    
    // API 호출
    func callAPI(searchText: String?){
        searchViewModel.searchInput.onNext(searchText ?? "")
        searchViewModel.pageCount += 1
        searchViewModel.getAPIData()
        
    }
    
}


extension SearchPageController: UITableViewDelegate, UITableViewDataSource{
    
    
    // tableview cell 구성
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: SearchPageTableView.identifier,for: indexPath ) as! SearchPageTableView
        
        cell.prepare(text: resultData[indexPath.section])
        cell.layer.cornerRadius = 15
        cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)
        cell.layer.borderWidth = 1
        
        sectionCount = indexPath.section
        
        return cell
    }
    
    // tableview cell이 선택된 경우
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("selected \(indexPath.section)")
        
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    // 무한 스크롤하면서 API 호출하는 기능
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let position = scrollView.contentOffset.y
        
        if position > (resultTableView.contentSize.height - scrollView.frame.size.height) || (sectionCount + 1) % 10 == 0{
            if !fetchingMore {
                
                fetchingMore = true
//                callAPI(searchText: self.searchText)
//                searchResultAutoThread()    // API 감지 스레드
            }
        }
        
    }
    
    // cell 높이 설정
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat { return deviceHeight / 10 }
    
    // section 간격 설정
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {  return 1 }
    
    // 색션 내부 row 개수
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    
    // 색션 개수
    func numberOfSections(in tableView: UITableView) -> Int { resultData.count }
    
    // 섹션 높이 지정
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " " }
    
}
