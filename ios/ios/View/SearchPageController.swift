//
//  SearchPageController.swift
//  ios
//
//  Created by 정호진 on 2023/01/25.
//

import UIKit
import SnapKit

// 검색창
class SearchPageController: UIViewController{
    
    let deviceWidth = UIScreen.main.bounds.width    // 각 장치들의 가로 길이
    let deviceHeight = UIScreen.main.bounds.height  // 각 장치들의 세로 길이
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.view.backgroundColor = .white
        
        addUItoView()   //View에 적용할 UI 작성

        
        searchUISetLayout()     // searchUI AutoLayout 함수
    }
    
    /*
     UI 작성
     */
    
    lazy var searchUI: UISearchBar = {
        let searchBar = UISearchBar(frame: CGRect(x: 0, y: 0, width: deviceWidth, height: 0))
        return searchBar
    }()

    /*
     UI Action 작성
     
     */
    
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    //View에 적용할 때 사용하는 함수
    private func addUItoView(){
        self.view.addSubview(searchUI)  //searchUI 적용
        
    }
    
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것 (추천)
     */
    
    private func searchUISetLayout(){
        searchUI.snp.makeConstraints({ make in
            make.top.equalTo(100)
            make.leading.equalTo(10)
            make.trailing.equalTo(-10)
        })
    }
    
    
   
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
    }
}

struct VCPreView1:PreviewProvider {
    static var previews: some View {
        SearchPageController().toPreview().previewDevice("iPhone 11")
    }
}

struct VCPreView2:PreviewProvider {
    static var previews: some View {
        SearchPageController().toPreview().previewDevice("iPad (10th generation)")
    }
}
