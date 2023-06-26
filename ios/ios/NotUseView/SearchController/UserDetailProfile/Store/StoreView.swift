////
////  StoreView.swift
////  ios
////
////  Created by 정호진 on 2023/04/13.
////
//
//import Foundation
//import UIKit
//import SnapKit
//
//// MARK: 창고 관련 UI
//final class StoreView: UIView{
//    private let etcList = ["Commit","Issue","PR","Comment"]
//
//    override init(frame: CGRect) {
//        super.init(frame: frame)
//        addUIToView()
//    }
//    
//    required init?(coder: NSCoder) {
//        super.init(coder: coder)
//    }
//    
//    
//    /*
//     UI
//     */
//    
//    // MARK: tier Image
//    private lazy var tierImage: UIImageView = {
//        let img = UIImageView()
//        img.image = UIImage(named: "pomi")?.resize(newWidth: 100)
//        return img
//    }()
//    
//    // MARK: 랭킹 표시할 막대기
//    private lazy var rankingBar: UIProgressView = {
//        let view = UIProgressView()
//        view.trackTintColor = .green
//        view.progressTintColor = .systemBlue
//        view.progress = 0.1
//        view.progressTintColor = .red
//        return view
//    }()
//    
//    // MARK: 티어 중 토큰 최소값
//    private lazy var minLabel: UILabel = {
//        let label = UILabel()
//        label.text = "100"
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14)
//        return label
//    }()
//    
//    // MARK: 티어 중 토큰 최대값
//    private lazy var maxLabel: UILabel = {
//        let label = UILabel()
//        label.text = "199"
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 14)
//        return label
//    }()
//    
//    // MARK: 티어 중 토큰 최대값
//    private lazy var userTokenCountLabel: UILabel = {
//        let label = UILabel()
//        label.text = "120"
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 12)
//        return label
//    }()
// 
//    // MARK: 조직 UI View
//    private lazy var organizationView: UIView = {
//        let view = UIView()
//        return view
//    }()
//    
//    // MARK: 인증된 사용자 '조직 이름' Title
//    private lazy var organizationTitleLabel: UILabel = {
//        let label = UILabel()
//        label.text = "나무꾼 협회"
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
//        return label
//    }()
//    
//    // MARK: 인증된 사용자 조직 이름 넣을 라벨
//    private lazy var organizationLabel: UILabel = {
//        let label = UILabel()
//        label.text = "Unknown"
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 17)
//        return label
//    }()
//    
//    // MARK: 협회 내부 랭킹 Title
//    private lazy var organizationRankingTitle: UILabel = {
//        let label = UILabel()
//        label.text = "협회 랭킹"
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 20)
//        return label
//    }()
//    
//    // MARK: 협회 내부 랭킹
//    private lazy var organizationRanking: UILabel = {
//        let label = UILabel()
//        label.text = "UnRank"
//        label.font = UIFont(name: "IBMPlexSansKR-SemiBold", size: 17)
//        return label
//    }()
//    
//    
//    // MARK: Commit, issue, PR, Comment 표현할 CollectionView
//    private lazy var etcCollectionView: UICollectionView = {
//        let collectionLayout = UICollectionViewFlowLayout()
//        collectionLayout.scrollDirection = .horizontal
//        let cv = UICollectionView(frame: .zero, collectionViewLayout: collectionLayout)
//        cv.backgroundColor = .clear
//        return cv
//    }()
//    
//    
//    
//    /*
//     Add UI & AutoLayout
//     */
// 
//    // MARK: Add UI To view
//    private func addUIToView(){
//        self.addSubview(tierImage)
//        
//        self.addSubview(rankingBar)
//        self.addSubview(minLabel)
//        self.addSubview(maxLabel)
//        self.addSubview(userTokenCountLabel)
//        
//        self.addSubview(organizationView)
//        organizationView.addSubview(organizationTitleLabel)
//        organizationView.addSubview(organizationLabel)
//        organizationView.addSubview(organizationRankingTitle)
//        organizationView.addSubview(organizationRanking)
//        
//        self.addSubview(etcCollectionView)
//        etcCollectionView.dataSource = self
//        etcCollectionView.delegate = self
//        etcCollectionView.register(ETCCollectionViewCell.self, forCellWithReuseIdentifier: ETCCollectionViewCell.identifier)
//        
//        setAutoLayout()
//    }
//    
//    // MARK: AutoLayout
//    private func setAutoLayout(){
//        
//        /// Tier Image
//        tierImage.snp.makeConstraints { make in
//            make.top.equalTo(self.snp.top).offset(10)
//            make.leading.equalTo(self.snp.leading).offset(20)
//        }
//        
//        /// 랭킹 순위 보여줄 프로그레스 바
//        rankingBar.snp.makeConstraints { make in
//            make.top.equalTo(tierImage.snp.bottom).offset(30)
//            make.leading.equalToSuperview().offset(20)
//            make.trailing.equalToSuperview().offset(-20)
//            make.height.equalTo(7)
//        }
//        
//        /// 티어 조건 최소값
//        minLabel.snp.makeConstraints { make in
//            make.leading.equalTo(rankingBar.snp.leading)
//            make.bottom.equalTo(rankingBar.snp.top)
//        }
//        
//        /// 티어 조건 최대값
//        maxLabel.snp.makeConstraints { make in
//            make.trailing.equalTo(rankingBar.snp.trailing)
//            make.bottom.equalTo(rankingBar.snp.top)
//        }
//        
//        /// 유저 토큰 개수
//        userTokenCountLabel.snp.makeConstraints { make in
//            make.top.equalTo(rankingBar.snp.bottom)
//            make.leading.equalTo(rankingBar.snp.leading)
//        }
//        
//        /// 조직 관련 UI View
//        organizationView.snp.makeConstraints { make in
//            make.top.equalTo(tierImage.snp.top)
//            make.leading.equalTo(tierImage.snp.trailing).offset(20)
//            make.trailing.equalToSuperview().offset(-20)
//            make.bottom.equalTo(tierImage.snp.bottom)
//        }
//        
//        /// 조직 이름 Title
//        organizationTitleLabel.snp.makeConstraints { make in
//            make.bottom.equalTo(organizationView.snp.centerY)
//            make.leading.equalTo(organizationView.safeAreaLayoutGuide)
//        }
//        
//        /// 사용자 조직 이름
//        organizationLabel.snp.makeConstraints { make in
//            make.centerY.equalTo(organizationTitleLabel.snp.centerY)
//            make.leading.equalTo(organizationTitleLabel.snp.trailing).offset(20)
//        }
//        
//        /// 조직 내부 랭킹 Title
//        organizationRankingTitle.snp.makeConstraints { make in
//            make.top.equalTo(organizationView.snp.centerY)
//            make.leading.equalTo(organizationView.safeAreaLayoutGuide)
//        }
//        
//        /// 조직 내부 사용자 랭킹
//        organizationRanking.snp.makeConstraints { make in
//            make.centerY.equalTo(organizationRankingTitle.snp.centerY)
//            make.leading.equalTo(organizationRankingTitle.snp.trailing).offset(20)
//        }
//        
//        /// Etc CollectionView
//        etcCollectionView.snp.makeConstraints { make in
//            make.top.equalTo(rankingBar.snp.bottom).offset(30)
//            make.leading.equalTo(10)
//            make.trailing.equalTo(-10)
//            make.bottom.equalTo(self.safeAreaLayoutGuide).offset(-10)
//        }
//        
//    }
//    
//}
//
//// MARK: Commit, issue, pr, comment 들어갈 collectionview
//extension StoreView: UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout {
//    
//    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
//        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: ETCCollectionViewCell.identifier, for: indexPath) as! ETCCollectionViewCell
//        
//        cell.inputInfo(title: etcList[indexPath.row], count: 1)
//        cell.backgroundColor = UIColor(red: 255/255, green: 194/255, blue: 194/255, alpha: 0.5) /* #ffc2c2 */
//        cell.layer.cornerRadius = 15
//        cell.layer.borderWidth = 1
//        return cell
//    }
//    
//    func collectionView(_ collectionView: UICollectionView, layout collectionViewLayout: UICollectionViewLayout, sizeForItemAt indexPath: IndexPath) -> CGSize {
//        let cellHeight = collectionView.bounds.height
//        let cellWidth = collectionView.bounds.width*2/5
//        
//        return CGSize(width: cellWidth, height: cellHeight)
//    }
//    
//    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int { return etcList.count }
//    
//}
//
//
