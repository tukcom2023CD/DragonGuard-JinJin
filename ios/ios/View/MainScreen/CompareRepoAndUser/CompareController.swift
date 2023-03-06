//
//  CompareRepositorycontroller.swift
//  ios
//
//  Created by 홍길동 on 2023/02/06.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

// Repository 비교
final class CompareController: UIViewController{
    let deviceHeight = UIScreen.main.bounds.height
    let searchPage = SearchPageController()
    var repository1: String = ""
    var repository2: String = ""
    let disposeBag = DisposeBag()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.isHidden = false
        self.view.backgroundColor = .white
        self.navigationItem.backButtonTitle = ""
        
        addToView()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        NotificationCenter.default.addObserver(self, selector: #selector(notificationData(notification:)), name: Notification.Name.data, object: nil)
    }
    /*
     UI 코드 작성
     */
    
    lazy var searchLabel: UILabel = {
        let label = UILabel()
        label.text = "레포지토리 선택하기"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 30)
        label.textColor = .black
        return label
    }()
    
    lazy var searchBtn1: UIButton = {
        let btn = UIButton()
        btn.titleColor(for: .normal)
        btn.tintColor = .black
        btn.setTitle("Choose Repository1", for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 20
        btn.layer.borderWidth = 2
        btn.addTarget(self, action: #selector(clickedSearchBtn1), for: .touchUpInside)
        return btn
    }()
    
    lazy var searchBtn2: UIButton = {
        let btn = UIButton()
        btn.titleColor(for: .normal)
        btn.setTitle("Choose Repository2", for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 20
        btn.layer.borderWidth = 2
        btn.addTarget(self, action: #selector(clickedSearchBtn2), for: .touchUpInside)
        return btn
    }()
    
    lazy var nextBtn: UIButton = {
        let btn = UIButton()
        btn.titleColor(for: .normal)
        btn.setTitle("다음", for: .normal)
        btn.backgroundColor = UIColor(red: 15/255, green: 135/255, blue: 255/255, alpha: 1.0) /* #0f87ff */
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 20
        btn.layer.borderWidth = 2
        btn.addTarget(self, action: #selector(clickedNextBtn), for: .touchUpInside)
        return btn
    }()
    
    /*
     UI Action 작성
     */
    
    private func addToView(){
        self.view.addSubview(searchLabel)
        
        self.view.addSubview(searchBtn1)
        self.view.addSubview(searchBtn2)
        self.view.addSubview(nextBtn)
        
        setAutoLayout()
    }
    
    @objc func clickedSearchBtn1(){
        searchPage.beforePage = "CompareRepo1"
        self.navigationController?.pushViewController(searchPage, animated: true)
    }
    
    @objc func clickedSearchBtn2(){
        searchPage.beforePage = "CompareRepo2"
        self.navigationController?.pushViewController(searchPage, animated: true)
    }
    
    @objc func clickedNextBtn(){
        if !repository1.isEmpty && !repository2.isEmpty{
            let tabBar = TabBarController()
            
            CompareViewModel.viewModel.getContributorInfo(firstRepoName: self.repository1, secondRepoName: self.repository2)
                .subscribe(onNext: { contributorInfo in
                    print(contributorInfo)
                })
                .disposed(by: self.disposeBag)
            
            // 다음 화면 연동
            self.navigationController?.pushViewController(tabBar, animated: true)
        }
    }
    
    @objc func notificationData(notification: Notification){
        guard let id = notification.userInfo?[NotificationKey.choiceId] as? Int else {return}
        guard let data = notification.userInfo?[NotificationKey.repository] as? String else {return}
        
        if id  == 1{
            repository1 = data
            searchBtn1.setTitle(data, for: .normal)
        }
        else if id == 2{
            repository2 = data
            searchBtn2.setTitle(data, for: .normal)
        }
        
        
        
    }
    
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func setAutoLayout(){
        searchLabel.snp.makeConstraints({ make in
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.centerX.equalToSuperview()
        })
        
        searchBtn1.snp.makeConstraints({ make in
            make.top.equalTo(self.view.snp.centerY).offset(-100)
            make.height.equalTo(deviceHeight/18)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
        })
        
        searchBtn2.snp.makeConstraints({ make in
            make.top.equalTo(searchBtn1.snp.bottom).offset(20)
            make.height.equalTo(deviceHeight/18)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
        })
        
        nextBtn.snp.makeConstraints({ make in
            make.bottom.equalTo(view.safeAreaLayoutGuide).offset(-30)
            make.leading.equalTo(30)
            make.trailing.equalTo(-30)
        })
    }
    
}






