//
//  YourProfileController.swift
//  ios
//
//  Created by 정호진 on 2023/06/21.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

final class YourProfileController: UIViewController{
    var userName: String?
    private var listCount: Int?
    private let disposeBag = DisposeBag()
    private let viewModel = YourProfileViewModel()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        getData()
    }
    
    /*
     UI Code
     */
    
    // MARK: 뒤로가기 버튼
    private lazy var backBtn: UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(named: "backBtn")?.resize(newWidth: 30), for: .normal)
        return btn
    }()
    
    // MARK: 유저 이름
    private lazy var nameLabel: UILabel = {
        let label = UILabel()
        label.text = self.userName ?? ""
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 25)
        label.textColor = .black
        label.backgroundColor = .clear
        return label
    }()
    
    // MARK:
    private lazy var scrollView: UIScrollView = {
        let scroll = UIScrollView()
        scroll.backgroundColor = .white
        return scroll
    }()
    
    // MARK:
    private lazy var contentView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        return view
    }()
    
    // MARK:
    private lazy var profileImageView: UIImageView = {
        let imgView = UIImageView()
        imgView.image = UIImage(named: "2")?.resize(newWidth: view.safeAreaLayoutGuide.layoutFrame.height/5, newHeight: view.safeAreaLayoutGuide.layoutFrame.height/5)
        imgView.layer.cornerRadius = 20
        imgView.clipsToBounds = true
        return imgView
    }()
    
    // MARK: 소속된 조직 이름
    private lazy var organizationLabel: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 20)
        label.text = "조직 없음"
        label.textColor = .black
        label.backgroundColor = .clear
        return label
    }()
    
    // MARK:
    private lazy var contributorView: ContributorsInfoUIView = {
        let view = ContributorsInfoUIView()
        view.backgroundColor = .white
        return view
    }()
    
    
    // MARK:
    private lazy var repoTitle: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        return view
    }()
    
    // MARK: Repository
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 23)
        label.text = "Repository"
        label.backgroundColor = .clear
        label.textColor = .black
        return label
    }()
    
    // MARK:
    private lazy var repoListView: RepositoryListUIView = {
        let view = RepositoryListUIView()
        view.layer.shadowOffset = CGSize(width: 0, height: 10)
        view.layer.shadowOpacity = 0.5
        view.layer.cornerRadius = 20
        view.layer.borderWidth = 1
        view.layer.borderColor = CGColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.5)
        view.layer.masksToBounds = true
        view.backgroundColor = .white
        view.clipsToBounds = true
        return view
    }()
    
    /*
     Add UI, AutoLayout
     */
    // MARK:
    private func addUI(){
        view.addSubview(backBtn)
        view.addSubview(nameLabel)
        view.addSubview(scrollView)
        
        scrollView.addSubview(contentView)
        
        contentView.addSubview(profileImageView)
        contentView.addSubview(organizationLabel)
        contentView.addSubview(contributorView)
        contentView.addSubview(repoListView)
        contentView.addSubview(repoTitle)
        repoTitle.addSubview(titleLabel)
        
        setAutoLayout()
        clickBtn()
    }
    
    // MARK:
    private func setAutoLayout(){
        backBtn.snp.makeConstraints { make in
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.top.equalTo(view.safeAreaLayoutGuide).offset(10)
        }
        
        nameLabel.snp.makeConstraints { make in
            make.top.equalTo(backBtn.snp.top)
            make.leading.equalTo(backBtn.snp.trailing).offset(30)
        }
        
        scrollView.snp.makeConstraints { make in
            make.top.equalTo(nameLabel.snp.bottom).offset(20)
            make.leading.trailing.bottom.equalTo(view.safeAreaLayoutGuide)
        }
        
        contentView.snp.makeConstraints { make in
            make.top.equalTo(scrollView.snp.top)
            make.leading.equalTo(scrollView.snp.leading)
            make.trailing.equalTo(scrollView.snp.trailing)
            make.bottom.equalTo(scrollView.snp.bottom)
            make.width.equalTo(scrollView.snp.width)
        }
        
        profileImageView.snp.makeConstraints { make in
            make.top.equalTo(contentView.snp.top)
            make.centerX.equalTo(contentView.snp.centerX)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/5)
        }
        
        organizationLabel.snp.makeConstraints { make in
            make.top.equalTo(profileImageView.snp.bottom).offset(10)
            make.centerX.equalTo(profileImageView.snp.centerX)
        }
        
        contributorView.snp.makeConstraints { make in
            make.top.equalTo(organizationLabel.snp.bottom).offset(20)
            make.leading.equalTo(contentView.snp.leading)
            make.trailing.equalTo(contentView.snp.trailing)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/7)
        }
        
        repoListView.snp.makeConstraints { make in
            make.top.equalTo(contributorView.snp.bottom).offset(50)
            make.leading.equalTo(contentView.snp.leading).offset(20)
            make.trailing.equalTo(contentView.snp.trailing).offset(-20)
            make.bottom.equalTo(contentView.snp.bottom).offset(-10)
            make.height.equalTo((100 * CGFloat(listCount ?? 0))+10)
        }
        
        repoTitle.snp.makeConstraints { make in
            make.top.equalTo(repoListView.snp.top).offset(-10)
            make.bottom.equalTo(repoListView.snp.top).offset(10)
            make.centerX.equalTo(repoListView.snp.centerX)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/3)
        }
        
        titleLabel.snp.makeConstraints { make in
            make.center.equalToSuperview()
        }
        
    }
    
    // MARK:
    private func getData(){
        guard let userName = userName else { return }
        viewModel.getData(githubId: userName)
            .subscribe(onNext:{ data in
                self.listCount = data.git_repos?.count
                self.addUI()
                self.profileImageView.load(img: self.profileImageView,
                                           url: URL(string: data.profile_image ?? "")!,
                                           width: self.view.safeAreaLayoutGuide.layoutFrame.height/5,
                                           height: self.view.safeAreaLayoutGuide.layoutFrame.height/5)
                self.organizationLabel.text = data.organization ?? "None"
                
                self.contributorView.inputData(ranking: data.rank ?? 0,
                                               commit: data.commits ?? 0,
                                               issue: data.issues ?? 0)
                self.repoListView.delegate = self
                self.repoListView.inputData(img: data.profile_image ?? "",
                                            userName: self.userName ?? "none",
                                            repoName: data.git_repos ?? [],
                                            height: self.view.safeAreaLayoutGuide.layoutFrame.height)
            })
            .disposed(by: disposeBag)
        
        
    }
    
    // MARK:
    private func clickBtn(){
        backBtn.rx.tap.subscribe(onNext: {
            self.dismiss(animated: true)
        })
        .disposed(by: disposeBag)
    }
}

extension YourProfileController: ClickedRepos{
    func clickedRepos(repoName: String) {
        let nextPage = RepoDetailController()
        nextPage.modalPresentationStyle = .fullScreen
        nextPage.selectedTitle = repoName
        self.present(nextPage,animated: false)
    }
    
}
