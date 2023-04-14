//
//  MyProfileviewModel.swift
//  ios
//
//  Created by 정호진 on 2023/04/14.
//

import Foundation
import RxSwift

final class MyProfileviewModel{
    private let disposeBag = DisposeBag()
    private let service = MyProfileService()
    
    // MARK: 자기 자신 프로필 받아오는 함수
    func getMyProfile() -> Observable<MyProfileInfoModel>{
        return Observable.create { observer in
            
            self.service.getMyProfileInfo()
                .subscribe(onNext: { info in
                    observer.onNext(info)
                })
                .disposed(by: self.disposeBag)
            
            return Disposables.create ()
        }
    }
    
    
}
