//
//  ViewController.swift
//  ios
//
//  Created by 정호진 on 2023/01/03.
//

import UIKit
import SnapKit


final class MainController: UIViewController {

    let deviceWidth = UIScreen.main.bounds.width
    let deviceHeight = UIScreen.main.bounds.height
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = .white
            
        self.view.addSubview(searchUI)
        
        searchUISetLayout()
        
    }
    
    /*
     UI 코드 작성
     */
    
    lazy var searchUI: UIButton = {
        let mvBtn = UIButton()
        mvBtn.setTitle("검색화면 이동", for: .normal)
        mvBtn.setTitleColor(.black, for: .normal)
        
        mvBtn.addTarget(self, action: #selector(searchUIClicked), for: .touchUpInside)
        return mvBtn
    }()
    
    
    /*
     UI Action 작성
     */
    
    @objc func searchUIClicked(){
        
        let searchPage = SearchPageController()
        self.navigationController?.pushViewController(searchPage, animated: true)
//        searchPage.modalPresentationStyle = UIModalPresentationStyle.fullScreen
//        self.present(searchPage,animated: true)
        
    }
    
    
    /*
     UI AutoLayout 코드 작성
     
     함수 실행시 private으로 시작할 것
     */
    
    private func searchUISetLayout(){
        searchUI.snp.makeConstraints({ make in
            make.top.equalTo(100)
            make.leading.equalTo(10)
            make.trailing.equalTo(-10)
        })
    }

}


/*
    SwiftUI preview 사용하는 코드
 
    preview 실행이 안되는 경우 단축키
    Command + Option + Enter : preview 그리는 캠버스 띄우기
    Command + Option + p : preview 재실행
 */

import SwiftUI

#if DEBUG
extension UIViewController {
    private struct Preview: UIViewControllerRepresentable {
            let viewController: UIViewController

            func makeUIViewController(context: Context) -> UIViewController {
                return viewController
            }

            func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
            }
        }

        func toPreview() -> some View {
            Preview(viewController: self)
        }
}
#endif


struct VCPreViewMain:PreviewProvider {
    static var previews: some View {
        MainController().toPreview().previewDevice("iPhone 14 pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
