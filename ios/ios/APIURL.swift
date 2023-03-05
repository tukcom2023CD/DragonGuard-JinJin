//
//  APIURL.swift
//  ios
//
//  Created by 정호진 on 2023/02/01.
//

import Foundation

// API URL은 여기에 저장해둘 것

final class APIURL{
    
    static let apiUrl = APIURL() 
    static let ip = ""
    
    private init(){}
    
    /// repository, user 검색 url
    /// - Parameters:
    ///   - title: Repository, Github userName
    ///   - type: repositories, users 두가지 타입
    /// - Returns: 검색 결과
    static func getSearchResult(title: String, type: String) -> String {
        let searchUrl = "http://localhost/api/search?page=1&name=\(title)&type=\(type)"
        return searchUrl
    }
    
    ///  DB에 User 정보 넣는 함수
    /// - Returns: DB에 저장된 Id 숫자
    func inputDBMembers(ip: String) -> String{
        let inputUserUrl = "http://\(ip)/api/members"
        return inputUserUrl
    }
    
    func inputWalletAddress(ip: String) -> String{
        let inputUserUrl = "http://\(ip)/api/members/wallet-address"
        return inputUserUrl
    }
    
    
    /// 멤버 정보 조회하는 함수
    /// - Parameter id:inputDBMembers 함수 return값, DB에 저장되는 Id
    /// - Returns: URL
    func getMembersInfo(ip:String, id: Int) -> String{
        let getMemberInfoUrl = "http://\(ip)/api/members/\(id)"
        return getMemberInfoUrl
    }
    
    /// 유저 전체 랭킹 받는 함수
    /// - Parameters:
    ///   - page: 다음 유저 페이지
    ///   - size: 한 번에 받을 크기
    /// - Returns: URL
    func getUserInfo(ip: String, page: Int, size: Int) -> String{
        let url = "http://\(ip)/api/members/ranking?page=\(page)&size=\(size)"
        return url
    }
    
    func getRepoContributorInfo(ip: String, name: String) -> String {
        let url = "http://\(ip)/api/git-repos?name=\(name)"
        return url
    }
    
    
    // 검색 필터링 추가할 때 변경해야함
    static func testUrl(ip: String, page:Int, searchWord: String) -> String {
        let url = "http://\(ip)/api/search?page=\(page)&name=\(searchWord)&type=REPOSITORIES"
        return url
    }
    
    
    // 비교하기 -> 유저비교
    func compareUserAPI(ip: String) -> String{
        let url = "http://\(ip)/api/git-repos/compare/members"
        return url
    }
    
    // 비교하기 -> 레포지토리 비교
    func compareRepoAPI(ip: String) -> String{
        let url = "http://\(ip)/api/git-repos/compare"
        return url
    }
    
    // 비교하기 전에 보내야함
    func compareBeforeAPI(ip: String) -> String{
        let url = "http://\(ip)/api/git-repos/compare/git-repos-members"
        return url
    }
    
    
    // KLIP prepare post API
    func klipPreparePostAPI() -> String{
        let url = "https://a2a-api.klipwallet.com/v2/a2a/prepare"
        return url
    }
    
    // KLIP DeepLink API
    func klipDeepLinkAPI(requestKey: String) -> String{
        let url = "https://klipwallet.com/?target=/a2a?request_key=\(requestKey)"
        return url
    }
    

    // KLIP result get API
    func klipResultGetAPI(requestKey: String) -> String{
        let url = "https://a2a-api.klipwallet.com/v2/a2a/result?request_key=\(requestKey)"
        return url
    }
    
}
