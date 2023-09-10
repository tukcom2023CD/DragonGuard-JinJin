//
//  DetailInfoTableViewCell.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import UIKit
import SnapKit

final class DetailInfoTableViewCell: UITableViewCell{
    static let identifier = "DetailInfoTableViewCell"
    
    override init(style: UITableViewCell.CellStyle, reuseIdentifier: String?) {
        super.init(style: style, reuseIdentifier: reuseIdentifier)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: Organization View
    private lazy var organzationView: OrganizationInfoView = {
        let view = OrganizationInfoView()
        view.backgroundColor = .white
        view.layer.cornerRadius = 20
        view.layer.borderWidth = 1
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 0, height: 3)
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK: Repository View
    private lazy var repositoryView: RepositoryInfoView = {
        let view = RepositoryInfoView()
        view.backgroundColor = .white
        view.layer.cornerRadius = 20
        view.layer.borderWidth = 1
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 0, height: 3)
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK:
    private func addUI_Organizaion(){
        self.addSubview(organzationView)
        
        organzationView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(10)
            make.leading.equalToSuperview().offset(30)
            make.trailing.equalToSuperview().offset(-30)
            make.bottom.equalToSuperview().offset(-10)
        }
    }
    
    func inputData_Organizaion(data: Organ_InfoModel?){
        addUI_Organizaion()
        
        organzationView.inputData(imgPath: data?.profile_image ?? "", title: data?.name ?? "none")
    }
    
    // MARK:
    private func addUI_Repository(){
        addSubview(repositoryView)
        
        repositoryView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(10)
            make.leading.equalToSuperview().offset(30)
            make.trailing.equalToSuperview().offset(-30)
            make.bottom.equalToSuperview().offset(-10)
        }
        
    }
  
    func inputData_Repository(title: String, imgPath: String, myId: String){
        addUI_Repository()
        
        let user = title.split(separator: "/")[0]
        let title = title.split(separator: "/")[1]
        repositoryView.inputData(imgPath: imgPath, title: String(title), userName: String(user))
    }
    
}
