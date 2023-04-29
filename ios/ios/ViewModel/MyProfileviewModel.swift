//
//  MyProfileviewModel.swift
//  ios
//
//  Created by 정호진 on 2023/04/14.
//

import Foundation
import RxSwift

final class MyProfileviewModel{
    static let viewModel = MyProfileviewModel()
    private let disposeBag = DisposeBag()
    private let service = MyProfileService()
    
    private init(){}
    
    // MARK: 자기 자신 프로필 받아오는 함수
    func getMyProfile(githubId: String) -> Observable<MyProfileInfoModel>{
        return Observable.create { observer in
            
            self.service.getMyProfileInfo(githubId: githubId)
                .subscribe(onNext: { info in
                    observer.onNext(info)
                })
                .disposed(by: self.disposeBag)
            
            return Disposables.create ()
        }
    }
    
    
}
