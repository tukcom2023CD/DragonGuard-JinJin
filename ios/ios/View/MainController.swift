//
//  ViewController.swift
//  ios
//
//  Created by 정호진 on 2023/01/03.
//

import UIKit
import SnapKit
import SwiftUI


final class MainController: UIViewController {
    
    
    let indexBtns = ["내 티어 : 루비/n내 토큰 : 28.7T", "전체 사용자 랭킹", "대학교 내부 랭킹", "전국 대학교 랭킹", "랭킹 보러가기", "Repository 비교하기"]
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
    
    // 버튼들 나열할 collectionView
    lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
        cv.backgroundColor = .white
        return cv
    }()
    
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
        
        settingUI.setImage(UIImage(named: "img1")?.resize(newWidth: 50),for: .normal)
        settingUI.imageView?.layer.cornerRadius = 20
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
    // collectionView 설정
    private func configureCollectionView(){
        collectionView.register(MainCollectionView.self, forCellWithReuseIdentifier: MainCollectionView.identifier)
        collectionView.dataSource = self
        collectionView.delegate = self
    }
    
    // 검색 버튼 누르는 경우 네비게이션 뷰 방식으로 이동
    @objc func searchUIClicked(){
        self.navigationItem.backButtonTitle = " "    //다른 화면에서 BackBtn title 설정
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
        self.view.addSubview(collectionView)
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
            make.top.equalTo(60)
            make.leading.equalTo(10)
        })
        
        // 랭킹 보러가기 버튼 AutoLayout
        watchRanking.snp.makeConstraints({ make in
            make.top.equalTo(200)
            make.leading.equalTo(30)
        })
        
    }
    
}

extension UIImage {
    //이미지 크기 재배치 하는 함수
    func resize(newWidth: CGFloat) -> UIImage {
        let scale = newWidth / self.size.width
        let newHeight = self.size.height * scale
        
        let size = CGSize(width: newWidth, height: newHeight)
        let render = UIGraphicsImageRenderer(size: size)
        let renderImage = render.image { context in
            self.draw(in: CGRect(origin: .zero, size: size))
        }
        return renderImage
    }
}

// CollectionView DataSouce, Delegate 설정
extension MainController: UICollectionViewDataSource, UICollectionViewDelegate{
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: MainCollectionView.identifier, for: indexPath) as? MainCollectionView ?? MainCollectionView()
        
        cell.customLabel.text = indexBtns[indexPath.row]
        
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 1
    }
    
    // cell 선택되었을 때
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        
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

