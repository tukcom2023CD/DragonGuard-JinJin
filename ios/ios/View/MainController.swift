//
//  ViewController.swift
//  ios
//
//  Created by 정호진 on 2023/01/03.
//

import UIKit
import SnapKit


final class MainController: UIViewController {

    let deviceWidth = UIScreen.main.bounds.width
    let deviceHeight = UIScreen.main.bounds.height
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.navigationController?.navigationBar.isHidden = true    // navigation bar 삭제
        self.navigationItem.backButtonTitle = "Home"    //다른 화면에서 BackBtn title 설정
        
        // UI view에 적용
        addUItoView()
        
        // UI AutoLayout 적용
        settingAutoLayout()
        
//        // 폰트 체크 하기
//        UIFont.familyNames.sorted().forEach { familyName in
//            print("*** \(familyName) ***")
//            UIFont.fontNames(forFamilyName: familyName).forEach { fontName in
//                print("\(fontName)")
//            }
//            print("---------------------")
//        }
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        self.navigationController?.navigationBar.isHidden = true // navigation bar 삭제
    }
    
    /*
     UI 코드 작성
     */
    
    // 검색 버튼 UI
    lazy var searchUI: UIButton = {
        let searchUI = UIButton()
        searchUI.setTitle("검색화면 이동", for: .normal)
        searchUI.setTitleColor(.black, for: .normal)
        searchUI.addTarget(self, action: #selector(searchUIClicked), for: .touchUpInside)
        return searchUI
    }()
    
    // 유지 이름 버튼 누르면 설정 화면으로 이동
    lazy var settingUI: UIButton = {
        let settingUI = UIButton()
        settingUI.setTitle("DragonGuard-JinJin", for: .normal)
        settingUI.setTitleColor(.black, for: .normal)
        settingUI.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        settingUI.addTarget(self, action: #selector(settingUIClicked), for: .touchUpInside)
        return settingUI
    }()
    
    lazy var watchRanking: UIButton = {
        let watchRanking = UIButton()
        watchRanking.setTitle("랭킹 보러가기", for: .normal)
        watchRanking.setTitleColor(.black, for: .normal)
        watchRanking.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        watchRanking.addTarget(self, action: #selector(watchRankingClicked), for: .touchUpInside)
        return watchRanking
    }()
    /*
     UI Action 작성
     */
    
    
    // 검색 버튼 누르는 경우 네비게이션 뷰 방식으로 이동
    @objc func searchUIClicked(){
        self.navigationController?.pushViewController(SearchPageController(), animated: true)
    }
    
    // 유저 이름 누르는 경우 네비게이션 뷰 방식으로 이동
    @objc func settingUIClicked(){
        self.navigationController?.pushViewController(SettingController(), animated: true)
    }
    
    // 랭킹 보러가기 누른 경우 네비게이션 뷰 방식으로 이동
    @objc func watchRankingClicked(){
        self.navigationController?.pushViewController(WatchRankingController(), animated: true)
    }
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUItoView(){
        self.view.addSubview(searchUI)
        self.view.addSubview(settingUI)
        self.view.addSubview(watchRanking)
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func settingAutoLayout(){
        
        // 검색 버튼 AutoLayout
        searchUI.snp.makeConstraints({ make in
            make.top.equalTo(100)
            make.leading.equalTo(10)
            make.trailing.equalTo(-10)
        })
        
        // 사용자 이름 버튼 AutoLayout
        settingUI.snp.makeConstraints({ make in
            make.top.equalTo(50)
            make.leading.equalTo(10)
        })
        
        // 랭킹 보러가기 버튼 AutoLayout
        watchRanking.snp.makeConstraints({ make in
            make.top.equalTo(200)
            make.leading.equalTo(30)
        })
        
    }

}






/*
    SwiftUI preview 사용하는 코드
 
    preview 실행이 안되는 경우 단축키
    Command + Option + Enter : preview 그리는 캠버스 띄우기
    Command + Option + p : preview 재실행
 */

import SwiftUI

#if DEBUG
extension UIViewController {
    private struct Preview: UIViewControllerRepresentable {
            let viewController: UIViewController

            func makeUIViewController(context: Context) -> UIViewController {
                return viewController
            }

            func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
            }
        }

        func toPreview() -> some View {
            Preview(viewController: self)
        }
}
#endif


struct VCPreViewMain:PreviewProvider {
    static var previews: some View {
        MainController().toPreview().previewDevice("iPhone 14 pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
