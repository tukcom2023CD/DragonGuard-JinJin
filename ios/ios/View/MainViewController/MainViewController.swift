//
//  MainViewController.swift
//  ios
//
//  Created by 홍길동 on 2023/06/21.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

final class MainViewController: UIViewController {
    private let disposeBag = DisposeBag()
    private let viewModel = MainViewModel()
    private var blockChainUrl: String?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        clickedBtn()
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        getData()
    }
    
    /*
     UI 작성
     */
          
    
    // MARK: 검색 버튼
    private lazy var searchBtn: UIButton = {
        let btn = UIButton()
        btn.backgroundColor = .white
        btn.setImage(UIImage(systemName: "magnifyingglass"), for: .normal)
        btn.setTitle(" Repository or User ", for: .normal)
        btn.titleLabel?.font = UIFont.systemFont(ofSize: 20)
        btn.setTitleColor(.lightGray, for: .normal)
        btn.layer.cornerRadius = 18
        btn.layer.shadowOpacity = 0.5
        btn.layer.shadowOffset = CGSize(width: -3, height: 3)
        return btn
    }()
    
    // MARK: 사용자 프로필
    private lazy var profileImage: UIImageView = {
        let img = UIImageView()
        img.backgroundColor = .white
        img.layer.shadowOffset = CGSize(width: 5, height: 5)
        img.layer.shadowOpacity = 0.7
        img.layer.shadowRadius = 20
        img.layer.cornerRadius = 20
        img.layer.shadowColor = UIColor.gray.cgColor
        img.clipsToBounds = true
        img.layer.masksToBounds = true
        return img
    }()
    
    // MARK: 사용자 이름
    private lazy var nameLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.text = "None"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: tier 글자
    private lazy var tierLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.text = "tier"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK:
    private lazy var emptyView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        return view
    }()
    
    // MARK: tier 틀
    private lazy var tierView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.borderWidth = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.cornerRadius = 20
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        view.clipsToBounds = true
        view.layer.masksToBounds = true
        return view
    }()
    
    // MARK: tier 이미지
    private lazy var tierImage: UIImageView = {
        let img = UIImageView()
        img.image = UIImage(named: "tier")
        img.backgroundColor = .white
        img.layer.shadowOffset = CGSize(width: 5, height: 5)
        img.layer.shadowOpacity = 0.7
        img.layer.shadowRadius = 20
        img.layer.cornerRadius = 20
        img.layer.shadowColor = UIColor.gray.cgColor
        img.clipsToBounds = true
        img.layer.masksToBounds = true
        return img
    }()
    
    // MARK: token 글자
    private lazy var tokenLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.text = "token"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK:
    private lazy var emptyView1: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        return view
    }()
    
    // MARK: token 틀
    private lazy var tokenView: UIButton = {
        let view = UIButton()
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.borderWidth = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.cornerRadius = 20
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK: token 이미지
    private lazy var tokenImage: UIImageView = {
        let token = UIImageView()
        token.image = UIImage(named: "token")?.resize(newWidth: 50, newHeight: 50)
        token.backgroundColor = .white
        return token
    }()
    
    // MARK: token 숫자
    private lazy var tokenNumLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.text = "0"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: contribution 글자
    private lazy var contributionLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.text = "기여도"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK:
    private lazy var emptyView2: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        return view
    }()
    
    // MARK: contribution 틀
    private lazy var contributionView: ContributorAutoUIView = {
        let view = ContributorAutoUIView()
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.cornerRadius = 20
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK: contribution 내용
    private lazy var cNumLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    
    // MARK: group 틀
    private lazy var groupView: OrganiRankInMyRank = {
        let view = OrganiRankInMyRank()
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.cornerRadius = 20
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK: contribution 글자
    private lazy var groupLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.text = "None"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK:
    private lazy var emptyView3: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        return view
    }()
    
    /*
     Add UI & Set AutoLayout
     */
    
    // MARK: UI 등록
    private func addUIToView(){
        view.addSubview(searchBtn)
        view.addSubview(profileImage)
        view.addSubview(nameLabel)
        
        
        view.addSubview(tierView)
        tierView.addSubview(tierImage)
        view.addSubview(emptyView)
        emptyView.addSubview(tierLabel)
        
        
        view.addSubview(tokenView)
        view.addSubview(emptyView1)
        emptyView1.addSubview(tokenLabel)
        tokenView.addSubview(tokenImage)
        tokenView.addSubview(tokenNumLabel)

        
        view.addSubview(contributionView)
        view.addSubview(emptyView2)
        emptyView2.addSubview(contributionLabel)
        
        view.addSubview(groupView)
        view.addSubview(emptyView3)
        emptyView3.addSubview(groupLabel)
        
        setAutoLayout()
    }
    
    // MARK: setting AutoLayout
    private func setAutoLayout(){
        
        searchBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/10)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width*35/60)
            make.centerX.equalTo(view.snp.centerX)
        }
        
        profileImage.snp.makeConstraints { make in
            make.top.equalTo(searchBtn.snp.bottom).offset(30)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/5)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/5)
            make.leading.equalTo(searchBtn.snp.leading)
        }

        nameLabel.snp.makeConstraints { make in
            make.centerY.equalTo(profileImage.snp.centerY)
            make.leading.equalTo(profileImage.snp.trailing).offset(30)
        }
        
        tierView.snp.makeConstraints { make in
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(30)
            make.top.equalTo(profileImage.snp.bottom).offset(30)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width*2/5)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.width*2/5)
        }
        
        tierImage.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(20)
            make.leading.equalToSuperview().offset(20)
            make.trailing.equalToSuperview().offset(-20)
            make.bottom.equalToSuperview().offset(-20)
        }
        
        emptyView.snp.makeConstraints { make in
            make.top.equalTo(tierView.snp.top).offset(-5)
            make.centerX.equalTo(tierView.snp.centerX)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/6)
            make.bottom.equalTo(tierView.snp.top).offset(5)
        }
        
        tierLabel.snp.makeConstraints { make in
            make.center.equalTo(emptyView.snp.center)
        }
        
        tokenView.snp.makeConstraints { make in
            make.top.equalTo(tierView.snp.top)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-30)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width*2/5)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.width*2/5)
        }
        
        tokenImage.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(20)
            make.centerX.equalToSuperview()
        }
        
        tokenNumLabel.snp.makeConstraints { make in
            make.top.equalTo(tokenImage.snp.bottom).offset(10)
            make.centerX.equalTo(tokenImage.snp.centerX)
        }
        
        emptyView1.snp.makeConstraints { make in
            make.top.equalTo(tokenView.snp.top).offset(-5)
            make.centerX.equalTo(tokenView.snp.centerX)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/6)
            make.bottom.equalTo(tokenView.snp.top).offset(5)
        }
        
        tokenLabel.snp.makeConstraints { make in
            make.center.equalTo(emptyView1.snp.center)
        }
        
        contributionView.snp.makeConstraints { make in
            make.top.equalTo(tokenView.snp.bottom).offset(50)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(30)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-30)
        }
        
        emptyView2.snp.makeConstraints { make in
            make.top.equalTo(contributionView.snp.top).offset(-5)
            make.centerX.equalTo(contributionView.snp.centerX)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/6)
            make.bottom.equalTo(contributionView.snp.top).offset(5)
        }
        
        contributionLabel.snp.makeConstraints { make in
            make.center.equalTo(emptyView2.snp.center)
        }

        groupView.snp.makeConstraints { make in
            make.top.equalTo(contributionView.snp.bottom).offset(30)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(40)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-40)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height*22/100)
            make.bottom.equalTo(view.safeAreaLayoutGuide).offset(-30)
        }
        
        emptyView3.snp.makeConstraints { make in
            make.top.equalTo(groupView.snp.top).offset(-5)
            make.centerX.equalTo(groupView.snp.centerX)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/6)
            make.bottom.equalTo(groupView.snp.top).offset(5)
        }
        
        groupLabel.snp.makeConstraints { make in
            make.center.equalTo(emptyView3.snp.center)
        }
        
    }
    
    
    func getData(){
        addUIToView()
        
        viewModel.getMyInformation().subscribe(onNext: { data in
            let imgUrl = URL(string: data.profile_image ?? "")!
            
            self.profileImage.load(img: self.profileImage, url: imgUrl, size: nil)
            self.tokenNumLabel.text = "\(data.token_amount ?? 0)"
            self.nameLabel.text = data.github_id ?? ""
            self.blockChainUrl = data.blockchain_url
            switch data.tier{
            case "BRONZE":
                self.tierImage.image = UIImage(named: "bronze")
            case "SILVER":
                self.tierImage.image = UIImage(named: "silver")
            case "GOLD":
                self.tierImage.image = UIImage(named: "gold")
            case "PLATINUM":
                self.tierImage.image = UIImage(named: "platinum")
            case "DIAMOND":
                self.tierImage.image = UIImage(named: "diamond")
            default:
                print("error!\n")
            }
            let check = (data.is_last ?? false)
            
            
            if data.organization_rank == 1{
                
                if data.member_github_ids?.count != 1{
                    self.groupView.inputData(rank1: nil,
                                             top: nil,
                                             rank2: data.organization_rank ?? 1,
                                             me: data.member_github_ids?[0] ?? "Unknown",
                                             rank3: (data.organization_rank ?? 1)+1,
                                             under: data.member_github_ids?[1] ?? "Unknown")
                }
                else{
                    self.groupView.inputData(rank1: nil,
                                             top: nil,
                                             rank2: data.organization_rank ?? 1,
                                             me: data.member_github_ids?[0] ?? "Unknown",
                                             rank3: nil,
                                             under: nil)
                }
            }
            else if check {
                if data.member_github_ids?.count != 1{
                    self.groupView.inputData(rank1: (data.organization_rank ?? 0)-1,
                                             top: data.member_github_ids?[0] ?? "Unknown",
                                             rank2: (data.organization_rank ?? 0),
                                             me: data.member_github_ids?[1] ?? "Unknown",
                                             rank3: nil,
                                             under: nil)
                }
                else{
                    self.groupView.inputData(rank1: nil,
                                             top: nil,
                                             rank2: (data.organization_rank ?? 0),
                                             me: data.member_github_ids?[0] ?? "Unknown",
                                             rank3: nil,
                                             under: nil)
                }
            }
            else if ((data.member_github_ids?.isEmpty) == nil){
                self.groupView.inputData(rank1: nil,
                                         top: nil,
                                         rank2: nil,
                                         me: nil,
                                         rank3: nil,
                                         under: nil)
            }
            else{
                self.groupView.inputData(rank1: (data.organization_rank ?? 0)-1,
                                         top: data.member_github_ids?[0] ?? "Unknown",
                                         rank2: (data.organization_rank ?? 0),
                                         me: data.member_github_ids?[1] ?? "Unknown",
                                         rank3: (data.organization_rank ?? 0)+1,
                                         under: data.member_github_ids?[2] ?? "Unknown")
            }
            
            self.groupLabel.text = data.organization ?? "None"
            
            self.contributionView.timer?.invalidate()
            self.contributionView.inputData(commit: data.commits ?? 0,
                                            issue: data.issues ?? 0,
                                            pr: data.pull_requests ?? 0,
                                            reviews: data.reviews ?? 0)
        })
        .disposed(by: self.disposeBag)
    }
    
    
    func clickedBtn(){
        searchBtn.rx.tap.subscribe(onNext:{
            let nextPage = SearchViewController()
            nextPage.modalPresentationStyle = .fullScreen
            nextPage.beforePage = "Main"
            self.present(nextPage ,animated: false)
        })
        .disposed(by: disposeBag)
        
        
        tokenView.rx.tap.subscribe(onNext:{
            let nextPage = BlockChainListController()
            nextPage.modalPresentationStyle = .fullScreen
            nextPage.blockchainUrl = self.blockChainUrl
            self.contributionView.stopTimer()
            self.present(nextPage ,animated: false)
        })
        .disposed(by: disposeBag)
    }
    
}
