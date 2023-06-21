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
    private let img = UIImageView()
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        addUIToView()
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
        img.layer.cornerRadius = 20
        img.backgroundColor = .white
        img.layer.shadowOffset = CGSize(width: 5, height: 5)
        img.layer.shadowOpacity = 0.7
        img.layer.shadowRadius = 5
        img.layer.shadowColor = UIColor.gray.cgColor
        return img
    }()
    
    // MARK: 사용자 이름
    private lazy var nameLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: tier 배경
    private lazy var tierView: TitleView = {
        let view = TitleView()
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.cornerRadius = 20
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK: tier 글자
    private lazy var tierLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: tier 이미지
    private lazy var tierImage: UIImageView = {
        let tier = UIImageView()
        tier.image = UIImage(named: "tier")
        tier.backgroundColor = .white
        return tier
    }()
    
    // MARK: token 배경
    private lazy var tokenView: TitleView = {
        let view = TitleView()
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.cornerRadius = 20
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK: token 글자
    private lazy var tokenLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: token 이미지
    private lazy var tokenImage: UIImageView = {
        let token = UIImageView()
        token.image = UIImage(named: "token")
        token.backgroundColor = .white
        return token
    }()
    
    // MARK: 링크 이미지
    private lazy var linkImage: UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(named: "linkIcon")?.resize(newWidth: 30), for: .normal)
        btn.backgroundColor = .white
        return btn
    }()
    
    // MARK: contribution 배경
    private lazy var contributionView: TitleView = {
        let view = TitleView()
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.cornerRadius = 20
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK: contribution 글자
    private lazy var cNameLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: contribution 글자
    private lazy var contributionLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: group 배경
    private lazy var groupView: TitleView = {
        let view = TitleView()
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.cornerRadius = 20
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK: contribution 글자
    private lazy var gNameLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: group 글자
    private lazy var groupLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    /*
     Add UI & Set AutoLayout
     */
    
    // MARK: UI 등록
    private func addUIToView(){
        self.view.addSubview(searchBtn)
        self.view.addSubview(profileImage)
        self.view.addSubview(nameLabel)
        self.view.addSubview(tierView)
        self.view.addSubview(tierLabel)
        self.view.addSubview(tierImage)
        self.view.addSubview(tokenView)
        self.view.addSubview(tokenLabel)
        self.view.addSubview(tokenImage)
        self.view.addSubview(linkImage)
        self.view.addSubview(contributionView)
        self.view.addSubview(cNameLabel)
        self.view.addSubview(contributionLabel)
        self.view.addSubview(groupView)
        self.view.addSubview(gNameLabel)
        self.view.addSubview(groupLabel)
        
        setAutoLayout()
    }
    
    // MARK: setting AutoLayout
    private func setAutoLayout(){
        
        searchBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/10)
            make.width.equalTo(view.safeAreaLayoutGuide.layoutFrame.width*35/60)
            make.centerX.equalTo(view.snp_centerXWithinMargins)
        }
        
        profileImage.snp.makeConstraints { make in
            make.top.equalTo(searchBtn.snp.bottom).offset(self.view.safeAreaLayoutGuide.layoutFrame.height/20)
            make.leading.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/10)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        }

        nameLabel.snp.makeConstraints { make in
            make.top.equalTo(searchBtn.snp.bottom).offset(self.view.safeAreaLayoutGuide.layoutFrame.height/20)
            make.leading.equalTo(profileImage.snp.trailing).offset(10)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        }

    }
    
    
    
}
