//
//  DetailInfoTableViewCell.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import UIKit
import SnapKit

final class OrganizationDetailTableViewCell: UITableViewCell{
    static let identifier = "OrganizationDetailTableViewCell"
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    
    // MARK:
    private lazy var repositoryView: RepositoryInfoView = {
        let view = RepositoryInfoView()
        view.backgroundColor = .white
        view.layer.cornerRadius = 20
        view.layer.borderWidth = 1
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(repositoryView)
        
        repositoryView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(10)
            make.leading.equalToSuperview().offset(30)
            make.trailing.equalToSuperview().offset(-30)
            make.bottom.equalToSuperview().offset(-10)
        }
    }
    
    func inputData(title: String, imgPath: String, organizationTitle: String){
        addUI()
        
        repositoryView.inputData(imgPath: imgPath, title: title, userName: organizationTitle)
    }
    
}
