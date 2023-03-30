//
//  OrganizationSearchController.swift
//  ios
//
//  Created by 정호진 on 2023/03/30.
//

import Foundation
import UIKit
import SnapKit

final class OrganizationSearchController: UIViewController{
    
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
    
    
    /*
     UI AutoLayout & Add UI
     */
    
    // MARK: Add UI To View
    private func addUIToView(){
        self.navigationItem.titleView = searchBar
        
        setAutoLayout()
    }
    
    // MARK: set UI AutoLayout
    private func setAutoLayout(){
        
    }
    
}
