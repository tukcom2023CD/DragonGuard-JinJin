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
    private var forksFilter: String?
    private var starsFilter: String?
    private var topicsFilter: String?
    private var type: String = "Repository"
    private let disposeBag = DisposeBag()
    var resultList: BehaviorSubject<[SearchResultModel]> = BehaviorSubject(value: [])
    var delegate: SendSearchResultList?
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        addUI()
        self.view.backgroundColor = .white
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
        btn.backgroundColor = UIColor(red: 225/255, green: 228/255, blue: 253/255, alpha: 1.0) /* #e1e4fd */
        btn.addTarget(self, action: #selector(clickedUser), for: .touchUpInside)
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
        btn.backgroundColor = UIColor(red: 225/255, green: 228/255, blue: 253/255, alpha: 1.0) /* #e1e4fd */
        btn.addTarget(self, action: #selector(clickedRepository), for: .touchUpInside)
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
        btn.backgroundColor = UIColor(red: 225/255, green: 228/255, blue: 253/255, alpha: 1.0) /* #e1e4fd */
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
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 18
        btn.layer.shadowOpacity = 0.5
        btn.layer.shadowOffset = CGSize(width: -3, height: 3)
        btn.backgroundColor = UIColor(red: 225/255, green: 228/255, blue: 253/255, alpha: 1.0) /* #e1e4fd */
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
        btn.backgroundColor = UIColor(red: 225/255, green: 228/255, blue: 253/255, alpha: 1.0) /* #e1e4fd */
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
        btn.backgroundColor = UIColor(red: 225/255, green: 228/255, blue: 253/255, alpha: 1.0) /* #e1e4fd */
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
        
    }
    
    // MARK: Repository AutoLayout Set
    private func setRepositoryAutoLayout(){
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
    
    // MARK: Repository 버튼
    @objc
    private func clickedRepository(){
        type = "Repository"
        self.userBtn.backgroundColor = UIColor(red: 225/255, green: 228/255, blue: 253/255, alpha: 1.0) /* #e1e4fd */
        self.repositoryBtn.backgroundColor = UIColor(red: 195/255, green: 202/255, blue: 251/255, alpha: 1.0) /* #c3cafb */
        setRepositoryAutoLayout()
        checkUserBtnOrRepositoryBtn(check: false)
    }
    
    // MARK: User 버튼
    @objc
    private func clickedUser(){
        type = "User"
        self.userBtn.backgroundColor = UIColor(red: 195/255, green: 202/255, blue: 251/255, alpha: 1.0) /* #c3cafb */
        self.repositoryBtn.backgroundColor = UIColor(red: 225/255, green: 228/255, blue: 253/255, alpha: 1.0) /* #e1e4fd */
        checkUserBtnOrRepositoryBtn(check: true)
    }
    
    // MARK: User or Repository 확인
    private func checkUserBtnOrRepositoryBtn(check: Bool){
        languageButton.isHidden = check
        languageCollectionView.isHidden = check
        forksButton.isHidden = check
        forksCollectionView.isHidden = check
        starsButton.isHidden = check
        starsCollectionView.isHidden = check
        topicsButton.isHidden = check
        topcisCollectionView.isHidden = check
        
        languageButton.isEnabled = !check
        forksButton.isEnabled = !check
        starsButton.isEnabled = !check
        topicsButton.isEnabled = !check
    }
    
    // MARK: 언어 선택 버튼
    @objc
    private func clickedLanguageBtn(){
        searchBtn.textfield.resignFirstResponder()
        
        let nc = ChooseLanguageView()
        nc.selectLanguage = self.languageFilterList
        if let sheet = nc.sheetPresentationController{
            sheet.detents = [.medium(),.large()]
            sheet.prefersGrabberVisible = true
            sheet.prefersScrollingExpandsWhenScrolledToEdge = false
        }
        
        nc.selectedLanguage.subscribe(onNext: { data in
            
            self.languageFilterList = data
            if !self.languageFilterList.isEmpty{
                self.languageButton.backgroundColor = UIColor(red: 195/255, green: 202/255, blue: 251/255, alpha: 1.0) /* #c3cafb */
            }
            self.languageCollectionView.reloadData()
        })
        .disposed(by: self.disposeBag)
        
        self.present(nc,animated: true)
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
                if type == "star"{
                    self.starsFilter = ""
                    self.starsFilter?.append(msg)
                    if !(self.starsFilter?.isEmpty ?? true){
                        self.starsButton.backgroundColor = UIColor(red: 195/255, green: 202/255, blue: 251/255, alpha: 1.0) /* #c3cafb */
                    }
                    self.starsCollectionView.reloadData()
                }
                else if type == "fork"{
                    self.forksFilter = ""
                    self.forksFilter?.append(msg)
                    if !(self.forksFilter?.isEmpty ?? true){
                        self.forksButton.backgroundColor = UIColor(red: 195/255, green: 202/255, blue: 251/255, alpha: 1.0) /* #c3cafb */
                    }
                    self.forksCollectionView.reloadData()
                }
                else{
                    self.topicsFilter = ""
                    self.topicsFilter?.append(msg)
                    if !(self.topicsFilter?.isEmpty ?? true){
                        self.topicsButton.backgroundColor = UIColor(red: 195/255, green: 202/255, blue: 251/255, alpha: 1.0) /* #c3cafb */
                    }
                    self.topcisCollectionView.reloadData()
                }
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
            
            cell.layer.cornerRadius = 15
            cell.backgroundColor = UIColor(red: 0/255, green: 252/255, blue: 252/255, alpha: 1.0) /* #00fcfc */
            
            cell.inputText(languageFilterList[indexPath.row])
            return cell
        }
        else if collectionView == forksCollectionView{
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: UICollectionviewCustomCell.identifier, for: indexPath) as? UICollectionviewCustomCell else { return UICollectionViewCell()}
            cell.layer.cornerRadius = 15
            cell.backgroundColor = UIColor(red: 0/255, green: 252/255, blue: 252/255, alpha: 1.0) /* #00fcfc */
            if let fork = forksFilter  {
                cell.inputText(fork)
            }
            return cell
        }
        else if collectionView == starsCollectionView{
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: UICollectionviewCustomCell.identifier, for: indexPath) as? UICollectionviewCustomCell else { return UICollectionViewCell()}
            
            cell.layer.cornerRadius = 15
            cell.backgroundColor = UIColor(red: 0/255, green: 252/255, blue: 252/255, alpha: 1.0) /* #00fcfc */
            
            if let star = starsFilter{
                cell.inputText(star)
            }
            return cell
        }
        else{
            guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: UICollectionviewCustomCell.identifier, for: indexPath) as? UICollectionviewCustomCell else { return UICollectionViewCell()}
            
            cell.layer.cornerRadius = 15
            cell.backgroundColor = UIColor(red: 0/255, green: 252/255, blue: 252/255, alpha: 1.0) /* #00fcfc */
            
            if let topic = topicsFilter{
                cell.inputText(topic)
            }
            return cell
        }
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        collectionView.deselectItem(at: indexPath, animated: true)
        
        if collectionView == languageCollectionView{
            let languageIndex = self.languageFilterList.firstIndex(of: self.languageFilterList[indexPath.row])
            self.languageFilterList.remove(at: languageIndex ?? -1)
            
            if languageFilterList.isEmpty{
                self.languageButton.backgroundColor = UIColor(red: 225/255, green: 228/255, blue: 253/255, alpha: 1.0) /* #e1e4fd */
            }
            
            self.languageCollectionView.reloadData()
        }
        else if collectionView == forksCollectionView{
            self.forksFilter = ""
            if self.forksFilter?.isEmpty ?? true{
                self.forksButton.backgroundColor = UIColor(red: 225/255, green: 228/255, blue: 253/255, alpha: 1.0) /* #e1e4fd */
            }
            self.forksCollectionView.reloadData()
        }
        else if collectionView == starsCollectionView{
            self.starsFilter = ""
            if self.starsFilter?.isEmpty ?? true{
                self.starsButton.backgroundColor = UIColor(red: 225/255, green: 228/255, blue: 253/255, alpha: 1.0) /* #e1e4fd */
            }
            
            self.starsCollectionView.reloadData()
        }
        else{
            self.topicsFilter = ""
            if self.topicsFilter?.isEmpty ?? true{
                self.topicsButton.backgroundColor = UIColor(red: 225/255, green: 228/255, blue: 253/255, alpha: 1.0) /* #e1e4fd */
            }
            self.topcisCollectionView.reloadData()
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
            if self.forksFilter?.isEmpty ?? true{
                return 0
            }
            return 1
        }
        else if collectionView == starsCollectionView{
            if self.starsFilter?.isEmpty ?? true{
                return 0
            }
            return 1
        }
        else{
            if self.topicsFilter?.isEmpty ?? true{
                return 0
            }
            return 1
        }
    }
}

extension SearchAndFilterController: UITextFieldDelegate{
    
    // 입력을 모두 마치고 return 버튼 누른 경우
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        guard let word = textField.text else { return false}
        print("seach \(word)")
        let filtering = getFiltering()
        
        let list: [SearchResultModel] = [
            SearchResultModel(create: "2022", language: "swift", title: "aaa"),
            SearchResultModel(create: "2022", language: "swift", title: "aaa"),
            SearchResultModel(create: "2022", language: "swift", title: "aaa"),
            SearchResultModel(create: "2022", language: "swift", title: "aaa"),
            SearchResultModel(create: "2022", language: "swift", title: "aaa"),
            SearchResultModel(create: "2022", language: "swift", title: "aaa"),
        ]
        
        self.delegate?.sendList(list: list)
        self.dismiss(animated: true)
        
//        SearchPageViewModel.viewModel.getSearchData(searchWord: word, type: self.type, change: true, filtering: filtering)
//            .subscribe(onNext: { list in
//                if !list.isEmpty{
//                    self.delegate?.sendList(list: list)
//                    self.dismiss(animated: true)
//                }
//            })
//            .disposed(by: self.disposeBag)
        
        textField.resignFirstResponder()
        return true
    }
    
    // MARK:
    private func getFiltering() -> String{
        var filtering: String = ""
        
        if !languageFilterList.isEmpty{
            self.languageFilterList.forEach { lang in
                filtering.append("language:\(lang),")
            }
        }
        
        if let starsFilter = starsFilter{
            if !starsFilter.isEmpty{
                if starsFilter == "10 미만"{
                    filtering.append("star:0..9,")
                }
                else if starsFilter == "50 미만"{
                    filtering.append("star:10..49,")
                }
                else if starsFilter == "100 미만"{
                    filtering.append("star:50..99,")
                }
                else if starsFilter == "500 미만"{
                    filtering.append("star:100..499,")
                }
                else if starsFilter == "500 이상"{
                    filtering.append("star:>=500,")
                }
            }
        }
        
        if let forksFilter = forksFilter {
            if !forksFilter.isEmpty{
                if forksFilter == "10 미만"{
                    filtering.append("fork:0..9,")
                }
                else if forksFilter == "50 미만"{
                    filtering.append("fork:10..49,")
                }
                else if forksFilter == "100 미만"{
                    filtering.append("fork:50..99,")
                }
                else if forksFilter == "500 미만"{
                    filtering.append("fork:100..499,")
                }
                else if forksFilter == "500 이상"{
                    filtering.append("fork:>=500,")
                }
            }
        }
        
        if let topicsFilter = topicsFilter {
            if !topicsFilter.isEmpty{
                if topicsFilter == "0"{
                    filtering.append("topic:0,")
                }
                else if topicsFilter == "1"{
                    filtering.append("topic:1,")
                }
                else if topicsFilter == "2"{
                    filtering.append("topic:2,")
                }
                else if topicsFilter == "3"{
                    filtering.append("topic:3,")
                }
                else if topicsFilter == "4 이상"{
                    filtering.append("topic:>3,")
                }
            }
        }
        
        if !(filtering.isEmpty){
            if filtering.last == ","{
                filtering.removeLast()
            }
        }
        
        print(filtering)
        return filtering
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

protocol SendSearchResultList{
    func sendList(list: [SearchResultModel])
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
