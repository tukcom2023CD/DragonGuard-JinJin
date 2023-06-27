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
        view.backgroundColor = .white
        return view
    }()
    
    
    // MARK: 2등
    private lazy var secondRankView: CustomETCRankingUserViewElementView = {
        let view = CustomETCRankingUserViewElementView()
        
        view.backgroundColor = .white
        return view
    }()
    
    // MARK: 3등
    private lazy var thirdRankView: CustomETCRankingUserViewElementView = {
        let view = CustomETCRankingUserViewElementView()
        
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
            make.top.equalToSuperview()
            make.height.equalTo(250).priority(250)
        }
    }
    
    
    func getData(list: [AllUserRankingModel]){
        self.topTierData = list
        
        addUI()
        print("getData")
        print(list)
        switch list.count{
        case 0:
            print("CustomTopView None")
        case 1:
            firstRankView.getData(data: topTierData[0])
        case 2:
            firstRankView.getData(data: topTierData[0])
            secondRankView.getData(data: topTierData[1], rank: 2)
        case 3:
            firstRankView.getData(data: topTierData[0])
            secondRankView.getData(data: topTierData[1], rank: 2)
            thirdRankView.getData(data: topTierData[2] ,rank: 3)
        default:
            print("CustomTopView Error\n")
        }
        
        firstRankView.layoutIfNeeded()
        secondRankView.layoutIfNeeded()
        thirdRankView.layoutIfNeeded()
    }
    
    func updateData(list: [AllUserRankingModel]){
        self.topTierData = list
        print("update")
        print(list)
        switch list.count{
        case 0:
            print("CustomTopView None")
        case 1:
            firstRankView.updateData(data: list[0])
        case 2:
            firstRankView.updateData(data: list[0])
            secondRankView.updateData(data: list[1])
        case 3:
            firstRankView.updateData(data: list[0])
            secondRankView.updateData(data: list[1])
            thirdRankView.updateData(data: list[2])
        default:
            print("CustomTopView Error\n")
        }
        
    }
    
}



