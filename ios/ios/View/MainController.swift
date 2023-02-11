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
    
    
    let indexBtns = ["전체 사용자 랭킹", "대학교 내부 랭킹", "랭킹 보러가기", "Repository 비교"]
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
    
    // 소속 대학교 이름 label
    lazy var univNameLabel: UILabel = {
       let univName = UILabel()
        univName.text = "한국공학대학교"
        univName.textColor = .black
        univName.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 30)
        return univName
    }()
    
    // 검색 버튼 UI
    lazy var searchUI: UIButton = {
        let searchUI = UIButton()
        searchUI.setTitle("검색화면 이동", for: .normal)
        searchUI.setTitleColor(.black, for: .normal)
        searchUI.addTarget(self, action: #selector(searchUIClicked), for: .touchUpInside)
        return searchUI
    }()
    
    // 내 티어, 토큰 띄우는 UI
    lazy var tierTokenUI: TierTokenCustomUIView = {
        let tierTokenUI = TierTokenCustomUIView()
        tierTokenUI.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)
        tierTokenUI.layer.cornerRadius = 20
        return tierTokenUI
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
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUItoView(){
        self.view.addSubview(collectionView)
        self.view.addSubview(univNameLabel)
        self.view.addSubview(searchUI)
        self.view.addSubview(settingUI)
        self.view.addSubview(tierTokenUI)
        configureCollectionView()
    }
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func settingAutoLayout(){
        
        // 소속 대학교 이름 AutoLayout
        univNameLabel.snp.makeConstraints({ make in
            make.top.equalTo(settingUI.snp.bottom).offset(30)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(searchUI.snp.top).offset(-20)
        })
        
        // 검색 버튼 AutoLayout
        searchUI.snp.makeConstraints({ make in
            make.top.equalTo(univNameLabel.snp.bottom)
            make.leading.equalTo(10)
            make.trailing.equalTo(-10)
        })
        
        // 사용자 이름 버튼 AutoLayout
        settingUI.snp.makeConstraints({ make in
            make.top.equalTo(60)
            make.leading.equalTo(10)
        })
        
        // 내 티어, 토큰 띄우는 UI AutoLayout
        tierTokenUI.snp.makeConstraints({ make in
            make.top.equalTo(searchUI.snp.bottom)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.height.equalTo(deviceHeight/6)
            make.bottom.equalTo(collectionView.snp.top).offset(-10)
        })
        
        
        // CollectionView AutoLayout
        collectionView.snp.makeConstraints({ make in
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
            make.bottom.equalTo(-30)
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
extension MainController: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: MainCollectionView.identifier, for: indexPath) as? MainCollectionView ?? MainCollectionView()
        
        cell.customLabel.text = indexBtns[indexPath.row]
        cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)
        cell.layer.cornerRadius = 20    //테두리 둥글게
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return indexBtns.count
    }
    
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let cellHeight = deviceHeight*24/100
        let cellWidth = collectionView.bounds.width*48/100
        
        return CGSize(width: cellWidth, height: cellHeight)
    }
    
    // cell 선택되었을 때
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        switch indexPath.row{
            
        case 2:
            self.navigationController?.pushViewController(WatchRankingController(), animated: true)
        case 3:
            self.navigationController?.pushViewController(CompareRepositoryController(), animated: true)
        default:
            print("aaa")
        }
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
        MainController().toPreview().previewDevice("iPhone 14 Pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}

