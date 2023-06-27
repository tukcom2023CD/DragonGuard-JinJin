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
        view.userLink = self.firstUserData?.profile_image
        view.layer.borderWidth = 1
        view.layer.cornerRadius = 20
        return view
    }()
    
    // MARK:
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.textAlignment = .center
        label.text = firstUserData?.github_id ?? ""
        return label
    }()
    
    // MARK:
    private lazy var numLabel: UILabel = {
        let label = UILabel()
        label.text = "\(firstUserData?.tokens ?? 0)"
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
        titleLabel.text = data.github_id
        numLabel.text = "\(data.tokens ?? 0)"
        
        switch data.tier ?? ""{
        case "BRONZE":
            userView.layer.borderColor = CGColor(red: 101/255, green: 4/255, blue: 4/255, alpha: 1.0) /* #650404 */
        case "SILVER":
            userView.layer.borderColor = CGColor(red: 192/255, green: 192/255, blue: 192/255, alpha: 1.0) /* #c0c0c0 */
        case "GOLD":
            userView.layer.borderColor = CGColor(red: 245/255, green: 238/255, blue: 176/255, alpha: 1.0) /* #f5eeb0 */
        case "PLATINUM":
            userView.layer.borderColor = CGColor(red: 46/255, green: 198/255, blue: 189/255, alpha: 1.0) /* #2ec6bd */
        case "DIAMOND":
            userView.layer.borderColor = CGColor(red: 0/255, green: 219/255, blue: 249/255, alpha: 1.0) /* #00dbf9 */
        default:
            userView.layer.borderColor = CGColor(red: 255/255, green: 25/255, blue: 255/255, alpha: 1.0) 
        }
        
        // 티어 색상마다 색깔 다르게
        userView.updateData(img: data.profile_image ?? "")
    }
}


