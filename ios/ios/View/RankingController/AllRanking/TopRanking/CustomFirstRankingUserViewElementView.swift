//
//  CustomUserViewElementView.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import UIKit
import SnapKit
import RxSwift

// MARK: 1등 보여주는 뷰
final class CustomFirstRankingUserViewElementView: UIView{
    var firstUserData: AllUserRankingModel?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK:
    private lazy var rankingImgView: UIImageView = {
        let imgview = UIImageView()
        imgview.image = UIImage(named: "firstRank")?.resize(newWidth: 60)
        return imgview
    }()
    
    // MARK:
    private lazy var userView: UserProfileImgView = {
        let view = UserProfileImgView()
        view.userLink = self.firstUserData?.profileImg
        view.layer.borderColor = .init(red: 255/255, green: 200/255, blue: 10/255, alpha: 1)
        view.layer.borderWidth = 1
        view.layer.cornerRadius = 20
        return view
    }()
    
    // MARK:
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .center
        label.text = firstUserData?.userName ?? ""
        return label
    }()
    
    // MARK:
    private lazy var numLabel: UILabel = {
        let label = UILabel()
        label.text = "\(firstUserData?.num ?? 0)"
        label.textAlignment = .center
        return label
    }()
    
    // MARK: UI add
    private func addUI(){
        self.addSubview(rankingImgView)
        self.addSubview(userView)
        self.addSubview(titleLabel)
        self.addSubview(numLabel)
        
        rankingImgView.snp.makeConstraints { make in
            make.top.equalToSuperview()
            make.centerX.equalToSuperview()
        }
        
        userView.snp.makeConstraints { make in
            make.top.equalTo(rankingImgView.snp.bottom).offset(5)
            make.centerX.equalToSuperview()
        }
        
        titleLabel.snp.makeConstraints { make in
            make.top.equalTo(userView.snp.bottom).offset(5)
            make.leading.equalToSuperview()
            make.trailing.equalToSuperview()
        }
        
        numLabel.snp.makeConstraints { make in
            make.top.equalTo(titleLabel.snp.bottom)
            make.leading.equalToSuperview()
            make.trailing.equalToSuperview()
        }
        
    }
    
    // MARK: 1등 데이터 삽입
    func getData(data: AllUserRankingModel){
        self.firstUserData = data
        addUI()
    }
        
    func updateData(data: AllUserRankingModel){
        self.firstUserData = data
        titleLabel.text = data.userName
        numLabel.text = "\(data.num ?? 0)"
        userView.updateData(img: data.profileImg ?? "")
    }
}


