//
//  EmailService.swift
//  ios
//
//  Created by 정호진 on 2023/03/29.
//

import Foundation
import RxSwift
import Alamofire

// MARK: 이메일 인증 관련 서비스
final class EmailService{
            
    // MARK: 이메일 인증번호 유효한지 확인하는 함수
    /// - Parameters:
    ///   - id: 조직 아이디
    ///   - code: 인증 번호
    /// - Returns: true, false
    func checkValidNumber(id: Int, code: Int) -> Observable<Bool>{
        let url = APIURL.apiUrl.checkEmailValidCode(ip: APIURL.ip,
                                                    id: id,
                                                    code: code)
        
        return Observable.create { observer in
            
            AF.request(url,
                       method: .get,
                       headers: ["Authorization": "Bearer \(Environment.jwtToken ?? "")"])
            .responseDecodable(of: CheckEmailModel.self){ response in
                
                switch response.result{
                case .success(let data):
                    observer.onNext(data.validCode)
                case .failure(let error):
                    print("checkValidNumber error! \(error)")
                }
            }
        
            return Disposables.create()
        }
    }
    
    
    
    
}
