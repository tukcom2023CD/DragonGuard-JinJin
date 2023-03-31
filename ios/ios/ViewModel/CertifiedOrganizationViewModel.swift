//
//  SearchOraganizationViewModel.swift
//  ios
//
//  Created by 정호진 on 2023/03/22.
//

import Foundation
import RxSwift

// MARK: 조직 인증 ViewModel
final class CertifiedOrganizationViewModel{
    static let viewModel = CertifiedOrganizationViewModel()
    let searchService = SearchOraganizationService()  /// 조직 검색 서비스
    let addService = AddOrganizationService()   /// 조직 등록하는 서비스
    let emailService = EmailService()   /// 이메일 서비스
    let disposeBag = DisposeBag()
    var page = 1
    var size = 20
    
    // MARK: 사용자가 검색한 조직 리스트를 서버에서 받아 반환하는 함수
    /// - Parameters:
    ///   - name: 검색한 조직 이름
    ///   - type: UNIVERSITY, COMPANY, HIGH_SCHOOL, ETC
    ///   - check: 새로운 조직을 검색했을 때 false로 들어옴
    /// - Returns: 검색한 조직 리스트들 반환
    func getOrganizationList(name: String, type: String, check: Bool) -> Observable<[SearchOrganizationListModel]>{
        // check가 거짓인 경우 page 초기화
        if !check{
            self.page = 1
        }
        
        return Observable.create(){ observer in
            self.searchService.getOrganizationListService(name: name, type: type, page: self.page, size: self.size)
                .subscribe(onNext: { data in
                    print("getOrganizationList data \n \(data)")
                    observer.onNext(data)
                    self.page += 1
                })
                .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
    
    // MARK: 조직 등록하는 함수
    /// - Parameters:
    ///   - name: 조직 이름
    ///   - type: 조직 타입
    ///   - endPoint: 조직 이메일 endPoint
    /// - Returns: response Data
    func addOrganization(name: String, type: String, endPoint: String) -> Observable<Int>{
        return Observable.create { observer in
            self.addService.addOrganization(name: name,
                                            type: type,
                                            endPoint: endPoint)
            .subscribe { data in
                print("addOrganization viewModel \(data)")
            }
            .disposed(by: self.disposeBag)
            return Disposables.create()
        }
        
    }
    
    // MARK: 이메일 인증번호 유효한지 확인하는 함수
    /// - Parameters:
    ///   - id: 조직 아디이
    ///   - code: 이메일 인증 번호
    /// - Returns: True, False
    func checkValidNumber(id: Int, code: Int) -> Observable<Bool>{
        
        return Observable.create { observer in
            self.emailService.checkValidNumber(id: id, code: code)
                .subscribe { checkValid in
                    print("checkValidNumber \(checkValid)")
                    observer.onNext(checkValid)
                }
                .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    
    }
    
    // MARK: 조직에 멤버 추가하는 함수
    /// - Parameters:
    ///   - organizationId: 조직 아이디
    ///   - email: 사용자 이메일 주소
    /// - Returns: 조직 아이디
    func addMember(organizationId: Int, email: String) -> Observable<Int>{
        
        return Observable.create { observer in
            self.addService.addMemberInOrganization(organizationId: organizationId, email: email)
                .subscribe { data in
                    observer.onNext(data)
                }
                .disposed(by: self.disposeBag)
            
            return Disposables.create()
        }
    }
    
}
