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
    private lazy var profileBtn: UIButton = {
        let btn = UIButton()
        btn.setImage(img.image, for: .normal)
        btn.imageView?.layer.cornerRadius = 20
        btn.setTitleColor(.black, for: .normal)
        btn.titleLabel?.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
        return btn
    }()
    
    // MARK: 사용자 이름
    private lazy var nameLabel: UILabel = {
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
        self.view.addSubview(profileBtn)
        self.view.addSubview(nameLabel)
        
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
        
        profileBtn.snp.makeConstraints { make in
            make.top.equalTo(searchBtn.snp.bottom).offset(self.view.safeAreaLayoutGuide.layoutFrame.height/20)
            make.leading.equalTo(view.safeAreaLayoutGuide.layoutFrame.width/10)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        }
        
        nameLabel.snp.makeConstraints { make in
            make.top.equalTo(searchBtn.snp.bottom).offset(self.view.safeAreaLayoutGuide.layoutFrame.height/20)
            make.leading.equalTo(profileBtn.snp.trailing).offset(10)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        }

    }
    
    
    
}
