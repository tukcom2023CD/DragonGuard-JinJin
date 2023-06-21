//
//  Contributor'sInfoUIView.swift
//  ios
//
//  Created by 정호진 on 2023/06/21.
//

import Foundation
import UIKit
import SnapKit

final class ContributorsInfoUIView: UIView{
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: 전체 랭킹 라벨
    private lazy var allRankingTitleLabel: UILabel = {
        let label = UILabel()
        label.text = "전체랭킹"
        label.font = .systemFont(ofSize: 20)
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    // MARK: 전체 랭킹
    private lazy var allRankingLabel: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 25)
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    // MARK:
    private lazy var firstStack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [allRankingTitleLabel, allRankingLabel])
        stack.axis = .vertical
        stack.distribution = .fillEqually
        stack.spacing = 5
        return stack
    }()
    
    // MARK: 커밋 개수 라벨
    private lazy var commitTitleLabel: UILabel = {
        let label = UILabel()
        label.text = "Commit"
        label.font = .systemFont(ofSize: 20)
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    // MARK: 커밋 개수 라벨
    private lazy var commitLabel: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 25)
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    private lazy var secondStack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [commitTitleLabel, commitLabel])
        stack.axis = .vertical
        stack.distribution = .fillEqually
        stack.spacing = 5
        return stack
    }()
    
    // MARK: 이슈 개수 라벨
    private lazy var issueTitleLabel: UILabel = {
        let label = UILabel()
        label.text = "Issue"
        label.font = .systemFont(ofSize: 20)
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    // MARK: 커밋 개수 라벨
    private lazy var issueLabel: UILabel = {
        let label = UILabel()
        label.font = .systemFont(ofSize: 25)
        label.textColor = .black
        label.textAlignment = .center
        return label
    }()
    
    private lazy var thirdStack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [issueTitleLabel, issueLabel])
        stack.axis = .vertical
        stack.distribution = .fillEqually
        stack.spacing = 5
        return stack
    }()
    
    // MARK:
    private lazy var roundStack: UIStackView = {
        let stack = UIStackView(arrangedSubviews: [firstStack, secondStack, thirdStack])
        stack.axis = .horizontal
        stack.distribution = .fillEqually
        stack.spacing = 10
        return stack
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(roundStack)
        
        roundStack.snp.makeConstraints { make in
            make.top.leading.trailing.bottom.equalToSuperview()
            make.centerX.equalTo(self.snp.centerX)
        }
    }
    
    func inputData(ranking: Int, commit: Int, issue: Int){
        addUI()
        allRankingLabel.text = "\(ranking)"
        commitLabel.text = "\(commit)"
        issueLabel.text = "\(issue)"
        
    }
    
}
