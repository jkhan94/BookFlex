<br><br>
<center><img src="https://github.com/user-attachments/assets/794785e1-70dc-45ef-a9d6-2327ab561178" width=50% height=50%/></center>
<br><br>

___

# 📖️ 목차
* [✒️ 프로젝트 소개](#a)
* [📆 개발 기간](#b)
* [️👨‍👨‍👧‍👦 팀원 구성](#c)
* [⚙️ 개발 환경](#d)
* [🗃️ Project Architecture](#e)
* [🎨 Wireframe](#f)
* [📊 ERD DIAGRAM](#g)
* [📜 API 명세서](#h)
* [📮 Git Convention](#i)
* [📮 Code Convention](#j)
<br>

<div id="a">

## ✒️ 프로젝트 소개
1. **서비스 개요** 
   * 서비스명: Bookflex <br>
   * 서비스 개요: 다양한 책을 손쉽게 검색하고 구매할 수 있는 온라인 서점 플랫폼 <br>
2. **서비스 개발 배경** 
   * 문제 인식
     * 현대인들은 바쁜 일상 속에서 오프라인 서점을 방문할 시간이 부족하여 온라인으로 책을 구매하는 빈도가 증가하고 있습니다. 
     * 하지만 기존 온라인 서점은 사용자 경험이 불편하거나, 관리 기능이 제한적인 경우가 많습니다. <br>
   * 필요성 
     * 보다 편리하고 효율적인 온라인 서점 운영 및 관리를 통해 사용자에게 최적의 독서 경험을 제공하고자 합니다.
3. **서비스 목표** 
   * 사용자가 원하는 책을 빠르고 쉽게 찾고 구매 가능
   * 효율적인 서점 운영 및 관리를 지원하는 시스템을 제공
4. **타겟 사용자** 
   * 책을 사랑하는 모든 독자, 특히 바쁜 직장인, 학생, 온라인 쇼핑을 선호하는 사람들
5. **서비스 주요 기능** 
   * 사용자 기능:
      * 상품 검색
      * 장바구니
      * 주문
      * 쿠폰함
      * 위시리스트
      * 프로필 관리
      * 고객 문의 남기기
   * 관리자 기능:
      * 상품 관리
      * 쿠폰 관리
      * 매출 내역
      * 리뷰 관리
      * 주문내역 조회
      * 주문상품 배송 정보 관리
      * 회원 관리
      * 고객문의 관리
6. **기대 효과** <br>
   * 사용자 측면: 간편하고 빠른 책 구매, 다양한 할인 혜택, 편리한 고객 지원
   * 비즈니스 측면: 고객 만족도 향상, 운영 효율성 증대
   
<br>   
Bookflex는 고객과 관리자의 요구를 모두 만족시키는 온라인 서점으로서, 보다 풍부한 독서 경험을 제공하고, 효율적인 서점 운영을 가능하게 합니다.

[프로젝트 노션](https://teamsparta.notion.site/13-647e5be3039d4e29b69be9b9efa70cf2)
</div>

<div id="b">

<br>

## 📆 개발 기간
* 2024.07.17 ~ 2024.08.21 <br>
* 2024.08.05: 중간 발표회 <br>
* 2024.08.21: 최종 발표회 <br>

</div>

<br>

<div id="c">

## ️👨‍👨‍👧‍👦 팀원 구성
| 홍용근(팀장)                                                                        | 이인호(부팀장)                                                               | 이종원                                                                                | 한진경                                                                  |
|--------------------------------------------------------------------------------|------------------------------------------------------------------------|------------------------------------------------------------------------------------|----------------------------------------------------------------------|
| [![홍용근](https://github.com/HongYongGuen.png)](https://github.com/HongYongGuen) | [![이인호](https://github.com/inho9979.png)](https://github.com/inho9979) | [![이종원](https://github.com/dlwhddnjs11223.png)](https://github.com/dlwhddnjs11223) | [![한진경](https://github.com/jkhan94.png)](https://github.com/jkhan94) |
| - 장바구니 <br> - 주문, 결제  <br> - 재고관리                                 | - 인증, 인가 <br> - 배송 <br> - 시스템 로그                                   | - 상품 관리 <br> - 리뷰 <br> - 매출, 주문 내역 조회                                              | - 카테고리 <br> - 쿠폰 <br> - 고객문의                                                   |

</div>

<br>

<div id="d">

## ⚙️ 기술 스택
### Front-end
![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB) 
![NodeJS](https://img.shields.io/badge/node.js-6DA55F?style=for-the-badge&logo=node.js&logoColor=white) <br>

### Back-end
![Java](https://img.shields.io/badge/java_(JDK_17)-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) 
![IntelliJ IDEA](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white) <br>
![SpringBoot](https://img.shields.io/badge/SpringBoot-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) 
![SpringSecurity](https://img.shields.io/badge/SpringSecurity-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) <br>
![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white)
![AWS](https://img.shields.io/badge/AWS_(EC2,_S3)-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white) <br>
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white) <br>

### Tools
![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white) <br>
![GitHub](https://img.shields.io/badge/github-%23121011.svg?style=for-the-badge&logo=github&logoColor=white) 
![Git](https://img.shields.io/badge/git-%23F05033.svg?style=for-the-badge&logo=git&logoColor=white) <br>
![Slack](https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white) 
![Notion](https://img.shields.io/badge/Notion-%23000000.svg?style=for-the-badge&logo=notion&logoColor=white) <br>

</div>

<br>

<div id="e">

## 🗃️ Project Architecture
![서비스 아키택처](https://github.com/user-attachments/assets/81f32f47-83da-48fe-98e9-d7ab3bb31e13)

</div>

<br>

<div id="f">

## 🎨 Wireframe
![와이어 프레임 x0 5](https://github.com/user-attachments/assets/3a1a5d05-12a1-4679-b2bc-b28172e8491f)
[Wireframe](https://www.figma.com/design/r3mcibIDLeKH9LYyHuerZx/%EC%99%80%EC%9D%B4%EC%96%B4-%ED%94%84%EB%A0%88%EC%9E%84?node-id=0-1&t=Gpmlpsj4eMticAry-1)
</div>

<br>

<div id="g">

## 📊 ERD DIAGRAM
![ERD](https://github.com/user-attachments/assets/60074327-dca3-4567-b4a6-0c1e2e9d28e1)

</div>

<br>

<div id="h">

## 📜 API 명세서
![API명세서](https://github.com/user-attachments/assets/184bb42c-8a01-4bb6-9d5a-16022f069570)
[API 명세서](https://www.notion.so/teamsparta/13-647e5be3039d4e29b69be9b9efa70cf2)
</div>

<br>

<div id="i">

## 📮 Git Convention
![깃 컨벤션](https://github.com/user-attachments/assets/7a5c1350-7082-45f9-9e95-33fa04ae426e)
[Git Convention](https://www.notion.so/teamsparta/Github-Rules-44d13abaf1bd46099977d2dab7abfac2)
</div>

<br>

<div id="j">

## 📮 Code Convention
![코드 컨벤션](https://github.com/user-attachments/assets/d8901aeb-cb40-47f0-b909-63102618d3f3)
[Code Convention](https://www.notion.so/teamsparta/Code-Convention-2a7b28f0a4f449d8976dd71fedf957ca)
</div>


 
