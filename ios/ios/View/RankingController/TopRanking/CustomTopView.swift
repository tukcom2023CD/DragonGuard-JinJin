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
//    private var topTierData: [AllUserRankingModel] = []
//    private var topTierTypeOfRankingData: [TypeRankingModel] = []
    
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
        addUI()
        print("getData")
        print(list)
        switch list.count{
        case 0:
            print("CustomTopView None")
            firstRankView.isHidden = true
            secondRankView.isHidden = true
            thirdRankView.isHidden = true
        case 1:
            firstRankView.getData(data: list[0])
            firstRankView.isHidden = false
            secondRankView.isHidden = false
            thirdRankView.isHidden = false
        case 2:
            firstRankView.getData(data: list[0])
            secondRankView.getData(data: list[1], rank: 2)
            firstRankView.isHidden = false
            secondRankView.isHidden = false
            thirdRankView.isHidden = false
        case 3:
            firstRankView.getData(data: list[0])
            secondRankView.getData(data: list[1], rank: 2)
            thirdRankView.getData(data: list[2] ,rank: 3)
            firstRankView.isHidden = false
            secondRankView.isHidden = false
            thirdRankView.isHidden = false
        default:
            print("CustomTopView Error\n")
        }
        
        firstRankView.layoutIfNeeded()
        secondRankView.layoutIfNeeded()
        thirdRankView.layoutIfNeeded()
    }
    
    /*
     Organization
     */
    
    func getData(list: [TypeRankingModel]){
        addUI()
        print("getData")
        print(list)
        switch list.count{
        case 0:
            print("CustomTopView None")
            firstRankView.isHidden = true
            secondRankView.isHidden = true
            thirdRankView.isHidden = true
        case 1:
            firstRankView.getData(data: list[0])
            firstRankView.isHidden = false
            secondRankView.isHidden = false
            thirdRankView.isHidden = false
        case 2:
            firstRankView.getData(data: list[0])
            secondRankView.getData(data: list[1], rank: 2)
            firstRankView.isHidden = false
            secondRankView.isHidden = false
            thirdRankView.isHidden = false
        case 3:
            firstRankView.getData(data: list[0])
            secondRankView.getData(data: list[1], rank: 2)
            thirdRankView.getData(data: list[2] ,rank: 3)
            firstRankView.isHidden = false
            secondRankView.isHidden = false
            thirdRankView.isHidden = false
        default:
            print("CustomTopView Error\n")
            
        }
        
        firstRankView.layoutIfNeeded()
        secondRankView.layoutIfNeeded()
        thirdRankView.layoutIfNeeded()
    }
    
    
}



