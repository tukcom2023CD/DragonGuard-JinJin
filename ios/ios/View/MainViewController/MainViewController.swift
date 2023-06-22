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
        img.image = UIImage(named: "pomi")?.resize(newWidth: 80, newHeight: 80)
        img.layer.cornerRadius = 120
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
    
    // MARK: tier 글자
    private lazy var tierLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: tier 틀
    private lazy var tierView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.cornerRadius = 20
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK: tier 이미지
    private lazy var tierImage: UIImageView = {
        let tier = UIImageView()
        tier.image = UIImage(named: "tier")
        tier.backgroundColor = .white
        return tier
    }()
    
    // MARK: token 글자
    private lazy var tokenLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: token 틀
    private lazy var tokenView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.cornerRadius = 20
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK: token 이미지
    private lazy var tokenImage: UIImageView = {
        let token = UIImageView()
        token.image = UIImage(named: "token")
        token.backgroundColor = .white
        return token
    }()
    
    // MARK: token 숫자
    private lazy var tokenNumLabel: UILabel = {
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
    
    // MARK: contribution 틀
    private lazy var contributionView: UIView = {
        let view = UIView()
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
    
    // MARK: group 글자
    private lazy var groupLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: group 틀
    private lazy var groupView: UIView = {
        let view = UIView()
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.cornerRadius = 20
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK: group 내용
    private lazy var gNumLabel: UILabel = {
        let label = UILabel()
        label.backgroundColor = .white
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return label
    }()
    
    // MARK: TapBar
    private lazy var tapBarView: UITabBarController = {
        let tabBar = UITabBarController()
       return tabBar
    }()
    
    /*
     Add UI & Set AutoLayout
     */
    
    // MARK: UI 등록
    private func addUIToView(){
        view.addSubview(searchBtn)
        view.addSubview(profileImage)
        view.addSubview(nameLabel)
        
        view.addSubview(tierLabel)
        view.addSubview(tierView)
        tierView.addSubview(tierImage)
        
        view.addSubview(tokenLabel)
        view.addSubview(tokenView)
        tokenView.addSubview(tokenImage)
        tokenView.addSubview(tokenNumLabel)
        
        view.addSubview(contributionLabel)
        view.addSubview(contributionView)
        contributionView.addSubview(cNumLabel)
        
        view.addSubview(groupLabel)
        view.addSubview(groupView)
        groupView.addSubview(gNumLabel)
        
        // view.addSubview(tapBarView)
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
            make.top.equalTo(searchBtn.snp.bottom).offset(50)
            make.leading.equalTo(searchBtn.snp.leading)
            make.centerY.equalTo(view.snp_centerYWithinMargins)
        }

        nameLabel.snp.makeConstraints { make in
            make.top.equalTo(searchBtn.snp.bottom).offset(50)
            make.leading.equalTo(profileImage.snp.trailing).offset(30)
            make.centerY.equalTo(view.snp_centerYWithinMargins)
        }

    }
    
    
    
}
