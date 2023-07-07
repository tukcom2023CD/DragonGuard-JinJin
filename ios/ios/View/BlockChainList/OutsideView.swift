//
//  OutsideView.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import SnapKit
import UIKit


final class OutsideView: UIView{
    private var dataList: [BlockChainListModel] = []
    var delegate: SendURL?
    
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK:
    private lazy var titleView: TitleView = {
        let view = TitleView()
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.cornerRadius = 20
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        return view
    }()
    
    // MARK: tableView
    private lazy var tableView: UITableView = {
        let table = UITableView()
        table.separatorStyle = .none
        return table
    }()
    
    // MARK: UI 등록
    private func addUI(){
        self.addSubview(titleView)
        titleView.delegate = self
        
        self.addSubview(tableView)
        
        tableView.dataSource = self
        tableView.delegate = self
        tableView.register(BlockChainListTableViewCell.self, forCellReuseIdentifier: BlockChainListTableViewCell.identfider)
        
        titleView.snp.makeConstraints { make in
            make.top.leading.trailing.equalToSuperview()
            make.height.equalTo(70)
        }
        
        tableView.snp.makeConstraints { make in
            make.top.equalTo(titleView.snp.bottom).offset(20)
            make.leading.equalToSuperview().offset(10)
            make.trailing.equalToSuperview().offset(-10)
            make.bottom.equalToSuperview()
        }
    }
    
    func inputData(list: [BlockChainListModel], totalLink: String?){
        self.dataList = list
        addUI()
        titleView.inputData(link: totalLink)
        
    }
    
}

extension OutsideView: UITableViewDelegate, UITableViewDataSource{
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        guard let cell = tableView.dequeueReusableCell(withIdentifier: BlockChainListTableViewCell.identfider, for: indexPath) as? BlockChainListTableViewCell else {return UITableViewCell()}
        
        cell.inputData(time: dataList[indexPath.row].created_at ?? "",
                       type: dataList[indexPath.row].contribute_type ?? "",
                       count: "\(dataList[indexPath.row].amount ?? 0)GTR")
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        
        delegate?.sendURL(url: dataList[indexPath.row].transaction_hash_url ?? "")
        
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int { return dataList.count }
    
}

extension OutsideView: SendingURL{
    func sendingURL(stringURL: String) {
        self.delegate?.sendURL(url: stringURL)
    }
}

protocol SendURL{
    func sendURL(url: String)
}
