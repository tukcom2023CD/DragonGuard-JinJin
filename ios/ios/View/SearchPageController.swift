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
import RxDataSources


// 검색창
final class SearchPageController: UIViewController {
    
    private let disposeBag = DisposeBag()
    private let searchViewModel = SearchPageViewModel()
    let deviceWidth = UIScreen.main.bounds.width    // 각 장치들의 가로 길이
    let deviceHeight = UIScreen.main.bounds.height  // 각 장치들의 세로 길이
    let uiSearchController = UISearchController()
    var resultData = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.navigationController?.navigationBar.isHidden = false
        
        addUItoView()   //View에 적용할 UI 작성
        resultTableViewSetLayout()    // 검색 결과 출력할 tableview AutoLayout
        
        
//        setTableViewDataSource()  //테이블 뷰 cell 및 개수 그리기
    }
    
    override func viewWillAppear(_ animated: Bool) {
        print("called")
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
        searchBar.showsCancelButton = true
        searchBar.searchTextField.leftView?.tintColor = .black  //돋보기 색상 변경
        return searchBar
    }()
    
    // 결과물 출력할 tableview
    lazy var resultTableView: UITableView = {
        let tableview = UITableView()
        tableview.backgroundColor = .white
        return tableview
    }()
    
    /*
     UI Action 작성
     
     */
    
    
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    
    //View에 적용할 때 사용하는 함수
    private func addUItoView(){
//        self.view.addSubview(searchUI)  //searchUI 적용
        
        self.navigationItem.titleView = searchUI
        self.view.addSubview(resultTableView)   //tableview 적용
        
        // 결과 출력하는 테이블 뷰 적용
        // datasource는 reactive 적용
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
    
    func searchBarTextDidBeginEditing(_ searchBar: UISearchBar) {
        
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        searchUI.text = ""
        searchUI.resignFirstResponder()
    }
    
    //검색 버튼을 눌렀을 때 실행
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchUI.resignFirstResponder()
        
        
        guard let searchText = searchUI.text else{ return }
        searchViewModel.searchInput.onNext(searchText)
        searchViewModel.getAPIData()
        
        
        resultData.append(searchText)
        resultTableView.reloadData()
        
    }
}


extension SearchPageController: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: SearchPageTableView.identifier,for: indexPath) as! SearchPageTableView
        
        var data = ""
        searchViewModel.switchData()
        searchViewModel.searchResult.subscribe(onNext: {
            data = $0.name
        }).disposed(by: disposeBag)
        resultData.append(data)
        
        cell.prepare(text: resultData[indexPath.section])
        cell.layer.cornerRadius = 15
        cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)
        cell.layer.borderWidth = 1
        
        return cell
    }
    
    // tableview cell이 선택된 경우
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("selected \(indexPath.section)")
        
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    // section 간격 설정
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {  return 1 }
    
    // 색션 내부 row 개수
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    
    // 색션 개수
    func numberOfSections(in tableView: UITableView) -> Int { resultData.count }
    
    // 섹션 높이 지정
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " " }
    
}


/*
 SwiftUI preview 사용 코드      =>      Autolayout 및 UI 배치 확인용
 
 preview 실행이 안되는 경우 단축키
 Command + Option + Enter : preview 그리는 캠버스 띄우기
 Command + Option + p : preview 재실행
 */

import SwiftUI

struct VCPreView:PreviewProvider {
    static var previews: some View {
        SearchPageController().toPreview().previewDevice("iPhone 14 pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}

struct VCPreView1:PreviewProvider {
    static var previews: some View {
        SearchPageController().toPreview().previewDevice("iPhone 11")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}

struct VCPreView2:PreviewProvider {
    static var previews: some View {
        SearchPageController().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
