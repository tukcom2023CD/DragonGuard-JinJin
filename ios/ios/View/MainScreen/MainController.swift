//
//  ViewController.swift
//  ios
//
//  Created by 정호진 on 2023/01/03.
//

import UIKit
import SnapKit
import RxSwift

final class MainController: UIViewController {
    let indexBtns = ["전체 사용자 랭킹", "대학교 내부 랭킹", "랭킹 보러가기", "Repository 비교"]
    let deviceWidth = UIScreen.main.bounds.width
    let deviceHeight = UIScreen.main.bounds.height
    let viewModel = MainViewModel()
    let disposeBag = DisposeBag()
    let img = UIImageView()
    var id: Int?
    var rank = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.navigationController?.navigationBar.isHidden = true    // navigation bar 삭제
        self.navigationItem.backButtonTitle = "Home"    //다른 화면에서 BackBtn title 설정
        
        // UI view에 적용
        addUItoView()
        
        // UI AutoLayout 적용
        settingAutoLayout()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        
        getMyData() // 내 토큰, 내 티어 데이터 불러오기
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
        searchUI.titleColor(for: .normal)
        searchUI.tintColor = .black
        searchUI.setImage(UIImage(systemName: "magnifyingglass")?.withRenderingMode(.alwaysTemplate), for: .normal)
        searchUI.backgroundColor = .lightGray
        searchUI.setTitle(" Repository or User ", for: .normal)
        searchUI.titleLabel?.font = UIFont.systemFont(ofSize: 20)
        searchUI.setTitleColor(.gray, for: .normal)
        searchUI.addTarget(self, action: #selector(searchUIClicked), for: .touchUpInside)
        searchUI.layer.cornerRadius = 10
        return searchUI
    }()
    
    // 내 티어, 토큰 띄우는 UI
    lazy var tierTokenUI: TierTokenCustomUIView = {
        let tierTokenUI = TierTokenCustomUIView()
        tierTokenUI.backgroundColor  = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
        tierTokenUI.layer.cornerRadius = 20
        
        // 티어, 토큰 개수 입력
        tierTokenUI.inputText(myTier: "SPROUT", tokens: 0)
        return tierTokenUI
    }()
    
    // 유지 이름 버튼 누르면 설정 화면으로 이동
    lazy var settingUI: UIButton = {
        let settingUI = UIButton()
        
        settingUI.setImage(img.image, for: .normal)
        settingUI.imageView?.layer.cornerRadius = 20
        settingUI.setTitle("unknown", for: .normal)
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
        collectionView.register(MainRankingCollectionView.self, forCellWithReuseIdentifier: MainRankingCollectionView.identifier)
    }
    
    // 검색 버튼 누르는 경우 네비게이션 뷰 방식으로 이동
    @objc func searchUIClicked(){
        let searchPage = SearchPageController()
        searchPage.beforePage = "Main"
        self.navigationItem.backButtonTitle = " "    //다른 화면에서 BackBtn title 설정
        self.navigationController?.pushViewController(searchPage, animated: true)
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
            make.trailing.equalTo(-100)
            make.leading.equalTo(100)
        })
        
        // 사용자 이름 버튼 AutoLayout
        settingUI.snp.makeConstraints({ make in
            make.top.equalTo(60)
            make.leading.equalTo(10)
        })
        
        // 내 티어, 토큰 띄우는 UI AutoLayout
        tierTokenUI.snp.makeConstraints({ make in
            make.top.equalTo(searchUI.snp.bottom).offset(10)
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
    
    // 내 티어, 내 토큰 가져오는 함수
    private func getMyData(){
        guard let id = self.id else {return}
        self.viewModel.getMyInformation(id: id)
            .subscribe(onNext: { data in
                self.rank = data.rank
                self.tierTokenUI.inputText(myTier: data.tier, tokens: data.tokenAmount)
                let url = URL(string: data.profileImage)!
                self.img.load(img: self.img, url: url,btn: self.settingUI)
                self.settingUI.setTitle(data.githubId, for: .normal)
                self.collectionView.reloadData()
            })
            .disposed(by: disposeBag)
    }
        
}



// CollectionView DataSouce, Delegate 설정
extension MainController: UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: MainRankingCollectionView.identifier, for: indexPath) as! MainRankingCollectionView
        
        if indexPath.row == 0 {
            cell.labelText(indexBtns[indexPath.row], rankingNum: "\(self.rank)", "상위 0%")
        }
        else if indexPath.row == 1 {
            cell.labelText(indexBtns[indexPath.row], rankingNum: "00", "상위 0%")
        }
        else {
            cell.labelText("", rankingNum: indexBtns[indexPath.row], "")
        }
        
        cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */


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
            self.navigationController?.pushViewController(CompareController(), animated: true)
        default:
            return
        }
    }
    
    
}

// 사용자 github 프로필 로딩
extension UIImageView {
    func load(img: UIImageView, url: URL, btn: UIButton){
        DispatchQueue.global().async {
            if let data = try? Data(contentsOf: url) {
                if let image = UIImage(data: data) {
                    DispatchQueue.main.async {
                        img.image = image
                        btn.setImage(img.image?.resize(newWidth: 50), for: .normal)
                    }
                }
            }
        }
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
struct VCPreViewMain2:PreviewProvider {
    static var previews: some View {
        MainController().toPreview().previewDevice("iPad (10th generation)")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
