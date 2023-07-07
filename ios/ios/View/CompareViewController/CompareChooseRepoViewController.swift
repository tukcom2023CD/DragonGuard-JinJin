//
//  CompareChooseRepoViewController.swift
//  ios
//
//  Created by 정호진 on 2023/06/18.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

final class CompareChooseRepoViewController: UIViewController{
    private let disposeBag = DisposeBag()
    var firstRepo: String?
    var secondRepo: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        addUI()
        
        NotificationCenter.default.addObserver(self, selector: #selector(notificationData(notification:)), name: Notification.Name.data, object: nil)
    }
    
    // MARK:
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.text = "Repository 비교하기"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 30)
        return label
    }()
    
    // MARK: 유저 1 선택
    private lazy var repository1Btn: UIButton = {
        let btn = UIButton()
        btn.setTitle("Choose Repository1", for: .normal)
//        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.titleLabel?.adjustsFontSizeToFitWidth = true
        btn.setTitleColor(.black, for: .normal)
        btn.backgroundColor = .white
        btn.layer.cornerRadius = 20
        btn.layer.shadowOffset = CGSize(width: 3, height: 3)
        btn.layer.shadowOpacity = 0.5
        return btn
    }()
    
    // MARK: 유저 2 선택
    private lazy var repository2Btn: UIButton = {
        let btn = UIButton()
        btn.setTitle("Choose Repository2", for: .normal)
//        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.titleLabel?.adjustsFontSizeToFitWidth = true
        btn.backgroundColor = .white
        btn.setTitleColor(.black, for: .normal)
        btn.layer.cornerRadius = 20
        btn.layer.shadowOffset = CGSize(width: 3, height: 3)
        btn.layer.shadowOpacity = 0.5
        return btn
    }()
    
    // MARK:
    private lazy var stackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [repository1Btn, repository2Btn])
        stack.axis = .vertical
        stack.distribution = .fillEqually
        stack.spacing = 20
        return stack
    }()
    
    // MARK: 다음 버튼
    private lazy var nextBtn: UIButton = {
        let btn = UIButton()
        btn.backgroundColor = .blue
        btn.setTitle("다음", for: .normal)
        btn.setTitleColor(.white, for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        btn.layer.cornerRadius = 20
        btn.layer.shadowOffset = CGSize(width: 3, height: 3)
        btn.layer.shadowOpacity = 0.5
        
        return btn
    }()
    
    // MARK:
    private func addUI(){
        view.addSubview(titleLabel)
        view.addSubview(stackView)
        view.addSubview(nextBtn)
        
        titleLabel.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(100)
            make.centerX.equalTo(view.snp.centerX)
        }
        
        stackView.snp.makeConstraints { make in
            make.centerY.equalToSuperview()
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(20)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-20)
        }
        
        nextBtn.snp.makeConstraints { make in
            make.top.equalTo(stackView.snp.bottom).offset(40)
            make.centerX.equalToSuperview()
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/4)
        }
        addClickedUI()
    }
    
    // MARK:
    private func addClickedUI(){
        repository1Btn.rx.tap.subscribe(onNext:{
            let searchPage = SearchViewController()
            searchPage.beforePage = "Compare1"
            searchPage.modalPresentationStyle = .fullScreen
            self.present(searchPage, animated: true)
        })
        .disposed(by: disposeBag)
        
        repository2Btn.rx.tap.subscribe(onNext:{
            let searchPage = SearchViewController()
            searchPage.beforePage = "Compare2"
            searchPage.modalPresentationStyle = .fullScreen
            self.present(searchPage, animated: true)
        })
        .disposed(by: disposeBag)
        
        nextBtn.rx.tap.subscribe(onNext:{
            if !(self.firstRepo?.isEmpty ?? false) && !(self.secondRepo?.isEmpty ?? false){
                let nextPage = CompareRepoUserController()
                nextPage.modalPresentationStyle = .fullScreen
                nextPage.firstRepo = self.firstRepo
                nextPage.secondRepo = self.secondRepo
                self.present(nextPage, animated: true)
            }
        })
        .disposed(by: disposeBag)
    }
    
    
    @objc func notificationData(notification: Notification){
        guard let id = notification.userInfo?[NotificationKey.choiceId] as? Int else {return}
        guard let data = notification.userInfo?[NotificationKey.repository] as? String else {return}
        
        if id  == 1{
            self.firstRepo = data
            repository1Btn.setTitle(data, for: .normal)
        }
        else if id == 2{
            self.secondRepo = data
            repository2Btn.setTitle(data, for: .normal)
        }
        
    }
    
}
