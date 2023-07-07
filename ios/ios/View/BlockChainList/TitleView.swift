//
//  TitleView.swift
//  ios
//
//  Created by 정호진 on 2023/06/17.
//

import Foundation
import SafariServices
import SnapKit
import UIKit
import RxSwift

final class TitleView: UIView{
    private let disposeBag = DisposeBag()
    private var link: String?
    var delegate: SendingURL?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK: 제목
    private lazy var titleLabel: UILabel  = {
        let label = UILabel()
        label.text = "블록체인 부여 내역"
        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 30)
        return label
    }()
    
    // MARK: 링크 이미지
    private lazy var linkImage: UIButton = {
        let btn = UIButton()
        btn.setImage(UIImage(named: "linkIcon")?.resize(newWidth: 30), for: .normal)
        return btn
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(titleLabel)
        self.addSubview(linkImage)
        
        titleLabel.snp.makeConstraints { make in
            make.center.equalTo(self.snp.center)
        }
        
        linkImage.snp.makeConstraints { make in
            make.trailing.equalToSuperview().offset(-30)
            make.centerY.equalToSuperview()
        }
        clickedLinkButton()
    }
    
    // MARK:
    private func clickedLinkButton(){
        linkImage.rx.tap.subscribe(onNext:{
            self.delegate?.sendingURL(stringURL: self.link ?? "")
        })
        .disposed(by: disposeBag)
    }
    
    func inputData(link: String?){
        addUI()
        self.link = link
    }
    
}

protocol SendingURL{
    func sendingURL(stringURL: String)
}
