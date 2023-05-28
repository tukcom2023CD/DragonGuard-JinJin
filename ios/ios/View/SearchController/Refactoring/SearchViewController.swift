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
    private var topConstraint: Constraint?
    private var cellHeight: CGFloat = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
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
        btn.setImage(UIImage(systemName: "magnifyingglass"), for: .normal)
        
        btn.backgroundColor = .white
        btn.setTitle(" Repository or User ", for: .normal)
        btn.titleLabel?.font = UIFont.systemFont(ofSize: 20)
        btn.setTitleColor(.lightGray, for: .normal)
        btn.layer.cornerRadius = 18
        btn.layer.shadowOpacity = 0.5
        btn.layer.shadowOffset = CGSize(width: -3, height: 3)
        return btn
    }()
    
    // MARK: 검색된 결과 표시할 tableView
    private lazy var tableView: UITableView = {
        let tableView = UITableView()
        tableView.isScrollEnabled = false
        return tableView
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
        contentView.addSubview(backBtn)
        contentView.addSubview(searchBtn)
        contentView.addSubview(tableView)
        tableView.dataSource = self
        tableView.delegate = self
        tableView.register(SearchViewTableViewCell.self, forCellReuseIdentifier: SearchViewTableViewCell.identifier)
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
            make.top.equalTo(self.scrollView.snp.top)
            make.leading.equalTo(self.scrollView.snp.leading)
            make.trailing.equalTo(self.scrollView.snp.trailing)
            make.bottom.equalTo(self.scrollView.snp.bottom)
            make.width.equalTo(scrollView.snp.width)
//            make.height.equalTo(2000)
        }
        
        // 뒤로가기 버튼
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(contentView.snp.top)
            make.leading.equalTo(contentView.snp.leading)
        }
        
        // 뒷 배경
        backgroundUIView.snp.makeConstraints { make in
            self.topConstraint = make.top.equalTo(contentView.snp.top).constraint
            make.leading.equalTo(contentView.snp.leading)
            make.trailing.equalTo(contentView.snp.trailing)
            make.height.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.height/3)
            
        }
        
        // 검색 버튼
        searchBtn.snp.makeConstraints { make in
            make.height.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.width/10)
            make.width.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.width*35/60)
            make.top.equalTo(backgroundUIView.snp.bottom)
            make.centerX.equalToSuperview()
        }
        
        // 검색 결과 표시할 리스트
        tableView.snp.makeConstraints { make in
            make.top.equalTo(searchBtn.snp.bottom).offset(self.view.safeAreaLayoutGuide.layoutFrame.width/20)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(contentView.snp.bottom)
            make.height.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.height/2)
        }
    }
    
    
    
    
    
}

extension SearchViewController: UITableViewDelegate, UITableViewDataSource {
   
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: SearchViewTableViewCell.identifier,for: indexPath ) as! SearchViewTableViewCell
        cell.layer.cornerRadius = 15
        cell.backgroundColor = .white
        cell.layer.borderWidth = 1
        cell.layer.cornerRadius = 18
        cell.layer.shadowOpacity = 0.5
        cellHeight += cell.frame.height
        cell.layer.shadowOffset = CGSize(width: 0, height: 3)
        cell.inputInfo(title: "asdfdsf", create: "2022", language: "swift")
        return cell
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    func numberOfSections(in tableView: UITableView) -> Int { return 30 }
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat { return 1 }
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " " }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return self.view.safeAreaLayoutGuide.layoutFrame.height/10
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
        scrollView.updateContentSize(cellHeight: self.cellHeight)
        self.contentView.snp.updateConstraints { make in
            make.bottom.equalTo(self.scrollView.snp.bottom)
        }
//        self.tableView.snp.updateConstraints { make in
//            make.bottom.equalTo(self.contentView.snp.bottom)
//        }
//        self.tableView.reloadData()
        
        if scrollView.contentOffset.y > 0 {
            if scrollView.contentOffset.y < 100 {
                topConstraint.update(offset: -scrollView.contentOffset.y)
            }
            else {
                topConstraint.update(offset: -100)
            }
        }
        else {
            topConstraint.update(offset: 0)
        }
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
