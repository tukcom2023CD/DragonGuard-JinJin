# DragonGuard-JinJin
## 블록체인 기반 깃 랭크 시스템 - _GitRank_

개인별로 또는 조직별로 깃허브를 얼마나 활용하는지 경쟁하며 순위를 매기는 기능을 제공

## 시스템 개요
- Github는 무료 Git 저장소로, 2023년 1월에 사용자 수 1억 명을 돌파했다. 하지만 유저들의 Github 활용도를 한 눈에 비교하거나 역량을 정량적으로 판단하기 어려웠다.
- 이에 따라 개발자들에 대한 현재 개발 역량뿐만 아니라 타인 또는 Repository, 대학 및 기관 등 여러 방면에서 필터링한 정보를 파악하는 시스템을 정량적인 분석과 평가를 거쳐 건전한 경쟁 심리를 유도하고자 한다.

## System Architecture
![image](https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/5dbf7b2e-cbc6-4412-882f-f20d848627a8)
## Cloud Infra
![image](https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/dbbbd6cf-5d62-4b17-91cb-292dee847027)
## CI / CD Flow
![image](https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/7065b430-d8ee-4244-97b7-c1c7b24acf10)
<details>
<summary>Jenkins</summary>
<div>
<img src=https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/109d420f-9c59-4db8-be39-480b026d375f/>
</div>
</details>
<details>
<summary>ArgoCD</summary>
<div>
<img src=https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/a86bd15c-b3e1-440d-8b20-54b5f91285ec/>
</div>
</details>

## 기능 명세

|index|분류|기능|index|분류|기능|
|:----:|:----:|:----:|:----:|:----:|:----:|
|_#01_|`랭킹`|조직 전체 랭킹|_#10_|`상세 조회`|Repository 내부 기여도 조회|
|_#02_|`랭킹`|조직 타입별 랭킹|_#11_|`상세 조회`|사용자 상세 조회|
|_#03_|`랭킹`|조직 내부의 개인 랭킹|_#12_|`상세 조회`|블록체인 토큰 부여 내역 조회|
|_#04_|`랭킹`|전체 사용자 랭킹|_#13_|`인증`|Github 소셜 로그인 (OAuth2, Jwt)|
|_#05_|`비교`|Repository 정보 비교|_#14_|`인증`|KLIP 인증|
|_#06_|`비교`|Repository 내부 기여자 비교|_#15_|`인증`|조직 인증 (이메일)|
|_#07_|`검색`|Repository 필터링별 검색|_#16_|`관리자`|조직 등록 요청 관리|
|_#08_|`검색`|사용자 검색|_#17_|`도우미`|FAQ|
|_#09_|`메인`|메인 화면|_#18_|`도우미`|토큰 부여 기준 확인|

## 화면 설계

|_#01_ 조직 전체 랭킹|_#02_ 조직 타입별 랭킹|_#03_ 조직 내부의 개인 랭킹|
|:----:|:----:|:----:|
|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/09b3ff58-4b9b-41aa-ac69-9e1f3c5cb7c0" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/243c7058-1198-4e68-9ca7-ff840114a7c6" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/d3ffda72-602c-4a67-afbe-e39f1cdb5971" width=250 height=550/>|
|_#04_ 전체 사용자 랭킹|_#05_ _#06_ Repository 비교|_#07_ Repository 필터링별 검색|
|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/af000586-0e46-46a2-a3c2-c5676a080036" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/82162bef-23fb-404f-a228-5675e8a1c2a9" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/b033bc11-0fb2-4439-8da6-a982ce8ade0d" width=250 height=550/>|
|_#07_ Repository 검색 결과|_#08_ 사용자 검색|_#09_ 메인 화면|
|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/abe96042-e864-4d05-ae39-e3d91ee2bb2a" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/9682e6b8-fd50-44bd-88f8-abda60158fa5" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/5bc636b1-1313-42fe-8738-120d5f3dd59b" width=250 height=550/>|
|_#10_ Repository 내부 기여도 조회|_#11_ 사용자 상세 조회|_#12_ 블록체인 토큰 부여 내역 조회|
|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/86011169-8904-4561-904c-61ab6a07d498" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/e8602bfb-0020-488c-a69f-3246b7373be1" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/6f5b0574-c023-4794-8ca3-6ca24bb21de6" width=250 height=550/>|
|_#12_ 블록체인 토큰 부여 내역 조회|_#13_ _#14_ 로그인|_#13_ Github 소셜 로그인|
|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/d4a709dc-50b9-47c6-9ccf-72e50431a86c" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/36f83630-d841-46be-a299-2f39b9dc2c48" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/a82229de-6aad-455c-8af3-fd7ae8e08210" width=250 height=550/>|
|_#14_ KLIP 인증|_#15_ 조직 인증|_#15_ 조직 등록|_#15_ 조직 인증|
|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/16cd8f73-6ef6-4c27-b582-d4f568e31f79" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/ed00f79a-da08-45c7-8da9-0b599f58799f" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/9f34b4cd-eafb-4bd2-8f31-59128e86cc03" width=250 height=550/>|
|_#15_ 인증 번호 이메일|_#15_ 인증 번호 확인|_#16_ 관리자 조직 승인/반려|_#16_ 관리자 조직 등록 요청 관리|
|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/3629d6cd-dca3-4d98-93a4-911bb0a6d34e" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/b18314ae-aa09-4e5b-b55d-1d28fd6d100f" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/a0cd62e5-e361-43f9-84b9-c4dd62a9a9f8" width=250 height=550/>|
|_#16_ 관리자 조직 상태별 목록 조회|_#17_ FAQ|_#18_ 토큰 부여 기준 및 티어 목록 확인|
|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/e8166d81-d1f5-46ec-b9aa-b82acd10e241" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/195818ae-371b-4a1e-91b1-178c73cd6205" width=250 height=550/>|<img src="https://github.com/tukcom2023CD/DragonGuard-JinJin/assets/89020004/4a712023-d00c-47ae-90e8-4d97024328b2" width=250 height=550/>|

## 협업 툴
<a href="https://seoullian.atlassian.net/jira/software/projects/DJ/boards/2/backlog" target="_blank"> Jira URL</a>

## Docs
<a href="https://github.com/tukcom2023CD/DragonGuard-JinJin/wiki">GitRank Wiki</a><br>
<a href="https://ohksj77.github.io/DragonGuard-JinJin-API-Docs/">Backend API Docs</a><br>
<a href="https://www.youtube.com/watch?v=aBx-MzU7E7c">Trailer (소개 영상)</a>

## 멤버 소개

|김관용|심영수|정호진|김승진|
|:----:|:----:|:----:|:----:|
|iOS, Blockchain|Android|iOS, Blockchain|Backend, DevOps, Scraping|
|<a href="https://github.com/Sammuelwoojae">@Sammuelwoojae</a>|<a href="https://github.com/posite">@posite</a>|<a href="https://github.com/HJ39">@HJ39</a>|<a href="https://github.com/ohksj77">@ohksj77</a>|
