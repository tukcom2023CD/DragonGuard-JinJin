//
//  CompareViewController.swift
//  ios
//
//  Created by 정호진 on 2023/06/19.
//

import Foundation
import UIKit
import SnapKit
import RxSwift
import Lottie

final class CompareRepoUserController: UIViewController{
    private let nameList: [String] = ["forks", "closed issues", "open issues", "stars", "contributers", "deletions average", "languages", "code average"]
    private let selectionList: [String] = ["Repository", "User"]
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        
        addUIIndicator()
        addUIBase()
        getData()
        
    }
    
    // MARK: 로딩 UI
    private lazy var indicatorView: LottieAnimationView = {
        let view = LottieAnimationView(name: "graphLottie")
        view.center = self.view.center
        view.loopMode = .loop
        return view
    }()
    
    
    // MARK: 뒤로가기 버튼
    private lazy var backBtn: UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(named: "backBtn")?.resize(newWidth: 30), for: .normal)
        return btn
    }()
    
    // MARK: Repository User 선택하는 화면
    private lazy var selectionCollectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
        
        return cv
    }()
    
    // MARK: 첫 번째 레포 정보
    private lazy var leftView: CustomUIView = {
        let view = CustomUIView()
        
        return view
    }()
    
    // MARK: 두 번째 레포 정보
    private lazy var rightView: CustomUIView = {
        let view = CustomUIView()
        
        return view
    }()
    
    // MARK: 세로 줄
    private lazy var lineView: UIView = {
        let view = UIView()
        view.backgroundColor = .black
        return view
    }()
    
    // MARK: 스크롤 뷰
    private lazy var scrollView: UIScrollView = {
        let scroll = UIScrollView()
        return scroll
    }()
    
    // MARK: contentView
    private lazy var contentView: UIView = {
        let view = UIView()
        
        return view
    }()
    
    // MARK: repository info
    private lazy var tableView: UITableView = {
        let table = UITableView()
        table.separatorStyle = .none
        table.isScrollEnabled = false
        table.layer.shadowOffset = CGSize(width: 3, height: 3)
        table.layer.cornerRadius = 20
        table.layer.shadowOpacity = 0.5
        table.clipsToBounds = true
        return table
    }()
    
    /*
     Add UI
     */
    
    // MARK: Add UI Indicator
    private func addUIIndicator(){
        self.view.addSubview(indicatorView)
        setIndicactorAutoLayout()
        indicatorView.play()
    }
    
    // MARK: Indicator View AutoLayout
    private func setIndicactorAutoLayout(){
        indicatorView.snp.makeConstraints { make in
            make.leading.equalTo(self.view.safeAreaLayoutGuide).offset(40)
            make.trailing.equalTo(self.view.safeAreaLayoutGuide).offset(-40)
            make.centerY.equalToSuperview()
        }
    }
    
    // MARK:
    private func addUIBase(){
        view.addSubview(backBtn)
        view.addSubview(selectionCollectionView)
        selectionCollectionView.dataSource = self
        selectionCollectionView.delegate = self
        selectionCollectionView.register(CompareCollectionViewCell.self, forCellWithReuseIdentifier: CompareCollectionViewCell.identfier)
        
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
        }
        
        selectionCollectionView.snp.makeConstraints { make in
            make.top.equalTo(backBtn.snp.bottom).offset(10)
            make.leading.trailing.equalToSuperview()
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/10)
        }
        clickedBtn()
    }
    
    // MARK: Add UI
    private func addUI(){
        view.addSubview(scrollView)
        scrollView.addSubview(contentView)
        contentView.addSubview(leftView)
        contentView.addSubview(rightView)
        contentView.addSubview(lineView)
        contentView.addSubview(tableView)
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.register(CompareTableViewCell.self, forCellReuseIdentifier: CompareTableViewCell.identfier)
        setAutoLayout()
    }
    
    // MARK: Setting AutoLayout
    private func setAutoLayout(){
        scrollView.snp.makeConstraints { make in
            make.top.equalTo(selectionCollectionView.snp.bottom).offset(10)
            make.leading.trailing.bottom.equalTo(view.safeAreaLayoutGuide)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        }
        
        contentView.snp.makeConstraints { make in
            make.top.equalTo(scrollView.snp.top)
            make.leading.equalTo(scrollView.snp.leading)
            make.trailing.equalTo(scrollView.snp.trailing)
            make.bottom.equalTo(scrollView.snp.bottom)
            make.width.equalTo(scrollView.snp.width)
        }
        
        leftView.snp.makeConstraints { make in
            make.top.equalTo(contentView.snp.top).offset(10)
            make.leading.equalTo(contentView.snp.leading)
            make.bottom.equalTo(lineView.snp.bottom)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/2-1)
        }
        
        lineView.snp.makeConstraints { make in
            make.top.equalTo(leftView.snp.top).offset(10)
            make.leading.equalTo(leftView.snp.trailing)
            make.width.equalTo(2)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height*3/5)
        }
        
        rightView.snp.makeConstraints { make in
            make.top.equalTo(contentView.snp.top).offset(10)
            make.trailing.equalTo(contentView.snp.trailing)
            make.bottom.equalTo(lineView.snp.bottom)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/2-1)
        }
        
        tableView.snp.makeConstraints { make in
            make.top.equalTo(lineView.snp.bottom).offset(10)
            make.leading.equalTo(contentView.snp.leading).offset(30)
            make.trailing.equalTo(contentView.snp.trailing).offset(-30)
            make.bottom.equalTo(contentView.snp.bottom).offset(-20)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height*8/14)
        }
        
        
    }
    
    // MARK:
    private func getData(){
        addUI()
        
        leftView.inputData(repo1: [], values: nil, repoName: "abc", imgList: [])
        rightView.inputData(repo1: [], values: nil, repoName: "qwer", imgList: [])
    }
    
    // MARK:
    private func clickedBtn(){
        backBtn.rx.tap.subscribe(onNext: {
            self.dismiss(animated: false)
        })
        .disposed(by: disposeBag)
    }
    
    
}

extension CompareRepoUserController: UITableViewDelegate, UITableViewDataSource{
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: CompareTableViewCell.identfier, for: indexPath) as? CompareTableViewCell else { return UITableViewCell() }
        cell.inputData(repo1: 1, title: nameList[indexPath.section], repo2: 2)
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat { return view.safeAreaLayoutGuide.layoutFrame.height/20 }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return 1 }
    func numberOfSections(in tableView: UITableView) -> Int { return nameList.count }
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? { return " " }
    func tableView(_ tableView: UITableView, heightForHeaderInSection section: Int) -> CGFloat { return 1 }
}


extension CompareRepoUserController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: CompareCollectionViewCell.identfier, for: indexPath) as? CompareCollectionViewCell else { return UICollectionViewCell() }
        cell.inputData(text: selectionList[indexPath.row])
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int { return selectionList.count }
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize { return CGSize(width: collectionView.frame.width/2, height: collectionView.frame.height) }
    
    
}
