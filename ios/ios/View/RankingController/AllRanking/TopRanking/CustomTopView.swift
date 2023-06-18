//
//  CustomTopView.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import UIKit
import SnapKit

// MARK: 상위 1,2,3등 정보 보여주는 View
final class CustomTopView: UIView{
    var topTierData: [AllUserRankingModel] = []
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: 1등
    private lazy var firstRankView: CustomFirstRankingUserViewElementView = {
        let view = CustomFirstRankingUserViewElementView()
        view.getData(data: topTierData[0])
        view.backgroundColor = .white
        return view
    }()
    
    
    // MARK: 2등
    private lazy var secondRankView: CustomETCRankingUserViewElementView = {
        let view = CustomETCRankingUserViewElementView()
        view.getData(data: topTierData[1], rank: 2)
        view.backgroundColor = .white
        return view
    }()
    
    // MARK: 3등
    private lazy var thirdRankView: CustomETCRankingUserViewElementView = {
        let view = CustomETCRankingUserViewElementView()
        view.getData(data: topTierData[2] ,rank: 3)
        view.backgroundColor = .white
        return view
    }()
    
    // MARK: 스택 뷰
    private lazy var stackView: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [secondRankView, firstRankView, thirdRankView])
        stack.axis = .horizontal
        stack.distribution = .fillEqually
        return stack
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(stackView)
        
        stackView.snp.makeConstraints { make in
            make.leading.equalToSuperview()
            make.bottom.equalToSuperview().offset(-20)
            make.trailing.equalToSuperview()
            make.height.equalTo(250).priority(250)
        }
    }
    
    
    func getData(list: [AllUserRankingModel]){
        self.topTierData = list
        
        addUI()
        firstRankView.layoutIfNeeded()
        secondRankView.layoutIfNeeded()
        thirdRankView.layoutIfNeeded()
    }
    
    func updateData(list: [AllUserRankingModel]){
        self.topTierData = list
        firstRankView.updateData(data: list[0])
        secondRankView.updateData(data: list[1])
        thirdRankView.updateData(data: list[2])
    }
    
}



