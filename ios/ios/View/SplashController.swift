//
//  SplashController.swift
//  ios
//
//  Created by 정호진 on 2023/06/18.
//

import Foundation
import UIKit
import SnapKit
import Lottie

// MARK: Splash Screen
final class SplashController: UIViewController{
    
    override func viewDidLoad() {
        super.viewDidLoad()
        addUIIndicator()
        view.backgroundColor = .white
    }
    
    // MARK:
    private lazy var imgView: UIImageView = {
        let imgview = UIImageView()
        imgview.image = UIImage(named: "2")
        return imgview
    }()
    
    // MARK: Splash Screen
    private lazy var indicatorView: LottieAnimationView = {
        let view = LottieAnimationView(name: "splashScreen")
        view.center = self.view.center
        view.backgroundColor = .white
        view.loopMode = .loop
        return view
    }()
    
    // MARK: Add UI Indicator
    private func addUIIndicator(){
        view.addSubview(imgView)
        self.view.addSubview(indicatorView)
        setIndicactorAutoLayout()
        indicatorView.play()
    }
    
    // MARK: Indicator View AutoLayout
    private func setIndicactorAutoLayout(){
        imgView.snp.makeConstraints { make in
            make.top.equalTo(0)
            make.leading.trailing.equalTo(view.safeAreaLayoutGuide)
        }
        
        indicatorView.snp.makeConstraints { make in
            make.top.equalTo(imgView.snp.bottom)
            make.leading.trailing.bottom.equalTo(view.safeAreaLayoutGuide)
//            make.top.leading.trailing.bottom.equalTo(view.safeAreaLayoutGuide)
        }
    }
    
}
