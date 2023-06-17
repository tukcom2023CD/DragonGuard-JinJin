//
//  CustomETCRankingUserViewElementView.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import UIKit
import SnapKit

// MARK: 2,3 등 보여주는 뷰
final class CustomETCRankingUserViewElementView: UIView{
    var etcUserData: AllUserRankingModel?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK:
    private lazy var rankingImgView: UIImageView = {
        let imgview = UIImageView()
        return imgview
    }()
    
    // MARK:
    private lazy var userView: UserProfileImgView = {
        let view = UserProfileImgView()
        view.layer.borderColor = .init(red: 255/255, green: 200/255, blue: 10/255, alpha: 1)
        view.layer.borderWidth = 1
        view.layer.cornerRadius = 20
        return view
    }()
    
    // MARK:
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .center
        label.text = etcUserData?.userName ?? ""
        return label
    }()
    
    // MARK:
    private lazy var numLabel: UILabel = {
        let label = UILabel()
        label.text = "\(etcUserData?.num ?? 0)"
        label.textAlignment = .center
        return label
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(rankingImgView)
        self.addSubview(userView)
        self.addSubview(titleLabel)
        self.addSubview(numLabel)
        
        rankingImgView.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.bottom.equalTo(userView.snp.top).offset(-5)
        }
        
        userView.snp.makeConstraints { make in
            make.centerX.equalToSuperview()
            make.bottom.equalTo(titleLabel.snp.top).offset(-5)
        }
        
        titleLabel.snp.makeConstraints { make in
            make.leading.equalToSuperview()
            make.trailing.equalToSuperview()
            make.bottom.equalTo(numLabel.snp.top).offset(-5)
        }
        
        numLabel.snp.makeConstraints { make in
            make.leading.equalToSuperview()
            make.trailing.equalToSuperview()
            make.bottom.equalToSuperview()
        }
        
    }
    
    // MARK: 2,3 등 데이터 삽입
    func getData(data: AllUserRankingModel, rank: Int){
        self.etcUserData = data
        addUI()
        
        if rank == 2{
            rankingImgView.image = UIImage(named: "secondRank")?.resize(newWidth: 60)
        }
        else if rank == 3{
            rankingImgView.image = UIImage(named: "thirdRank")?.resize(newWidth: 60)
        }
    }
}


