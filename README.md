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
|01|`랭킹`|조직 전체 랭킹|09|`상세 조회`|Repository 내부 기여도 조회|
|02|`랭킹`|조직 타입별 랭킹|10|`상세 조회`|사용자 상세 조회|
|03|`랭킹`|조직 내부의 개인 랭킹|11|`상세 조회`|블록체인 토큰 부여 내역 조회|
|04|`랭킹`|전체 사용자 랭킹|12|`인증`|Github 소셜 로그인 (OAuth2, Jwt)|
|05|`비교`|Repository 정보 비교|13|`인증`|KLIP 인증|
|06|`비교`|Repository 내부 기여자 비교|14|`인증`|조직 인증 (이메일)|
|07|`검색`|Repository 필터링별 검색|15|`관리자`|조직 등록 요청 관리|
|08|`검색`|사용자 검색|16|`도우미`|FAQ & 토큰 부여 기준 확인|

## 협업 툴
<a href="https://seoullian.atlassian.net/jira/software/projects/DJ/boards/2/backlog" target="_blank"> Jira URL</a>

## Docs
<a href="https://github.com/tukcom2023CD/DragonGuard-JinJin/wiki">GitRank Wiki</a><br>
<a href="https://ohksj77.github.io/DragonGuard-JinJin-API-Docs/">Backend API Docs</a>

## 멤버 소개

|김관용|심영수|정호진|김승진|
|:----:|:----:|:----:|:----:|
|iOS, Blockchain|Android, Blockchain|iOS, Blockchain|Backend, DevOps, Scraping|
|<a href="https://github.com/Sammuelwoojae">@Sammuelwoojae</a>|<a href="https://github.com/posite">@posite</a>|<a href="https://github.com/HJ39">@HJ39</a>|<a href="https://github.com/ohksj77">@ohksj77</a>|
