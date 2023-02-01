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
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.navigationController?.navigationBar.isHidden = false
        
        addUItoView()   //View에 적용할 UI 작성
        searchUISetLayout()     // searchUI AutoLayout 함수
        resultTableViewSetLayout()    // 검색 결과 출력할 tableview AutoLayout
        setTableViewDataSource()  //테이블 뷰 cell 및 개수 그리기
        bindInput() //테스트 목적
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
    
    // 검색 UI Autolayout 설정
    private func searchUISetLayout(){
//        searchUI.snp.makeConstraints({ make in
//            make.top.equalTo(self.view.safeAreaLayoutGuide)
//            make.leading.equalTo(10)
//            make.trailing.equalTo(-10)
//        })
    }
    
    // tableview Autolayout 설정
    private func resultTableViewSetLayout(){
        resultTableView.snp.makeConstraints({ make in
            make.top.equalTo(50)
            make.bottom.equalTo(0)
            make.leading.equalTo(10)
            make.trailing.equalTo(-10)
        })
    }
    
    /*
     UI 입출력 감지
     */
    
    
    private func bindInput(){
        //데이터 잘들어가는 지 테스트
        //        searchViewModel.checkValidId
        //            .subscribe(onNext: {
        //                print($0)
        //            })
    }
    
    
    
    // 결과물 출력할 tableview 설정하는 부분
     func setTableViewDataSource() {
         
         let dataSource = RxTableViewSectionedReloadDataSource<SearchModel> { _, tableview, indexPath, item in
             let cell = tableview.dequeueReusableCell(withIdentifier: SearchPageTableView.identifier,for: indexPath) as! SearchPageTableView
             cell.prepare(text: item)
             cell.layer.cornerRadius = 15
             cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)
             cell.layer.borderWidth = 1
             return cell
         } titleForHeaderInSection: { dataSource, sectionIndex in
             return dataSource[sectionIndex].header
         }
         
         // 데이터 테스트
         let sections = [
            SearchModel(header: " ", items: ["1"]),
            SearchModel(header: " ", items: ["2"]),
            SearchModel(header: " ", items: ["3"]),
            SearchModel(header: " ", items: ["4"])
         ]
         // 데이터 삽입
         Observable.just(sections)
             .bind(to: resultTableView.rx.items(dataSource: dataSource))
             .disposed(by: disposeBag)
        
    }
}

// SearchController Delegate
extension SearchPageController: UISearchBarDelegate{
    //검색 버튼을 눌렀을 때 실행
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        searchUI.resignFirstResponder()
        self.searchUI.showsCancelButton = false
        
        guard let searchText = searchUI.text else{ return }
        searchViewModel.searchingData.onNext(searchText)
        
    }
}


extension SearchPageController: UITableViewDelegate{
    // tableview cell이 선택된 경우
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        print("selected \(indexPath.section)")
        
        tableView.deselectRow(at: indexPath, animated: true)
    }
    
    // section 간격 설정
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat {  return 1 }
    
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
