//
//  CompareRepositorycontroller.swift
//  ios
//
//  Created by 홍길동 on 2023/02/06.
//

import Foundation
import UIKit

final class CompareRepositoryController: UIViewController{
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.navigationController?.navigationBar.isHidden = false
        self.view.backgroundColor = .white
    }
}

/*
 SwiftUI preview 사용 코드      =>      Autolayout 및 UI 배치 확인용
 
 preview 실행이 안되는 경우 단축키
 Command + Option + Enter : preview 그리는 캠버스 띄우기
 Command + Option + p : preview 재실행
 */

import SwiftUI

struct VCPreViewCompareRepositoryController:PreviewProvider {
    static var previews: some View {
        CompareRepositoryController().toPreview().previewDevice("iPhone 14 pro")
        // 실행할 ViewController이름 구분해서 잘 지정하기
    }
}
