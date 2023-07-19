//
//  CustomTopView.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import UIKit
import RxSwift
import SnapKit

// MARK: 상위 1,2,3등 정보 보여주는 View
final class CustomTopView: UIView{
    private var disposeBag = DisposeBag()
    var delegate: SendUserName?
    
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
        let stack = UIStackView(arrangedSubviews: [secondRankView,firstRankView,thirdRankView])
        stack.axis = .horizontal
        stack.distribution = .fillEqually
        stack.backgroundColor = .white
        return stack
    }()
    
    // MARK:
    private func addUI(){
        addSubview(stackView)
        
        stackView.snp.makeConstraints { make in
            make.leading.equalToSuperview()
            make.bottom.equalToSuperview().offset(-20)
            make.trailing.equalToSuperview()
            make.top.equalToSuperview()
            make.height.equalTo(250).priority(250)
        }
    }
    
    func getData(list: [AllUserRankingModel]){
        stackView.removeFromSuperview()
        addUI()
        disposeBag = DisposeBag()

        print("getData1")
        print(list)
        
        switch list.count{
        case 0:
            print("CustomTopView None")
            firstRankView.isHidden = true
            secondRankView.isHidden = true
            thirdRankView.isHidden = true
        case 1:
            firstRankView.getData(data: list[0])
            
            firstRankView.rx.tap
                .subscribe(onNext: {
                    self.delegate?.sendUserName(name: list[0].github_id ?? "", type: "user")
                })
                .disposed(by: disposeBag)
            
            firstRankView.isHidden = false
            secondRankView.isHidden = true
            thirdRankView.isHidden = true
        case 2:
            firstRankView.getData(data: list[0])
            secondRankView.getData(data: list[1], rank: 2)
            thirdRankView.getData(data: nil, rank: 0)
            
            firstRankView.rx.tap
                .subscribe(onNext: {
                    self.delegate?.sendUserName(name: list[0].github_id ?? "", type: "user")
                })
                .disposed(by: disposeBag)
            
            secondRankView.rx.tap
                .subscribe(onNext: {
                    self.delegate?.sendUserName(name: list[1].github_id ?? "", type: "user")
                })
                .disposed(by: disposeBag)
            
            firstRankView.isHidden = false
            secondRankView.isHidden = false
            thirdRankView.isHidden = false
            thirdRankView.layer.opacity = 0
            
        case 3:
            firstRankView.getData(data: list[0])
            secondRankView.getData(data: list[1], rank: 2)
            thirdRankView.getData(data: list[2], rank: 3)
            
            firstRankView.rx.tap
                .subscribe(onNext: {
                    self.delegate?.sendUserName(name: list[0].github_id ?? "", type: "user")
                })
                .disposed(by: disposeBag)
            
            secondRankView.rx.tap
                .subscribe(onNext: {
                    self.delegate?.sendUserName(name: list[1].github_id ?? "", type: "user")
                })
                .disposed(by: disposeBag)
            
            thirdRankView.rx.tap
                .subscribe(onNext: {
                    self.delegate?.sendUserName(name: list[2].github_id ?? "", type: "user")
                    
                })
                .disposed(by: disposeBag)
            
            firstRankView.isHidden = false
            secondRankView.isHidden = false
            thirdRankView.isHidden = false
        default:
            print("CustomTopView Error\n")
        }
        
    }
    
    /*
     Organization
     */
    
    func getData(typeList: [TypeRankingModel]){
        stackView.removeFromSuperview()
        addUI()
        disposeBag = DisposeBag()

        print("getData2")
        print(typeList)
        
        switch typeList.count {
        case 0:
            print("CustomTopView None")
            firstRankView.isHidden = true
            secondRankView.isHidden = true
            thirdRankView.isHidden = true
        case 1:
            firstRankView.getData(data: typeList[0])
            
            firstRankView.rx.tap
                .subscribe(onNext: {
                    self.delegate?.sendUserName(name: typeList[0].name ?? "", type: "Organization")
                })
                .disposed(by: disposeBag)
            
            firstRankView.isHidden = false
            secondRankView.isHidden = true
            thirdRankView.isHidden = true
        case 2:
            firstRankView.getData(data: typeList[0])
            secondRankView.getData(data: typeList[1], rank: 2)
            
            firstRankView.rx.tap
                .subscribe(onNext: {
                    self.delegate?.sendUserName(name: typeList[0].name ?? "", type: "Organization")
                })
                .disposed(by: disposeBag)
            
            secondRankView.rx.tap
                .subscribe(onNext: {
                    self.delegate?.sendUserName(name: typeList[1].name ?? "", type: "Organization")
                })
                .disposed(by: disposeBag)
            
            firstRankView.isHidden = false
            secondRankView.isHidden = false
            thirdRankView.isHidden = true
        case 3:
            firstRankView.getData(data: typeList[0])
            secondRankView.getData(data: typeList[1], rank: 2)
            thirdRankView.getData(data: typeList[2] ,rank: 3)
            
            firstRankView.rx.tap
                .subscribe(onNext: {
                    self.delegate?.sendUserName(name: typeList[0].name ?? "", type: "Organization")
                })
                .disposed(by: disposeBag)
            
            secondRankView.rx.tap
                .subscribe(onNext: {
                    self.delegate?.sendUserName(name: typeList[1].name ?? "", type: "Organization")
                })
                .disposed(by: disposeBag)
            
            thirdRankView.rx.tap
                .subscribe(onNext: {
                    self.delegate?.sendUserName(name: typeList[2].name ?? "", type: "Organization")
                })
                .disposed(by: disposeBag)
            
            firstRankView.isHidden = false
            secondRankView.isHidden = false
            thirdRankView.isHidden = false
        default:
            print("CustomTopView Error\n")
            
        }
    }
    
   

}


protocol SendUserName{
    func sendUserName(name: String, type: String)
}
