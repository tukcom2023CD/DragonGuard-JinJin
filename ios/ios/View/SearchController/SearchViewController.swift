//
//  SearchViewController.swift
//  ios
//
//  Created by 정호진 on 2023/05/29.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

final class SearchViewController: UIViewController{
    private var topConstraint: Constraint?  // background View
    private var searchBarTopConstraint: Constraint?
    private var resultList: [SearchResultModel] = []
    private let disposeBag = DisposeBag()
    var beforePage: String? = "Main"// 이전 페이지 확인하는 변수
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        addUIToView()
    }
    
    /*
     UI 작성
     */
    
    // MARK: 뒤로가기 버튼
    private lazy var backBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("<", for: .normal)
        btn.setTitleColor(.blue, for: .normal)
        btn.addTarget(self, action: #selector(clickedBackBtn), for: .touchUpInside)
        return btn
    }()
    
    // MARK: 뒷 배경
    private lazy var backgroundUIView: UIView = {
        let view = BackgroundUIView()
        view.setImg(imgName: "3")
        return view
    }()
    
    // MARK: 검색 버튼
    private lazy var searchBtn: UIButton = {
        let btn = UIButton()
        btn.backgroundColor = .white
        btn.setImage(UIImage(systemName: "magnifyingglass"), for: .normal)
        btn.setTitle(" Repository or User ", for: .normal)
        btn.titleLabel?.font = UIFont.systemFont(ofSize: 20)
        btn.setTitleColor(.lightGray, for: .normal)
        btn.layer.cornerRadius = 18
        btn.layer.shadowOpacity = 0.5
        btn.layer.shadowOffset = CGSize(width: -3, height: 3)
        btn.addTarget(self, action: #selector(clickedSearchBtn), for: .touchUpInside)
        return btn
    }()
    
    // MARK:
    private lazy var searchBar: UISearchBar = {
        let search = UISearchBar()
        search.searchTextField.layer.cornerRadius = 20
        search.searchTextField.layer.masksToBounds = true
        search.searchTextField.layer.shadowOpacity = 0.5
        search.backgroundColor = .clear
        search.placeholder = "Repository or User"
        search.searchBarStyle = .minimal
        search.tag = -10
        search.searchTextField.layer.shadowOffset = CGSize(width: -3, height: 3)
        return search
    }()
    
    // MARK: 검색 버튼 고정할 때 사용할 UIView
    private lazy var searchBtnUIView: UIView = {
        let view = UIView()
        return view
    }()
    
    // MARK: 고정된 뒤로가기 버튼
    private lazy var back2Btn: UIButton = {
        let btn = UIButton()
        btn.setTitle("<", for: .normal)
        btn.isHidden = true
        btn.setTitleColor(.blue, for: .normal)
        btn.addTarget(self, action: #selector(clickedBackBtn), for: .touchUpInside)
        return btn
    }()
    
    // MARK: tableView 역할
    private lazy var stackView: UIStackView = {
        let stack = UIStackView()
        stack.axis = .vertical
        stack.distribution = .fillEqually
        stack.spacing = 10
        stack.backgroundColor = .white
        return stack
    }()
    
    // MARK: scrollView
    private lazy var scrollView: UIScrollView = {
        let scroll = UIScrollView()
        
        return scroll
    }()
    
    // MARK: contentview
    private lazy var contentView: UIView = {
        let view = UIView()
        return view
    }()
    
    /*
     Add UI & Set AutoLayout
     */
    
    // MARK: UI 등록
    private func addUIToView(){
        self.view.addSubview(scrollView)
        
        scrollView.addSubview(contentView)
        scrollView.delegate = self
        
        contentView.addSubview(backgroundUIView)
        contentView.addSubview(stackView)

        contentView.addSubview(searchBtnUIView)
        contentView.addSubview(backBtn)
        searchBtnUIView.addSubview(searchBtn)
        searchBtnUIView.addSubview(back2Btn)
        
        setAutoLayout()
    }
    
    // MARK: setting AutoLayout
    private func setAutoLayout(){
        
        // ScrollView
        scrollView.snp.makeConstraints { make in
            make.top.equalTo(self.view.safeAreaLayoutGuide)
            make.leading.equalTo(self.view.safeAreaLayoutGuide)
            make.trailing.equalTo(self.view.safeAreaLayoutGuide)
            make.bottom.equalTo(self.view.safeAreaLayoutGuide)
        }
        
        // contentView
        contentView.snp.makeConstraints { make in
            self.topConstraint = make.top.equalTo(self.scrollView.snp.top).constraint
            make.leading.equalTo(self.scrollView.snp.leading)
            make.trailing.equalTo(self.scrollView.snp.trailing)
            make.bottom.equalTo(self.scrollView.snp.bottom)
            make.width.equalTo(scrollView.snp.width)
        }
        
        // 뒤로가기 버튼
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(contentView.snp.top)
            make.leading.equalTo(contentView.snp.leading)
        }
        
        // 뒷 배경
        backgroundUIView.snp.makeConstraints { make in
            make.top.equalTo(contentView.snp.top)
            make.leading.equalTo(contentView.snp.leading)
            make.trailing.equalTo(contentView.snp.trailing)
            make.height.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.height/3)
        }
        
        // 검색버튼 고정시 사용
        searchBtnUIView.snp.makeConstraints { make in
            make.height.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.width/10)
            make.width.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.width)
            searchBarTopConstraint = make.top.equalTo(backgroundUIView.snp.bottom).offset(self.view.safeAreaLayoutGuide.layoutFrame.width/20).constraint
            make.centerX.equalToSuperview()
        }
        
        // 검색 버튼
        searchBtn.snp.makeConstraints { make in
            make.height.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.width/10)
            make.width.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.width*35/60)
            make.center.equalToSuperview()
        }
        
        // 고정 뒤로가기 버튼
        back2Btn.snp.makeConstraints { make in
            make.top.equalTo(searchBtnUIView.snp.top).offset(10)
            make.leading.equalTo(searchBtnUIView.snp.leading)
        }
        
        stackView.snp.makeConstraints { make in
            make.top.equalTo(backgroundUIView.snp.bottom).offset(self.view.safeAreaLayoutGuide.layoutFrame.width/5)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(contentView.snp.bottom)
        }
        
        inputDataIntoList()
    }
    
    // MARK: 데이터 삽입
    private func inputDataIntoList(){
  
        stackView.arrangedSubviews.forEach { view in
            view.removeFromSuperview()
        }
        
        var index = 0
        resultList.forEach { result in
            
            let resultUI = SearchResultUIVIew()
            resultUI.snp.makeConstraints { make in
                make.height.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.height/10)
            }
            
            let tapGesture = UITapGestureRecognizer(target: self, action: #selector(handleTap(sender:)))
            resultUI.tag = index
            index += 1
            resultUI.isUserInteractionEnabled = true
            
            resultUI.addGestureRecognizer(tapGesture)
            resultUI.inputInfo(title: result.name,
                               create: result.createdAt,
                               language: result.language)
            stackView.addArrangedSubview(resultUI)
        }
    }
    
    // MARK: 검색된 리스트 누른 경우
    @objc
    private func handleTap(sender: UITapGestureRecognizer) {
        
        /*
         리스트 누르는 경우 코드 처리 해야 함
         
         */
        let comparePage = CompareController()
        
        let nextPage = RepoDetailController()
        nextPage.selectedTitle = resultList[sender.view?.tag ?? -1].name
        nextPage.modalPresentationStyle = .fullScreen
        self.present(nextPage,animated: true)
        
        if beforePage == "Main"{    // 레포 상세조회로 이동
            let nextPage = RepoDetailController()
            nextPage.selectedTitle = resultList[sender.view?.tag ?? -1].name
            nextPage.modalPresentationStyle = .fullScreen
            self.present(nextPage,animated: true)
        }
        else if beforePage == "Compare1"{
            comparePage.repository1 = resultList[sender.view?.tag ?? -1].name
            NotificationCenter.default.post(name: Notification.Name.data, object: nil,userInfo: [NotificationKey.choiceId: 1, NotificationKey.repository: resultList[sender.view?.tag ?? -1].name])
            resultList = []
            self.dismiss(animated: true)
        }
        else if beforePage == "Compare2"{
            comparePage.repository2 = resultList[sender.view?.tag ?? -1].name
            NotificationCenter.default.post(name: Notification.Name.data, object: nil,userInfo: [NotificationKey.choiceId: 2, NotificationKey.repository: resultList[sender.view?.tag ?? -1].name])
            resultList = []
            self.dismiss(animated: true)
        }
        
        
        print(sender.view?.tag ?? -1)
    }
        
    // MARK: 검색 버튼 누른 경우
    @objc
    private func clickedSearchBtn(){
        let nextPage = SearchAndFilterController()
        nextPage.modalPresentationStyle = .fullScreen
        nextPage.delegate = self
        
        nextPage.resultList.subscribe(onNext: { list in
            self.resultList = list
        })
        .disposed(by: self.disposeBag)
        
        self.present(nextPage,animated: true)
    }
    
    // MARK: 뒤로가기 버튼 누르는 경우
    @objc
    private func clickedBackBtn(){
        
        self.dismiss(animated: true)
    }
    
    // MARK: 테스트용 함수
    private func testData(){
//        for _ in 0...10{
//            resultList.append(SearchResultModel(create: "2022", language: "swift", title: "hi"))
//        }
    }
    
}

extension UIScrollView {
    
    func updateContentSize(cellHeight: CGFloat) {
        let unionCalculatedTotalRect = recursiveUnionInDepthFor(view: self)
        
        // 계산된 크기로 컨텐츠 사이즈 설정
        self.contentSize = CGSize(width: self.frame.width, height: unionCalculatedTotalRect.height)
    }
    
    private func recursiveUnionInDepthFor(view: UIView) -> CGRect {
        var totalRect: CGRect = .zero
        
        // 모든 자식 View의 컨트롤의 크기를 재귀적으로 호출하며 최종 영역의 크기를 설정
        for subView in view.subviews {
            totalRect = totalRect.union(recursiveUnionInDepthFor(view: subView))
        }
        
        // 최종 계산 영역의 크기를 반환
        return totalRect.union(view.frame)
    }
}

// 스크롤 될 때 뷰 올라가게 하기
extension SearchViewController: UIScrollViewDelegate {
    
    func scrollViewDidScroll(_ scrollView: UIScrollView) {
        guard let topConstraint = topConstraint else {return}
        guard let searchBarTopConstraint = searchBarTopConstraint else {return}
        
        // 스크롤 시 뒷 배경 점점 사라지는 효과
        let percent =  1 - (scrollView.contentOffset.y / (self.backgroundUIView.frame.height))
        if !percent.isFinite || percent.constraintPriorityTargetValue.isNaN{
            self.backgroundUIView.alpha = 1
        }
        else{
            self.backgroundUIView.alpha = percent
        }
        
        // 스크롤 할때 검색 버튼이 고정되도록 하는 부분
        if scrollView.contentOffset.y > 0 {
            if scrollView.contentOffset.y > self.backgroundUIView.frame.height {
                self.searchBtnUIView.backgroundColor = .white
                back2Btn.isHidden = false
                searchBarTopConstraint.update(offset: scrollView.contentOffset.y - self.backgroundUIView.frame.height)
            }
            
        }
        else {
            topConstraint.update(offset: 0)
            back2Btn.isHidden = true
            self.searchBtnUIView.backgroundColor = .clear
        }
        
    }
}

extension SearchViewController: SendSearchResultList{
    func sendList(list: [SearchResultModel]) {
//        print(list)
        resultList = []
        self.resultList = list
        inputDataIntoList()
    }
}


import SwiftUI
struct VCPreViewSearchViewController:PreviewProvider {
    static var previews: some View {
        SearchViewController().toPreview().previewDevice("iPhone 14 Pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
struct VCPreViewSearchViewController2:PreviewProvider {
    static var previews: some View {
        SearchViewController().toPreview().previewDevice("iPhone 11")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
