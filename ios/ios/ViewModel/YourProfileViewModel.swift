//
//  YourProfileViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/07/02.
//

import Foundation
import RxSwift

final class YourProfileViewModel{
    private let disposeBag = DisposeBag()
    private let service = YourProfileService()
    
    func getData(githubId: String) -> Observable<YourProfileModel>{
        
        return Observable.create { observer in
            self.service.getData(githubId: githubId)
                .subscribe { data in
                    observer.onNext(data)
                }
                .disposed(by: self.disposeBag)
            return Disposables.create()
        }
    }
    
    
}
