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
    static var ip = "192.168.0.14"
    
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
    static func inputDBMembers() -> Int{
        let inputUserUrl = "http://localhost/api/members"
        /*
         입력 형태 JSON
         {
           "id" : 1,
           "name" : "김승진",
           "githubId" : "ohksj77",
           "commits" : 100,
           "tier" : "SILVER",
           "authStep" : "NONE",
           "profileImage" : "http://abcd.efgh"
         }
        */
        
        return 0
    }
    
    
    
    /// 사용자 티어 정보를 얻는 함수
    /// - Parameter id: inputDBMembers 함수 return값, DB에 저장되는 Id
    static func getUserTier(id: Int){
        let getTierUrl = "http://localhost/api/members/\(id)/tier"
        
    }
    
    
    /// 사용자 커밋 정보를 얻는 함수
    /// - Parameter id: inputDBMembers 함수 return값, DB에 저장되는 Id
    static func getUserCommits(id: Int){
        let getCommitUrl = "http://localhost/api/members/\(id)/commits"
        
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
    
    func getRepoContributorInfo(ip: String, name: String) -> String{
        let url = "http://\(ip)/api/git-repos?name=\(name)"
        return url
    }
    
    
    // 검색 필터링 추가할 때 변경해야함
    static func testUrl(ip: String, page:Int, searchWord: String) -> String {
        let url = "http://\(ip)/api/search?page=\(page)&name=\(searchWord)&type=repositories"
        return url
    }
    
}
