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
        return label
    }()
    
    // MARK:
    private lazy var numLabel: UILabel = {
        let label = UILabel()
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
        addUI()
        titleLabel.text = data.github_id ?? ""
        numLabel.text = "\(data.tokens ?? 0)"
        
        if rank == 2{
            rankingImgView.image = UIImage(named: "secondRank")?.resize(newWidth: 60)
        }
        else if rank == 3{
            rankingImgView.image = UIImage(named: "thirdRank")?.resize(newWidth: 60)
        }
        
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
        userView.updateData(img: data.profile_image ?? "")
    }
    
    /*
     organization
     */
    
    // MARK: 2,3 등 organization 데이터 삽입
    func getData(data: TypeRankingModel, rank: Int){
        addUI()
        titleLabel.text = data.name ?? ""
        numLabel.text = "\(data.token_sum ?? 0)"
        
        if rank == 2{
            rankingImgView.image = UIImage(named: "secondRank")?.resize(newWidth: 60)
        }
        else if rank == 3{
            rankingImgView.image = UIImage(named: "thirdRank")?.resize(newWidth: 60)
        }
        
        userView.updateData(type: data.organization_type ?? "")
    }
    
}


