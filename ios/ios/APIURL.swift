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
    static let ip = Environment.ip
    
    
    private init(){}
    
    /// repository, user 검색 url
    /// - Parameters:
    ///   - title: Repository, Github userName
    ///   - type: REPOSITORIES, USERS 두가지 타입
    /// - Returns: 검색 결과
    func getSearchResult(ip: String, title: String, page: Int, type: String, filtering: String) -> String {
        if !filtering.isEmpty{
            let searchUrl = "http://\(ip)/api/search?page=\(page)&name=\(title)&type=\(type)&filters=\(filtering)"
            return searchUrl
        }
        else{
            let searchUrl = "http://\(ip)/api/search?page=\(page)&name=\(title)&type=\(type)"
            return searchUrl
        }
    }
    
    ///  DB에 User 정보 넣는 함수
    /// - Returns: DB에 저장된 Id 숫자
    func inputDBMembers(ip: String) -> String{
        let inputUserUrl = "http://\(ip)/api/members"
        return inputUserUrl
    }
    
    // MARK: 지갑 주소 전송
    func inputWalletAddress(ip: String) -> String{
        let inputUserUrl = "http://\(ip)/api/members/wallet-address"
        return inputUserUrl
    }
    
    // MARK:  유저 전체 랭킹 받는 함수
    /// - Parameters:
    ///   - page: 다음 유저 페이지
    ///   - size: 한 번에 받을 크기
    /// - Returns: URL
    func getUserInfo(ip: String, page: Int, size: Int) -> String{
        let url = "http://\(ip)/api/members/ranking?page=\(page)&size=\(size)"
        return url
    }
    
    // MARK:
    func getRepoContributorInfo(ip: String, name: String) -> String {
        let url = "http://\(ip)/api/git-repos?name=\(name)"
        return url
    }
    
    // MARK: 비교하기 -> 유저비교
    func compareUserAPI(ip: String) -> String{
        let url = "http://\(ip)/api/git-repos/compare/members"
        return url
    }
    
    // MARK: 비교하기 -> 레포지토리 비교
    func compareRepoAPI(ip: String) -> String{
        let url = "http://\(ip)/api/git-repos/compare"
        return url
    }
    
    // MARK: 비교하기 전에 보내야함
    func compareBeforeAPI(ip: String) -> String{
        let url = "http://\(ip)/api/git-repos/compare/git-repos-members"
        return url
    }
    
    
    // MARK: KLIP prepare post API
    func klipPreparePostAPI() -> String{
        let url = "https://a2a-api.klipwallet.com/v2/a2a/prepare"
        return url
    }
    
    // MARK: KLIP DeepLink API
    func klipDeepLinkAPI(requestKey: String) -> String{
        let url = "https://klipwallet.com/?target=/a2a?request_key=\(requestKey)"
        return url
    }
    

    // KLIP result get API
    func klipResultGetAPI(requestKey: String) -> String{
        let url = "https://a2a-api.klipwallet.com/v2/a2a/result?request_key=\(requestKey)"
        return url
    }
    
    // Github 로그인 하기 위해 백엔드에 요청
    func callBackendForGithubLogin(ip: String) -> String{
        let url = "http://\(ip)/api/oauth2/authorize/github"
        return url
    }
    
    
    /// 멤버 정보 조회하는 함수
    /// - Parameter id:inputDBMembers 함수 return값, DB에 저장되는 Id
    /// - Returns: URL
    func getMembersInfo(ip:String) -> String{
        let getMemberInfoUrl = "http://\(ip)/api/members/me"
        return getMemberInfoUrl
    }
    
    func getRefreshToken(ip: String) -> String{
        let url = "http://\(ip)/api/auth/refresh"
        return url
    }
    
    // MARK: 조직 검색
    func searchOrganizationList(ip: String, name: String, type: String, page: Int, size: Int) -> String{
        let url = "http://\(ip)/api/organizations/search?type=\(type)&name=\(name)&page=\(page)&size=\(size)"
        return url
    }
    
    // MARK: 조직 등록
    func addOrganization(ip: String) -> String{
        let url = "http://\(ip)/api/organizations"
        return url
    }
    
    // MARK: 조직 아이디 조회
    func getOrganizationId(ip: String, name: String) -> String{
        let url = "http://\(ip)/api/organizations/search-id?name=\(name)"
        return url
    }
    
    // MARK: 조직에 멤버 등록
    func addMemberInOrganization(ip: String) -> String{
        let url = "http://\(ip)/api/organizations/add-member"
        return url
    }
    
    // MARK: 인증 번호 재전송
    func sendEmailToAuth(ip: String) -> String{
        let url = "http://\(ip)/api/email/send"
        return url
    }
    
    // MARK: 유효한 인증 번호인지 확인
    func checkEmailValidCode(ip: String, id: Int, code: Int) -> String{
        let url = "http://\(ip)/api/email/check?id=\(id)&code=\(code)"
        return url
    }
    
    // MARK: 인증 번호 삭제
    func removeCertificatedNumber(ip: String) -> String{
        let url = "http://\(ip)/api/email/1"
        return url
    }
    
    // MARK: 전체 조직 랭킹 조회
    func allOrganizationRanking(ip: String) -> String{
        let url = "http://\(ip)/api/organizations/ranking/all"
        return url
    }
    
    // MARK: 타입 필터링 후 랭킹 조회
    func typeFilterOrganiRanking(ip: String, type: String, page: Int, size: Int) -> String{
        let url = "http://\(ip)/api/organizations/ranking?type=\(type)&page=\(page)&size=\(size)"
        return url
    }
    
    // MARK: 조직 내부 나의 랭킹 조회
    func organizationInMyRanking(ip: String, id: Int ,type: String, page: Int, size: Int) -> String{
        let url = "http://\(ip)/api/members/ranking/organization?organizationId=\(id)&page=\(page)&size=\(size)"
        return url
    }
    
}

