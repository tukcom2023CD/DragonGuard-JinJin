//
//  ContributorAutoCollectionView.swift
//  ios
//
//  Created by 정호진 on 2023/06/22.
//

import Foundation
import UIKit
import SnapKit

final class ContributorAutoUIView: UIView{
    private var titleList: [String] = ["Commit", "Issue", "Pull-Request", "Reviews"]
    private var numList: [Int] = []
    private var nowPage: Int = 0
    var timer: Timer?
    
    override init(frame: CGRect) {
        super.init(frame: frame)
        
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
    }
    
    // MARK:
    private lazy var collectionView: UICollectionView = {
        let layout = UICollectionViewFlowLayout()
        layout.scrollDirection = .horizontal
        let cv = UICollectionView(frame: .zero, collectionViewLayout: layout)
        
        return cv
    }()
    
    // MARK:
    private func addUI(){
        self.addSubview(collectionView)
        
        collectionView.dataSource = self
        collectionView.delegate = self
        collectionView.register(ContributorAutoCollectionViewCell.self, forCellWithReuseIdentifier: ContributorAutoCollectionViewCell.identifier)
        
        collectionView.snp.makeConstraints { make in
            make.top.equalToSuperview().offset(10)
            make.leading.equalToSuperview().offset(10)
            make.trailing.equalToSuperview().offset(-10)
            make.bottom.equalToSuperview().offset(-10)
        }
    }
    
    func inputData(commit: Int, issue: Int, pr: Int, reviews: Int){
        bannerTimer()
        numList = []
        numList.append(commit)
        numList.append(issue)
        numList.append(pr)
        numList.append(reviews)
        addUI()
    }
    
    // 2초마다 실행되는 타이머
    func bannerTimer() {
        var index = 0
        self.timer = Timer.scheduledTimer(withTimeInterval: 2, repeats: true) { (Timer) in
            index += 1
            self.bannerMove()
        }
        
    }
    
    func stopTimer(){
        timer?.invalidate()
    }
    
    // 배너 움직이는 매서드
    func bannerMove() {
        DispatchQueue.main.async {
            // 현재페이지가 마지막 페이지일 경우
            if self.nowPage == self.numList.count-1 {
                // 맨 처음 페이지로 돌아감
                self.collectionView.scrollToItem(at: NSIndexPath(item: 0, section: 0) as IndexPath, at: .right, animated: true)
                self.nowPage = 0
                return
            }
            // 다음 페이지로 전환
            self.nowPage += 1
            self.collectionView.scrollToItem(at: NSIndexPath(item: self.nowPage, section: 0) as IndexPath, at: .right, animated: true)
        }
    }
    
}

extension ContributorAutoUIView: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout{
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        guard let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ContributorAutoCollectionViewCell.identifier, for: indexPath) as? ContributorAutoCollectionViewCell else { return UICollectionViewCell() }
        
        cell.inputData(title: titleList[indexPath.row], num: numList[indexPath.row])
        
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
        return CGSize(width: collectionView.frame.width, height: collectionView.frame.height)
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return 4
    }
    
    //컬렉션뷰 감속 끝났을 때 현재 페이지 체크
   func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
       nowPage = Int(scrollView.contentOffset.x) / Int(scrollView.frame.width)
   }
}
