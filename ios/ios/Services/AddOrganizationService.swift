//
//  AddOrganizationService.swift
//  ios
//
//  Created by 정호진 on 2023/03/29.
//

import Foundation
import Alamofire
import RxSwift

// MARK: 조직 등록 과련 서비스 클래스
final class AddOrganizationService{
        
    // MARK: 조직이 없는 경우 새롭게 조직 등록하는 함수
    func addOrganization(name: String, type: String, endPoint: String) -> Observable<Int>{
        let url = APIURL.apiUrl.addOrganization(ip: APIURL.ip)
        
        let body: [String: Any] = [
            "name": name,
            "organization_type": type,
            "email_endpoint": endPoint
        ]
        let access = UserDefaults.standard.string(forKey: "Access")
        print("body \(body)")
        
        return Observable.create { observer in
            AF.request(url,
                       method: .post,
                       parameters: body,
                       encoding: JSONEncoding.default,
                       headers: [
                        "Content-Type": "application/json",
                        "Authorization": "Bearer \(access ?? "")"])
            .validate(statusCode: 200..<201)
            .responseDecodable(of: AddOrganizationCodableModel.self){ response in

                switch response.result{
                case .success(let data):
                    observer.onNext(data.id)
                case .failure(let error):
                    print("addOrganization error!! \(error)")
                }

            }
            
            return Disposables.create()
        }
    }
    
    // MARK: 조직에 멤버 추가하는 함수
    /// - Parameters:
    ///   - organizationId: 조직 아이디
    ///   - email: 사용자 이메일 주소
    /// - Returns: 조직 아이디, 이메일로 인증번호 전송
    func addMemberInOrganization(organizationId: Int, email: String) -> Observable<Int>{
        let url = APIURL.apiUrl.addMemberInOrganization(ip: APIURL.ip)
        let body: [String: Any] = [
            "organization_id" : organizationId,
              "email" : email
        ]
        let encodedString = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed) ?? ""
        let access = UserDefaults.standard.string(forKey: "Access")
        print("body \(body)")
        return Observable.create { observer in
            AF.request(encodedString,
                       method: .post,
                       parameters: body,
                       encoding: JSONEncoding.default,
                       headers: [
                        "Content-Type" : "application/json",
                        "Authorization" : "Bearer \(access ?? "")"
                       ])
            .validate(statusCode: 200..<201)
            .responseDecodable(of: EmailIdDecodingModel.self) { response in
                
                switch response.result{
                case .success(let data):
                    observer.onNext(data.id)
                    print("addMemberInOrganization data \(data.id)")
                case .failure(let error):
                    print("addMemberInOrganization error!\n \(error)")
                }
            }
            
            return Disposables.create()
        }
    }
    
    
}
