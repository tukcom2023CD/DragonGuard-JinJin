//
//  BlockChainListController.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import UIKit
import SnapKit
import RxSwift
import SafariServices

// MARK: 블록체인 부여 내역
final class BlockChainListController: UIViewController{
    var blockchainUrl: String?
    private let disposeBag = DisposeBag()
    private let viewModel = BlockchainViewModel()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        view.backgroundColor = .white
        getData()
        
    }
    
    
    // MARK: 뒤로가기 버튼
    private lazy var backBtn: UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(named: "backBtn")?.resize(newWidth: 30), for: .normal)
        return btn
    }()
    
    // MARK:
    private lazy var descriptionLabel: UILabel = {
        let label = UILabel()
        label.text = "*세부내역을 누르면 링크 연결됩니다."
        label.font = .systemFont(ofSize: 15)
        label.layer.opacity = 0.5
        return label
    }()
    
    // MARK:
    private lazy var outsideView: OutsideView = {
        let view = OutsideView()
        view.backgroundColor = .white
        view.layer.shadowOpacity = 1
        view.layer.shadowOffset = CGSize(width: 3, height: 3)
        view.layer.shadowColor = .init(red: 200/255, green: 200/255, blue: 200/255, alpha: 1)
        view.layer.cornerRadius = 20
        return view
    }()
    
    
    // MARK:
    private func addUI(){
        view.addSubview(backBtn)
        view.addSubview(descriptionLabel)
        view.addSubview(outsideView)
        
        backBtn.snp.makeConstraints { make in
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(10)
            make.top.equalTo(view.safeAreaLayoutGuide).offset(10)
        }
        
        descriptionLabel.snp.makeConstraints { make in
            make.top.equalTo(backBtn.snp.bottom).offset(10)
            make.trailing.equalTo(outsideView.snp.trailing)
        }
        
        outsideView.snp.makeConstraints { make in
            make.top.equalTo(descriptionLabel.snp.bottom)
            make.leading.equalTo(view.safeAreaLayoutGuide).offset(20)
            make.trailing.equalTo(view.safeAreaLayoutGuide).offset(-20)
            make.bottom.equalTo(view.safeAreaLayoutGuide).offset(-30)
        }
    }
    
    // MARK:
    private func getData(){
        
        self.viewModel.getData()
            .subscribe(onNext: { list in
                self.addUI()
                self.outsideView.delegate = self
                self.outsideView.inputData(list: list, totalLink: self.blockchainUrl)
            })
            .disposed(by: disposeBag)
        
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

extension BlockChainListController: SendURL{
    func sendURL(url: String) {
        let url = NSURL(string: url)
        let blogSafariView: SFSafariViewController = SFSafariViewController(url: url! as URL)
        self.present(blogSafariView, animated: true)
    }
}
