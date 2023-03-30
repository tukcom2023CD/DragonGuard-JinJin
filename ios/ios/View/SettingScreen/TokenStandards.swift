//
//  TierTypes.swift
//  ios
//
//  Created by 정호진 on 2023/01/30.
//

import Foundation
import UIKit

final class TokenStandards : UIViewController {
    private let tierArray = ["Bronze", "Silver", "Gold", "Diamond"]
    private let imageArray = [UIImage(named: "bronze"), UIImage(named: "silver"), UIImage(named: "gold"), UIImage(named: "diamond")]
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
        self.navigationItem.title = "토큰 부여 기준"
        self.navigationController?.interactivePopGestureRecognizer?.isEnabled = true
        addUIToView()
    }
    
    /*
     UI 작성
     */
    
    // MARK: 토큰 부여 기준을 담는 StackView
    private lazy var standards: UIStackView = {
       let standards = UIStackView(arrangedSubviews: [commitLabel, issueLabel, prLabel])
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
    
    // MARK: GTR 당 숫자를 담는 StackView
    private lazy var numbers: UIStackView = {
       let numbers = UIStackView(arrangedSubviews: [commitNum, issueNum, prNum])
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
        tier.isScrollEnabled = false
        return tier
    }()
    
    /*
     UI 추가할 때 작성하는 함수
     */
    
    private func addUIToView(){
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
        allUp.snp.makeConstraints ({ make in
            make.leading.equalTo(20)
            make.trailing.equalTo(-20)
            make.top.equalTo(view.safeAreaLayoutGuide)
            make.height.equalTo(UIScreen.main.bounds.height/12)
        })
        
        tierTableView.snp.makeConstraints ({ make in
            make.top.equalTo(allUp.snp.bottom).offset(20)
            make.leading.equalTo(20)
            make.trailing.equalTo(-20)
            make.bottom.equalTo(view.safeAreaLayoutGuide)
        })
        
    }
    
    
}

extension TokenStandards : UITableViewDelegate, UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return tierArray.count
    }
    
    // 중요하니까 한번 더 보고 복습
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: TierTableViewCell.identifier, for: indexPath) as! TierTableViewCell
        cell.inputData(tier: imageArray[indexPath.row]!.resize(newWidth: 100), list: tierArray[indexPath.row])
        cell.backgroundColor = .white
        cell.selectionStyle = .none
        return cell
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return tableView.frame.height/4
    }
    
    
    
    
}
