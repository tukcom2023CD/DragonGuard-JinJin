//
//  RepositoriesInOrganizationViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/07/07.
//

import Foundation
import RxSwift

final class RepositoriesInOrganizationViewModel{
    private let disposeBag = DisposeBag()
    private let service = RepositoriesInOrganizationService()
    
    func getData(name: String) -> Observable<RepositoriesInOrganizationModel>{
        
        return Observable.create { observer in
            self.service.getData(name: name).subscribe(onNext:{ data in
                observer.onNext(data)
            })
            .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
}
