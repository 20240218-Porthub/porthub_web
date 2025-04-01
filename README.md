


# 🚀 PortfolioHub

> 통합 포트폴리오 웹 서비스


---


## 📜 프로젝트 개요
> 이 프로젝트는 각 분야 별로 흩어진 포트폴리오 및 작업물을 하나의 플랫폼으로
관리하고, 협업을 촉진하는 웹 서비스입니다.

- `주요 기능`: 포트폴리오, 채팅, 멘토링
- `인원` : 개발 3인
- `기간`

  - 설계 기간: 2024.01.02 ~ 2024.03.01
  - 개발 기간: 2024.03.02 ~ 2024.06.19

## 🛠️ 프로젝트 아키텍처

![아키텍처](https://github.com/user-attachments/assets/f7f468a9-0fce-467e-b295-4be758d3bfd2)

---

## 🛠 기술 스택

### 📌 Backend && Frontend
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white) ![Spring Security](https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white) 

![Thymeleaf](https://img.shields.io/badge/Thymeleaf-005F0F?style=for-the-badge&logo=thymeleaf&logoColor=white)

### 📌 Database
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white) ![MyBatis](https://img.shields.io/badge/MyBatis-DC382D?style=for-the-badge&logo=&logoColor=white) ![AWS RDS](https://img.shields.io/badge/AWS%20RDS-527FFF?style=for-the-badge&logo=amazon-aws&logoColor=white)

### 📌 CI/CD & DevOps
![AWS EC2](https://img.shields.io/badge/AWS%20EC2-FF9900?style=for-the-badge&logo=amazon-aws&logoColor=white) ![AWS S3](https://img.shields.io/badge/AWS%20S3-569A31?style=for-the-badge&logo=amazon-aws&logoColor=white) ![AWS CodeDeploy](https://img.shields.io/badge/AWS%20CodeDeploy-232F3E?style=for-the-badge&logo=amazon-aws&logoColor=white)

![Travis CI](https://img.shields.io/badge/TravisCI-3EAAAF?style=for-the-badge&logo=travisci&logoColor=white)  ![Nginx](https://img.shields.io/badge/Nginx-269539?style=for-the-badge&logo=nginx&logoColor=white)

### 📌 협업 도구
![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white) ![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)  ![WBS](https://img.shields.io/badge/WBS-007ACC?style=for-the-badge&logo=&logoColor=white)

---


## 🎥 미리 보기



| ![메인홈](https://github.com/user-attachments/assets/b6b5db71-f636-4b59-a2a9-f32f04603ecc) | ![검색](https://github.com/user-attachments/assets/93210e8d-2ae8-46c4-9329-c9d4698f31c7) |
|:----------:|:----------:|
| **메인 홈** | **검색 기능** |
| ![결제](https://github.com/user-attachments/assets/628bc065-e48b-4cfc-a278-90877c90c6fa) | ![멘토 인증](https://github.com/user-attachments/assets/8be2310f-2b1d-4670-9599-6901142aa910) |
| **결제 시스템** | **멘토 인증** |
| ![멘토 등록](https://github.com/user-attachments/assets/834d71a0-a3dd-412e-bc4d-9a94ae1a2ab2) | ![무중단 배포](https://github.com/user-attachments/assets/9b1db388-87b6-4cb5-8c51-7ee7c742d321) |
| **멘토 등록** | **무중단 배포** |
| ![사용자 제재](https://github.com/user-attachments/assets/977801a0-5982-436c-8717-4ed18701b922) | ![어드민](https://github.com/user-attachments/assets/9fe5d1d3-5ad8-49e0-b64a-ca7d79ea68e8) |
| **사용자 제재** | **어드민 페이지** |
| ![채팅](https://github.com/user-attachments/assets/ce4ce4fc-8c3e-41bb-980e-4d3ac082282e) | ![챗봇](https://github.com/user-attachments/assets/2ca102ce-7d34-4eb5-931a-71d21d52dfa0) |
| **채팅 기능** | **챗봇 시스템** |
| ![포트폴리오 등록](https://github.com/user-attachments/assets/9cb2f41d-9baf-4e1a-86a1-27980e6a0c27) | ![포트폴리오 기능](https://github.com/user-attachments/assets/32879e86-7b29-43aa-9017-d691ece1459c) |
| **포트폴리오 관리** | **포트폴리오 기능** |
| ![프로필 수정](https://github.com/user-attachments/assets/27532d7c-583d-4e13-8a29-b7bfbf52ef0e) | ![회원가입](https://github.com/user-attachments/assets/982e14ea-beba-44c1-ba0d-fbb80e019ceb) |
| **프로필 수정** | **회원가입** |

---

## YML && DataBase
 - <details>
   <summary>application.yml 파일을 src/main/resources에 생성</summary>
   <div markdown="1">
   
   ```
     server:
      port: 8080

    spring:
      datasource:
        driver-class-name: org.mariadb.jdbc.Driver
        url: /* */
        username: /* */
        password: /* */
      servlet:
        multipart:
          #file upload size
          max-file-size: 20MB
          max-request-size: 20MB
      mail:
        host: /* */
        port: /* */
        username: /* */
        password: /* */
        properties:
          mail:
            smtp:
              auth: true
              starttls:
                enable: true
    #            required: true
    #          connectiontimeout: 5000
    #          timeout: 5000
    #          writetimeout: 5000
    #    auth-code-expiration-millis: 300000
    
    #Colud S3
    cloud:
      aws:
        s3:
          bucket: /* */
        stack.auto: false
        region.static: ap-northeast-2
        credentials:
          accessKey: /* */
          secretKey: /* */
    
    
    # mybatis
    mybatis:
      mapper-locations: classpath:mapper/*.xml
      config-location: classpath:mybatis-config.xml
    
    #iamport
    imp:
      code: /* */
      api:
        key: /* */
        secretkey: /* */
    
    chatGpt:
      key: /* */
    
   ```
   </div>
   </details>
   
 - <details>
   <summary>Database</summary>
   <div markdown="1">
   
   ```
  
    CREATE TABLE `Categories` (
      `CategoryID` int(11) NOT NULL AUTO_INCREMENT,
      `CategoryName` varchar(255) NOT NULL,
      PRIMARY KEY (`CategoryID`)
    )
  
    CREATE TABLE `CopyrightReport` (
      `ReportID` int(11) NOT NULL AUTO_INCREMENT,
      `PortfolioID` int(11) NOT NULL,
      `Contents` text NOT NULL,
      `ReporterEmail` varchar(255) NOT NULL,
      `ReportedID` int(11) NOT NULL,
      `ReportState` tinyint(1) NOT NULL DEFAULT 0,
      PRIMARY KEY (`ReportID`)
    ) 
    
    CREATE TABLE `Follows` (
      `FollowerID` int(11) NOT NULL,
      `FollowingID` int(11) NOT NULL,
      PRIMARY KEY (`FollowerID`,`FollowingID`)
    ) 
    
    CREATE TABLE `Mento` (
      `MentoID` int(11) NOT NULL AUTO_INCREMENT,
      `UserID` int(11) NOT NULL,
      `Introduction` text DEFAULT NULL,
      `Ability` varchar(255) DEFAULT NULL,
      `CompanyName` varchar(255) DEFAULT NULL,
      `CareerCertification` longtext DEFAULT NULL,
      `UnivName` varchar(255) DEFAULT NULL,
      `UnivCertification` longtext DEFAULT NULL,
      `CertificationName` varchar(255) DEFAULT NULL,
      `IssueCertification` longtext DEFAULT NULL,
      `credit` int(11) DEFAULT 0,
      PRIMARY KEY (`MentoID`)
    ) 
    
    CREATE TABLE `MentoFile` (
      `MentoFileID` int(11) NOT NULL AUTO_INCREMENT,
      `ActivityID` int(11) NOT NULL,
      `File_url` varchar(255) DEFAULT NULL,
      `contents` text DEFAULT NULL,
      PRIMARY KEY (`MentoFileID`)
    ) 
    
    CREATE TABLE `MentoProcess` (
      `ProcessID` int(11) NOT NULL AUTO_INCREMENT,
      `MentoID` int(11) NOT NULL,
      `Process` varchar(1) NOT NULL,
      PRIMARY KEY (`ProcessID`)
    ) 
    
    CREATE TABLE `Mentoring` (
      `MentoringID` int(11) NOT NULL AUTO_INCREMENT,
      `MentoID` int(11) NOT NULL,
      `CategoryID` int(11) DEFAULT NULL,
      `Title` varchar(255) DEFAULT NULL,
      `Content` text DEFAULT NULL,
      `Price` decimal(10,2) DEFAULT NULL,
      `Thumbnail` varchar(255) DEFAULT NULL,
      `file_urls` longtext DEFAULT NULL,
      `mentoring_delete` int(11) DEFAULT 0,
      PRIMARY KEY (`MentoringID`)
    ) 
    
    CREATE TABLE `MentoringOrder` (
      `OrderID` int(11) NOT NULL AUTO_INCREMENT,
      `pg` varchar(50) NOT NULL,
      `pay_method` varchar(50) NOT NULL,
      `merchant_uid` text DEFAULT NULL,
      `goods_id` int(11) NOT NULL,
      `pay_name` longtext NOT NULL,
      `amount` int(11) NOT NULL,
      `buyer_email` varchar(50) NOT NULL,
      `buyer_name` varchar(50) NOT NULL,
      PRIMARY KEY (`OrderID`)
    ) 
    
    CREATE TABLE `PopularUser` (
      `PopularID` int(11) NOT NULL AUTO_INCREMENT,
      `UserID` int(11) NOT NULL,
      `UserName` varchar(255) NOT NULL,
      `Email` varchar(255) NOT NULL,
      `ProfileImage` longtext DEFAULT NULL,
      `aff` longtext DEFAULT NULL,
      PRIMARY KEY (`PopularID`)
    ) 
    
    CREATE TABLE `PortfolioLikes` (
      `PortfolioLikesID` int(11) NOT NULL AUTO_INCREMENT,
      `PortfolioID` int(11) NOT NULL,
      `Email` varchar(255) NOT NULL,
      `Heart_Check` tinyint(1) NOT NULL DEFAULT 0,
      PRIMARY KEY (`PortfolioLikesID`)
    ) 
    
    CREATE TABLE `Usermeta` (
      `MetaID` int(11) NOT NULL AUTO_INCREMENT,
      `UserID` int(11) NOT NULL,
      `intro` longtext DEFAULT NULL,
      `aff` longtext DEFAULT NULL,
      `link` longtext DEFAULT NULL,
      `career` longtext DEFAULT NULL,
      PRIMARY KEY (`MetaID`)
    ) 
    
    CREATE TABLE `Users` (
      `UserID` int(11) NOT NULL AUTO_INCREMENT,
      `UserName` varchar(255) NOT NULL,
      `Email` varchar(255) NOT NULL,
      `PasswordHash` varbinary(255) NOT NULL,
      `ProfileImage` longtext DEFAULT NULL,
      `RegistrationDate` datetime NOT NULL,
      `AdditionalInfo` text DEFAULT NULL,
      `Role` text DEFAULT 0,
      `backImage` longtext DEFAULT NULL,
      `PaidProduct` longtext DEFAULT NULL,
      PRIMARY KEY (`UserID`),
      UNIQUE KEY `Email` (`Email`)
    ) 
    
    CREATE TABLE `Chats` (
      `ChatID` int(11) NOT NULL AUTO_INCREMENT,
      `SenderID` int(11) NOT NULL,
      `ReceiverID` int(11) NOT NULL,
      `Content` text NOT NULL,
      `DateTime` datetime NOT NULL,
      `SessionID` varchar(64) DEFAULT NULL,
      PRIMARY KEY (`ChatID`),
      KEY `SenderID` (`SenderID`),
      KEY `ReceiverID` (`ReceiverID`),
      KEY `fk_ChatSessions` (`SessionID`),
      CONSTRAINT `Chats_ibfk_1` FOREIGN KEY (`SenderID`) REFERENCES `Users` (`UserID`),
      CONSTRAINT `Chats_ibfk_2` FOREIGN KEY (`ReceiverID`) REFERENCES `Users` (`UserID`)
    ) 
    
    CREATE TABLE `Payments` (
      `PaymentID` int(11) NOT NULL AUTO_INCREMENT,
      `PayerID` int(11) NOT NULL,
      `PayeeID` int(11) NOT NULL,
      `PaymentDate` datetime NOT NULL,
      `Amount` decimal(10,2) NOT NULL,
      `PaymentStatus` varchar(255) NOT NULL,
      PRIMARY KEY (`PaymentID`),
      KEY `PayerID` (`PayerID`),
      KEY `PayeeID` (`PayeeID`),
      CONSTRAINT `Payments_ibfk_1` FOREIGN KEY (`PayerID`) REFERENCES `Users` (`UserID`),
      CONSTRAINT `Payments_ibfk_2` FOREIGN KEY (`PayeeID`) REFERENCES `Users` (`UserID`)
    ) 
    
    CREATE TABLE `Portfolios` (
      `PortfolioID` int(11) NOT NULL AUTO_INCREMENT,
      `AuthorID` int(11) NOT NULL,
      `Thumbnail_url` varchar(255) DEFAULT NULL,
      `Title` varchar(255) NOT NULL,
      `Hearts_count` int(11) DEFAULT NULL,
      `Views_count` int(11) DEFAULT NULL,
      `CreationDate` datetime NOT NULL,
      `CategoryID` int(11) DEFAULT NULL,
      `AttachmentsOrLinks` text DEFAULT NULL,
      PRIMARY KEY (`PortfolioID`),
      KEY `AuthorID` (`AuthorID`),
      KEY `CategoryID` (`CategoryID`),
      CONSTRAINT `Portfolios_ibfk_1` FOREIGN KEY (`AuthorID`) REFERENCES `Users` (`UserID`),
      CONSTRAINT `Portfolios_ibfk_2` FOREIGN KEY (`CategoryID`) REFERENCES `Categories` (`CategoryID`)
    ) 
    
    CREATE TABLE `SessionParticipants` (
      `SessionKey` varchar(64) NOT NULL,
      `UserID` int(11) NOT NULL,
      PRIMARY KEY (`SessionKey`,`UserID`),
      KEY `UserID` (`UserID`),
      KEY `idx_SessionParticipants_SessionKey` (`SessionKey`),
      CONSTRAINT `SessionParticipants_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `Users` (`UserID`)
    ) 
    
    CREATE TABLE `Images` (
      `ImageID` int(11) NOT NULL AUTO_INCREMENT,
      `PortfolioID` int(11) DEFAULT NULL,
      `Image_url` varchar(255) DEFAULT NULL,
      `contents` mediumtext DEFAULT NULL,
      PRIMARY KEY (`ImageID`),
      KEY `PortfolioID` (`PortfolioID`),
      CONSTRAINT `Images_ibfk_1` FOREIGN KEY (`PortfolioID`) REFERENCES `Portfolios` (`PortfolioID`)
    ) 
    
    CREATE TABLE `PortfolioTags` (
      `TagID` int(11) NOT NULL AUTO_INCREMENT,
      `PortfolioID` int(11) NOT NULL,
      `TagName` varchar(255) NOT NULL,
      PRIMARY KEY (`TagID`),
      KEY `PortfolioID` (`PortfolioID`),
      CONSTRAINT `PortfolioTags_ibfk_1` FOREIGN KEY (`PortfolioID`) REFERENCES `Portfolios` (`PortfolioID`)
    ) 
   ```
   </div>
   </details>
   
