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
    let deviceWidth = UIScreen.main.bounds.width    // 각 장치들의 가로 길이
    let deviceHeight = UIScreen.main.bounds.height  // 각 장치들의 세로 길이
    let refreshTable = UIRefreshControl()   //새로 고침 사용
    private let disposeBag = DisposeBag()
    var searchResultList = [SearchPageResultModel]()
    var searchText = ""         // 검색하는 단어
    var beforePage: String = "" // 이전 View 이름
    var isInfiniteScroll = false // 무한 스크롤 1번만 로딩되게 확인하는 변수
    var filtering = ""  //필터링 조건 넣을 변수  ex) 언어, 스타, 포크 수 등등
    var filteringArray: [String] = []  // 언어를 제외한 모든 필터 API용
    var conditionFilter: [String] = []  // 언어를 제외한 모든 필터 사용자 시각용
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.navigationController?.navigationBar.isHidden = false
        self.navigationItem.backButtonTitle = "검색"
        
        searchResultList = []
        addUItoView()   //View에 적용할 UI 작성
        
        initRefreshTable()  //새로고침 함수
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        searchResultList = []
    }
    /*
     UI 작성
     */
    
    // 검색 UI
    lazy var searchUI: UISearchBar = {
        let searchBar = UISearchBar(frame: CGRect(x: 0, y: 0, width: deviceWidth - 20, height: 0))
        searchBar.searchTextField.textColor = .black
        searchBar.searchTextField.attributedPlaceholder = NSAttributedString(string: "Repository or User", attributes: [NSAttributedString.Key.foregroundColor: UIColor.lightGray])
        searchBar.searchTextField.backgroundColor = .white
        searchBar.searchBarStyle = .minimal
        searchBar.layer.cornerRadius = 10
        searchBar.placeholder = "Repository or User"
        searchBar.searchTextField.tintColor = .gray
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
    
    // 필터링 화면 이동하는 버튼
    lazy var filteringBtn: UIButton = {
        let btn = UIButton()
        btn.tintColor = UIColor.black
        btn.setTitleColor(.black, for: .normal)
        btn.setImage(UIImage(systemName: "arrowtriangle.down.fill"), for: .normal)
        btn.addTarget(self, action: #selector(clickedFilteringBtn), for: .touchUpInside)
        return btn
    }()
    
    lazy var filteringCollectionView : UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
        cv.backgroundColor = .white
        return cv
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
    
    @objc func refreshing(refresh: UIRefreshControl){ }
    
    // 검색한 데이터 가져오는 함수
    private func getData(searchWord: String, type: String, change: Bool, filtering: String){
        SearchPageViewModel.viewModel.getSearchData(searchWord: searchWord, type: type, change: change ,filtering: filtering)
            .subscribe(onNext: { searchList in
                for data in searchList{
                    self.searchResultList.append(data)
                }
                self.isInfiniteScroll = true
                self.filtering = "" // 필터링 초기화
                self.resultTableView.reloadData()
            })
            .disposed(by: disposeBag)
    }
    
    // 필터링 버튼 클릭 시 화면 전환
    @objc private func clickedFilteringBtn(){
        let filteringController = FilteringController()
        
        filteringController.delegate = self
        self.conditionFilter = []
        self.filteringArray = []
        
        self.present(filteringController, animated: true)
    }
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    
    //View에 적용할 때 사용하는 함수
    private func addUItoView(){
        
        self.navigationItem.titleView = searchUI
        
        // 필터링 버튼 적용
        let barButton = UIBarButtonItem(customView: filteringBtn)
        self.navigationItem.rightBarButtonItem = barButton
        
        // searchControllerDelegate
        self.searchUI.delegate = self
        
        self.view.addSubview(filteringCollectionView)
        self.filteringCollectionView.delegate = self
        self.filteringCollectionView.dataSource = self
        self.filteringCollectionView.register(FilteringCollectionViewCell.self, forCellWithReuseIdentifier: FilteringCollectionViewCell.identifier)
        
        // tableview 설치
        self.resultTableView.register(SearchPageTableView.self, forCellReuseIdentifier: SearchPageTableView.identifier)
        
    }
    private func addTableView(){
        self.view.addSubview(resultTableView)   //tableview 적용
        // 결과 출력하는 테이블 뷰 적용
        self.resultTableView.dataSource = self
        self.resultTableView.delegate = self
        
        setAutoLayuot()    // 검색 결과 출력할 tableview AutoLayout
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것 (추천)
     */
    
    private func setCollectionView(){
        filteringCollectionView.snp.makeConstraints({ make in
            make.top.equalTo(self.view.safeAreaLayoutGuide).offset(10)
            make.leading.equalTo(10)
            make.trailing.equalTo(-10)
            
            if !self.conditionFilter.isEmpty{
                make.height.equalTo(deviceHeight/15)
            }
        })
    }
    
    private func setAutoLayuot(){
        resultTableView.snp.makeConstraints({ make in
            make.top.equalTo(self.filteringCollectionView.snp.bottom).offset(10)
            make.leading.equalTo(10)
            make.trailing.equalTo(-10)
            make.bottom.equalTo(0)
        })
    }
    
}

// SearchController Delegate
extension SearchPageController: UISearchBarDelegate{
    
    // 검색 바 검색하기 시작할 때
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        searchResultList = []
    }
    
    // Cancel 취소 버튼 눌렀을 때
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        searchUI.text = ""
        searchUI.resignFirstResponder()
    }
    
    //검색 버튼을 눌렀을 때 실행
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchUI.resignFirstResponder()
        
        repositoryfiltering()
        
        guard let searchText = searchUI.text else{ return }
        addTableView()
        
        self.searchText = searchText
        getData(searchWord: searchText, type: "REPOSITORIES", change: true, filtering: self.filtering)    // API 감지 스레드
    }
    
    func repositoryfiltering(){
        
        if !self.filteringArray.isEmpty{
            for index in 0..<self.filteringArray.count{
                self.filtering.append(self.filteringArray[index])
                if index != self.filteringArray.count-1{
                    self.filtering.append(",")
                }
            }
        }
        print("필터링 결과")
        print(self.filtering)
    }
    
}

// 레포지토리 필터링된 정보들을 가지고 오는 구문
extension SearchPageController: SendFilteringData{
    func send(languageFilter: [String],
              languageFilterIndex: [Int],
              starFiltering: String,
              starIndex: Int?,
              forkFiltering: String,
              forkIndex: Int?,
              topicFiltering: String,
              topicIndex: Int?) {
        let starsArray = ["10 미만","50 미만","100 미만","500 미만","500 이상"]
        let forksArray = ["10 미만","50 미만","100 미만","500 미만","500 이상"]
        let topicArray = ["0","1","2","3","4 이상"]
        
        if !languageFilter.isEmpty{
            for lang in languageFilter{
                filteringArray.append("languages:\(lang)")
                conditionFilter.append(lang)
            }
        }
        if !starFiltering.isEmpty{
            for index in 0..<starsArray.count{
                if index == starIndex ?? -1{
                    filteringArray.append(starFiltering)
                    conditionFilter.append("star:\(starsArray[index])")
                }
            }
        }
        if !forkFiltering.isEmpty{
            for index in 0..<forksArray.count{
                if index == forkIndex ?? -1{
                    filteringArray.append(forkFiltering)
                    conditionFilter.append("fork:\(forksArray[index])")
                }
            }
        }
        if !topicFiltering.isEmpty{
            for index in 0..<topicArray.count{
                if index == topicIndex ?? -1{
                    filteringArray.append(topicFiltering)
                    conditionFilter.append("topic:\(topicArray[index])")
                }
            }
        }
        print("api \(self.filteringArray)")
        setCollectionView()
        self.filteringCollectionView.reloadData()
    }
}

extension SearchPageController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: FilteringCollectionViewCell.identifier, for: indexPath) as? FilteringCollectionViewCell ?? FilteringCollectionViewCell()

        cell.inputText(text: self.conditionFilter[indexPath.row])
        cell.layer.cornerRadius = collectionView.bounds.height/2
        cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return self.conditionFilter.count
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let cellHeight = collectionView.bounds.height
        let cellWidth = collectionView.bounds.width/4
        
        return CGSize(width: cellWidth, height: cellHeight)
        
    }
    
    
}


extension SearchPageController: UITableViewDelegate, UITableViewDataSource{
    
    
    // tableview cell 구성
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: SearchPageTableView.identifier,for: indexPath ) as! SearchPageTableView
        self.isInfiniteScroll = true
        cell.prepare(text: searchResultList[indexPath.section].name)
        cell.layer.cornerRadius = 15
        cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)
        cell.layer.borderWidth = 1
        
        return cell
    }
    
    // tableview cell이 선택된 경우
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let comparePage = CompareController()

        if beforePage == "Main"{
            let nextPage = RepoContributorInfoController()
            nextPage.selectedTitle = searchResultList[indexPath.section].name
            self.navigationController?.pushViewController(nextPage, animated: true)
        }
        else if beforePage == "CompareRepo1"{
            comparePage.repository1 = searchResultList[indexPath.section].name
            NotificationCenter.default.post(name: Notification.Name.data, object: nil,userInfo: [NotificationKey.choiceId: 1, NotificationKey.repository: searchResultList[indexPath.section].name])
            
            self.navigationController?.popViewController(animated: true)
        }
        else if beforePage == "CompareRepo2"{
            comparePage.repository2 = searchResultList[indexPath.section].name
            NotificationCenter.default.post(name: Notification.Name.data, object: nil,userInfo: [NotificationKey.choiceId: 2, NotificationKey.repository: searchResultList[indexPath.section].name])
            self.navigationController?.popViewController(animated: true)
        }
        
        searchUI.text = ""
        tableView.reloadData()
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    // 무한 스크롤하면서 API 호출하는 기능
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        let position = scrollView.contentOffset.y
        
        if position > (resultTableView.contentSize.height - scrollView.frame.size.height){
            if self.isInfiniteScroll{
                getData(searchWord: self.searchText, type: "REPOSITORIES", change: false,filtering: self.filtering)
                self.isInfiniteScroll = false
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
    func numberOfSections(in tableView: UITableView) -> Int { searchResultList.count }
    
    // 섹션 높이 지정
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " " }
    
}


