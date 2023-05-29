//
//  SearchAndFilterController.swift
//  ios
//
//  Created by 정호진 on 2023/05/29.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

final class SearchAndFilterController: UIViewController{
    private var languageFilterList: [String] = []
    private var forksFilterList: [String] = []
    private var starsFilterList: [String] = []
    private var topicsFilterList: [String] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        addUI()
        self.view.backgroundColor = .white
        
        let tapGesture = UITapGestureRecognizer(target: view, action: #selector(view.endEditing))
        view.addGestureRecognizer(tapGesture)
    }
    
    /*
     UI code
     */
    
    // MARK: 검색 버튼
    private lazy var searchBtn: CustomButton = {
        let btn = CustomButton()
        btn.backgroundColor = .white
        btn.layer.cornerRadius = 18
        btn.layer.shadowOpacity = 0.5
        btn.layer.shadowOffset = CGSize(width: -3, height: 3)
        return btn
    }()
    
    
    // MARK: 뒤로가기 버튼
    private lazy var backBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("<", for: .normal)
        btn.setTitleColor(.blue, for: .normal)
        btn.addTarget(self, action: #selector(clickedBackBtn), for: .touchUpInside)
        return btn
    }()
    
    // MARK: 유저 필터링 버튼
    private lazy var userBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("User", for: .normal)
        btn.layer.cornerRadius = 15
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 18
        btn.layer.shadowOpacity = 0.5
        btn.layer.shadowOffset = CGSize(width: -3, height: 3)
        btn.backgroundColor = UIColor(red: 0/255, green: 252/255, blue: 252/255, alpha: 1.0) /* #00fcfc */
        return btn
    }()
    
    // MARK: 레포지토리 필터링 버튼
    private lazy var repositoryBtn: UIButton = {
        let btn = UIButton()
        btn.setTitle("Repository", for: .normal)
        btn.layer.cornerRadius = 15
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 18
        btn.layer.shadowOpacity = 0.5
        btn.layer.shadowOffset = CGSize(width: -3, height: 3)
        btn.backgroundColor = UIColor(red: 0/255, green: 252/255, blue: 252/255, alpha: 1.0) /* #00fcfc */
        return btn
    }()
    
    // MARK: 필터링 버튼 묶는 stackview
    private lazy var horStackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [userBtn, repositoryBtn])
        stack.axis = .horizontal
        stack.distribution = .fillEqually
        stack.spacing = 10
        return stack
    }()
    
    // MARK: 언어 필터링 버튼
    private lazy var languageButton: UIButton = {
        let btn = UIButton()
        btn.setTitle("Language", for: .normal)
        btn.layer.cornerRadius = 15
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 18
        btn.layer.shadowOpacity = 0.5
        btn.layer.shadowOffset = CGSize(width: -3, height: 3)
        btn.backgroundColor = UIColor(red: 0/255, green: 252/255, blue: 252/255, alpha: 1.0) /* #00fcfc */
        btn.addTarget(self, action: #selector(clickedLanguageBtn), for: .touchUpInside)
        return btn
    }()
    
    // MARK: 선택한 언어 CollectionView
    private lazy var languageCollectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let collect = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collect.backgroundColor = .white
        return collect
    }()
    
    // MARK: 포크 필터링 버튼
    private lazy var forksButton: UIButton = {
        let btn = UIButton()
        btn.setTitle("Forks", for: .normal)
        btn.layer.cornerRadius = 15
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 18
        btn.layer.shadowOpacity = 0.5
        btn.layer.shadowOffset = CGSize(width: -3, height: 3)
        btn.backgroundColor = UIColor(red: 0/255, green: 252/255, blue: 252/255, alpha: 1.0) /* #00fcfc */
        btn.addTarget(self, action: #selector(clickedForksBtn), for: .touchUpInside)
        return btn
    }()
    
    // MARK: 선택한 포크 CollectionView
    private lazy var forksCollectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let collect = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collect.backgroundColor = .white
        return collect
    }()
    
    // MARK: 스타 필터링 버튼
    private lazy var starsButton: UIButton = {
        let btn = UIButton()
        btn.setTitle("Stars", for: .normal)
        btn.layer.cornerRadius = 15
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 18
        btn.layer.shadowOpacity = 0.5
        btn.layer.shadowOffset = CGSize(width: -3, height: 3)
        btn.backgroundColor = UIColor(red: 0/255, green: 252/255, blue: 252/255, alpha: 1.0) /* #00fcfc */
        btn.addTarget(self, action: #selector(clickedStarsBtn), for: .touchUpInside)
        return btn
    }()
    
    // MARK: 선택한 스타 CollectionView
    private lazy var starsCollectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let collect = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collect.backgroundColor = .white
        return collect
    }()
    
    // MARK: 토픽 필터링 버튼
    private lazy var topicsButton: UIButton = {
        let btn = UIButton()
        btn.setTitle("Topices", for: .normal)
        btn.layer.cornerRadius = 15
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 18
        btn.layer.shadowOpacity = 0.5
        btn.layer.shadowOffset = CGSize(width: -3, height: 3)
        btn.backgroundColor = UIColor(red: 0/255, green: 252/255, blue: 252/255, alpha: 1.0) /* #00fcfc */
        btn.addTarget(self, action: #selector(clickedTopicsBtn), for: .touchUpInside)
        return btn
    }()
    
    // MARK: 선택한 토픽 CollectionView
    private lazy var topcisCollectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let collect = UICollectionView(frame: .zero, collectionViewLayout: layout)
        collect.backgroundColor = .white
        return collect
    }()
    
    /*
     Add UI and Set AutoLayout
     */
    
    // MARK: Add UI
    private func addUI(){
        self.view.addSubview(backBtn)
        self.view.addSubview(searchBtn)
        self.view.addSubview(horStackView)
        self.view.addSubview(languageButton)
        self.view.addSubview(languageCollectionView)
        self.view.addSubview(forksButton)
        self.view.addSubview(forksCollectionView)
        self.view.addSubview(starsButton)
        self.view.addSubview(starsCollectionView)
        self.view.addSubview(topicsButton)
        self.view.addSubview(topcisCollectionView)
        
        searchBtn.textfield.delegate = self
        languageCollectionView.dataSource = self
        languageCollectionView.delegate = self
        languageCollectionView.register(UICollectionviewCustomCell.self, forCellWithReuseIdentifier: UICollectionviewCustomCell.identifier)
        
        forksCollectionView.dataSource = self
        forksCollectionView.delegate = self
        forksCollectionView.register(UICollectionviewCustomCell.self, forCellWithReuseIdentifier: UICollectionviewCustomCell.identifier)
        
        starsCollectionView.dataSource = self
        starsCollectionView.delegate = self
        starsCollectionView.register(UICollectionviewCustomCell.self, forCellWithReuseIdentifier: UICollectionviewCustomCell.identifier)
        
        topcisCollectionView.dataSource = self
        topcisCollectionView.delegate = self
        topcisCollectionView.register(UICollectionviewCustomCell.self, forCellWithReuseIdentifier: UICollectionviewCustomCell.identifier)
        
        setAutoLayout()
    }
    
    // MARK: Set AutoLayout
    private func setAutoLayout(){
        backBtn.snp.makeConstraints { make in
            make.centerY.equalTo(searchBtn.snp.centerY)
            make.leading.equalTo(self.view.safeAreaLayoutGuide).offset(10)
        }
        
        searchBtn.snp.makeConstraints { make in
            make.top.equalTo(self.view.safeAreaLayoutGuide)
            make.leading.equalTo(self.view.safeAreaLayoutGuide).offset(50)
            make.centerX.equalTo(self.view.snp.centerX)
        }
        
        horStackView.snp.makeConstraints { make in
            make.top.equalTo(searchBtn.snp.bottom).offset(10)
            make.centerX.equalTo(self.view.snp.centerX)
            make.width.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.width/2)
        }
        
        /// 언어 필터링
        languageButton.snp.makeConstraints { make in
            make.top.equalTo(horStackView.snp.bottom).offset(15)
            make.leading.equalTo(self.view.safeAreaLayoutGuide).offset(10)
            make.width.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.width/4)
        }
        
        languageCollectionView.snp.makeConstraints { make in
            make.top.equalTo(languageButton.snp.top)
            make.leading.equalTo(languageButton.snp.trailing).offset(10)
            make.trailing.equalTo(self.view.safeAreaLayoutGuide).offset(-10)
            make.bottom.equalTo(languageButton.snp.bottom)
        }
        
        /// 포크 필터링
        forksButton.snp.makeConstraints { make in
            make.top.equalTo(languageButton.snp.bottom).offset(15)
            make.leading.equalTo(self.view.safeAreaLayoutGuide).offset(10)
            make.width.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.width/4)
        }
        
        forksCollectionView.snp.makeConstraints { make in
            make.top.equalTo(forksButton.snp.top)
            make.leading.equalTo(forksButton.snp.trailing).offset(10)
            make.trailing.equalTo(self.view.safeAreaLayoutGuide).offset(-10)
            make.bottom.equalTo(forksButton.snp.bottom)
        }
        
        /// Star 필터링
        starsButton.snp.makeConstraints { make in
            make.top.equalTo(forksButton.snp.bottom).offset(15)
            make.leading.equalTo(self.view.safeAreaLayoutGuide).offset(10)
            make.width.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.width/4)
        }
        
        starsCollectionView.snp.makeConstraints { make in
            make.top.equalTo(starsButton.snp.top)
            make.leading.equalTo(starsButton.snp.trailing).offset(10)
            make.trailing.equalTo(self.view.safeAreaLayoutGuide).offset(-10)
            make.bottom.equalTo(starsButton.snp.bottom)
        }
        
        /// Topic 필터링
        topicsButton.snp.makeConstraints { make in
            make.top.equalTo(starsButton.snp.bottom).offset(15)
            make.leading.equalTo(self.view.safeAreaLayoutGuide).offset(10)
            make.width.equalTo(self.view.safeAreaLayoutGuide.layoutFrame.width/4)
        }
        
        topcisCollectionView.snp.makeConstraints { make in
            make.top.equalTo(topicsButton.snp.top)
            make.leading.equalTo(topicsButton.snp.trailing).offset(10)
            make.trailing.equalTo(self.view.safeAreaLayoutGuide).offset(-10)
            make.bottom.equalTo(topicsButton.snp.bottom)
        }
        
    }
    
    /*
     Etc
     */
    
    // MARK: 뒤로가기 버튼
    @objc
    private func clickedBackBtn(){
        self.dismiss(animated: true)
    }
    
    // MARK: 언어 선택 버튼
    @objc
    private func clickedLanguageBtn(){
        searchBtn.textfield.resignFirstResponder()
        
    }
    
    // MARK: 포크 선택 버튼
    @objc
    private func clickedForksBtn(){
        searchBtn.textfield.resignFirstResponder()
        actionSheet(title: "Fork 개수를 선택해주세요.", type: "fork")
    }
    
    // MARK: 스타 선택 버튼
    @objc
    private func clickedStarsBtn(){
        searchBtn.textfield.resignFirstResponder()
        actionSheet(title: "Star 개수를 선택해주세요.", type: "star")
    }
    
    // MARK: 토픽 선택 버튼
    @objc
    private func clickedTopicsBtn(){
        searchBtn.textfield.resignFirstResponder()
        actionSheet(title: "Topic 개수를 선택해주세요.", type: "topic")
    }
   
    // MARK: 5가지 고르는 actionSheet
    private func actionSheet(title: String, type: String){
        let actionSheet = UIAlertController(title: title, message: nil, preferredStyle: .actionSheet)
        var list: [String]?
        let starsArray = ["10 미만","50 미만","100 미만","500 미만","500 이상"]
        let forksArray = ["10 미만","50 미만","100 미만","500 미만","500 이상"]
        let topicArray = ["0","1","2","3","4 이상"]
        if type == "star"{
            list = starsArray
        }
        else if type == "fork"{
            list = forksArray
        }
        else{
            list = topicArray
        }
        
        guard let list = list else { return }
        
        list.forEach { msg in
            actionSheet.addAction(UIAlertAction(title: msg,
                                                style: .default,
                                                handler: {(ACTION:UIAlertAction) in
                print(msg)
                
            }))
        }
        
        actionSheet.addAction(UIAlertAction(title: "Cancel",
                                            style: .destructive,
                                            handler: nil))
        
        self.present(actionSheet, animated: true)
    }
    
}


extension SearchAndFilterController: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == languageCollectionView{
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: UICollectionviewCustomCell.identifier, for: indexPath) as? UICollectionviewCustomCell else { return UICollectionViewCell()}
            
            cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)   //셀 배경 색상
            cell.layer.cornerRadius = 15
            
            
            cell.inputText(languageFilterList[indexPath.row])
            return cell
        }
        else if collectionView == forksCollectionView{
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: UICollectionviewCustomCell.identifier, for: indexPath) as? UICollectionviewCustomCell else { return UICollectionViewCell()}
            
            cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)   //셀 배경 색상
            cell.layer.cornerRadius = 15
            cell.inputText(forksFilterList[indexPath.row])
            return cell
        }
        else if collectionView == starsCollectionView{
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: UICollectionviewCustomCell.identifier, for: indexPath) as? UICollectionviewCustomCell else { return UICollectionViewCell()}
            
            cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)   //셀 배경 색상
            cell.layer.cornerRadius = 15
            cell.inputText(starsFilterList[indexPath.row])
            return cell
        }
        else{
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: UICollectionviewCustomCell.identifier, for: indexPath) as? UICollectionviewCustomCell else { return UICollectionViewCell()}
            
            cell.backgroundColor = UIColor(red: 153/255.0, green: 204/255.0, blue: 255/255.0, alpha: 0.4)   //셀 배경 색상
            cell.layer.cornerRadius = 15
            cell.inputText(topicsFilterList[indexPath.row])
            return cell
        }
            
        
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        let cellHeight = collectionView.bounds.height
        let cellWidth = collectionView.bounds.width/4
        
        return CGSize(width: cellWidth, height: cellHeight)
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == languageCollectionView{
            return languageFilterList.count
        }
        else if collectionView == forksCollectionView{
            return forksFilterList.count
        }
        else if collectionView == starsCollectionView{
            return starsFilterList.count
        }
        else{
            return topicsFilterList.count
        }
    }
}

extension SearchAndFilterController: UITextFieldDelegate{
    
    // 입력을 모두 마치고 return 버튼 누른 경우
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    
}


final class UICollectionviewCustomCell: UICollectionViewCell{
    static let identifier = "UICollectionviewCell"
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        add()
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: label
    private lazy var label: UILabel = {
        let label = UILabel()
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 15)
        return label
    }()
    
    // MARK:
    private func add(){
        self.addSubview(label)
        label.snp.makeConstraints { make in
            make.center.equalTo(self.snp.center)
        }
    }
    
    // MARK: 선택한 리스트 생성
    func inputText(_ text: String){
        label.text = text
    }
    
}


import SwiftUI
struct VCPreViewSearchAndFilterController:PreviewProvider {
    static var previews: some View {
        SearchAndFilterController().toPreview().previewDevice("iPhone 14 Pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
struct VCPreViewSearchAndFilterController2:PreviewProvider {
    static var previews: some View {
        SearchAndFilterController().toPreview().previewDevice("iPhone 11")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
