//
//  RepositoryListTableViewCell.swift
//  ios
//
//  Created by 정호진 on 2023/06/21.
//

import Foundation
import UIKit
import SnapKit

final class RepositoryListTableViewCell: UITableViewCell{
    static let identfier = "RepositoryListTableViewCell"
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
        
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK:
    private lazy var repositoryListTableViewInUIView: RepositoryListTableViewInUIView = {
        let view = RepositoryListTableViewInUIView()
        view.layer.shadowOffset = CGSize(width: 0, height: 10)
        view.layer.shadowOpacity = 0.5
        view.layer.cornerRadius = 20
        view.layer.borderWidth = 1
        view.layer.borderColor = CGColor(red: 0.0, green: 0.0, blue: 0.0, alpha: 0.5)
        view.layer.masksToBounds = true
        view.backgroundColor = .white
        view.clipsToBounds = true
        
        return view
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(repositoryListTableViewInUIView)
        
        repositoryListTableViewInUIView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(10)
            make.leading.equalToSuperview().offset(30)
            make.trailing.equalToSuperview().offset(-30)
            make.bottom.equalToSuperview().offset(-10)
        }
    }
    
    func inputData(title: String?, imgPath: String?, repoName: String?){
        addUI()
        
        repositoryListTableViewInUIView.inputData(imgPath: imgPath, title: title, userName: repoName)
    }
    
    
    
    
}
