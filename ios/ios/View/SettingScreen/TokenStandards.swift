//
//  TierTypes.swift
//  ios
//
//  Created by 정호진 on 2023/01/30.
//

import Foundation
import UIKit
import RxSwift

final class TokenStandards : UIViewController {
    private let tierArray = ["Bronze", "Silver", "Gold", "Platinum", "Diamond"]
    private let imageArray = [UIImage(named: "bronze"), UIImage(named: "silver"), UIImage(named: "gold"), UIImage(named: "platinum"), UIImage(named: "diamond")]
    private let disposeBag = DisposeBag()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        
        addUIToView()
    }
    
    /*
     UI 작성
     */
    
    // MARK: 뒤로가기 버튼
    private lazy var backBtn: UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(named: "backBtn")?.resize(newWidth: 30), for: .normal)
        return btn
    }()
    
    // MARK: 설정 라벨
    private lazy var titleLabel: UILabel = {
        let label = UILabel()
        label.text = "토큰 부여 기준"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 25)
        return label
    }()
    
    // MARK: 토큰 부여 기준을 담는 StackView
    private lazy var standards: UIStackView = {
       let standards = UIStackView(arrangedSubviews: [commitLabel, issueLabel, prLabel, codeReviewLabel])
        standards.axis = .horizontal
        standards.distribution = .fillEqually
        return standards
    }()
    
    // MARK: 토큰 부여 기준) 커밋의 경우
    private lazy var commitLabel: UILabel = {
       let label = UILabel()
        label.text = "Commit"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14)
        label.textAlignment = .center
        return label
    }()
    
    // MARK: 토큰 부여 기준) 이슈의 경우
    private lazy var issueLabel: UILabel = {
       let label = UILabel()
        label.text = "Issue"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14)
        label.textAlignment = .center
        return label
    }()
    
    // MARK: 토큰 부여 기준) PR의 경우
    private lazy var prLabel: UILabel = {
       let label = UILabel()
        label.text = "Pull-Request"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14)
        label.textAlignment = .center
        return label
    }()
    
    // MARK: 토큰 부여 기준) PR의 경우
    private lazy var codeReviewLabel: UILabel = {
       let label = UILabel()
        label.text = "Code Review"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14)
        label.textAlignment = .center
        return label
    }()
    
    // MARK: GTR 당 숫자를 담는 StackView
    private lazy var numbers: UIStackView = {
       let numbers = UIStackView(arrangedSubviews: [commitNum, issueNum, prNum, codeReviewNum])
        numbers.axis = .horizontal
        numbers.distribution = .fillEqually
        return numbers
    }()
    
    // MARK: 1 GTR에 해당하는 커밋 수
    private lazy var commitNum: UILabel = {
       let label = UILabel()
        label.text = "1"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14)
        label.textAlignment = .center
        return label
    }()
    
    // MARK: 1 GTR에 해당하는 이슈 수
    private lazy var issueNum: UILabel = {
       let label = UILabel()
        label.text = "1"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14)
        label.textAlignment = .center
        return label
    }()
    
    // MARK: 1 GTR에 해당하는 PR 수
    private lazy var prNum: UILabel = {
       let label = UILabel()
        label.text = "1"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14)
        label.textAlignment = .center
        return label
    }()
    
    // MARK: 1 GTR에 해당하는 code review 수
    private lazy var codeReviewNum: UILabel = {
       let label = UILabel()
        label.text = "1"
        label.textColor = .black
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14)
        label.textAlignment = .center
        return label
    }()
    
    // MARK: 전체를 담는 StackView
    private lazy var allUp: UIStackView = {
       let all = UIStackView(arrangedSubviews: [standards, numbers])
        all.axis = .vertical
        all.distribution = .fillEqually
        return all
    }()
    
    // MARK: 티어 종류 담는 TableView
    private lazy var tierTableView: UITableView = {
       let tier = UITableView()
        tier.backgroundColor = .white
        tier.isScrollEnabled = true
        return tier
    }()
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUIToView(){
        view.addSubview(titleLabel)
        view.addSubview(backBtn)
        view.addSubview(allUp)
        view.addSubview(tierTableView)
        
        allUpAutoLayout()
        
        tierTableView.delegate = self
        tierTableView.dataSource = self
        tierTableView.register(TierTableViewCell.self, forCellReuseIdentifier: TierTableViewCell.identifier)
    }
    
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func allUpAutoLayout(){
        
        titleLabel.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.centerX.equalTo(view.snp.centerX)
        }
        
        backBtn.snp.makeConstraints { make in
            make.top.equalTo(view.safeAreaLayoutGuide).offset(15)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
        }
        
        allUp.snp.makeConstraints ({ make in
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(20)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-20)
            make.top.equalTo(titleLabel.snp.bottom).offset(10)
            make.height.equalTo(view.safeAreaLayoutGuide.layoutFrame.height/12)
        })
        
        tierTableView.snp.makeConstraints ({ make in
            make.top.equalTo(allUp.snp.bottom).offset(20)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(20)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-20)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
        
        clickedBackBtn()
    }
    
    // MARK:
    private func clickedBackBtn(){
        backBtn.rx.tap.subscribe(onNext: {
            self.dismiss(animated: true)
        })
        .disposed(by: disposeBag)
    }
    
    
}

extension TokenStandards : UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tierArray.count
    }
    
    // 중요하니까 한번 더 보고 복습
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: TierTableViewCell.identifier, for: indexPath) as! TierTableViewCell
        cell.inputData(tier: imageArray[indexPath.row]!,
                       list: tierArray[indexPath.row])
        cell.backgroundColor = .white
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return tableView.frame.height/4
    }
    
    
    
    
}
