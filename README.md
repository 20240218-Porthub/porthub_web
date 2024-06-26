
## 다이어그램 및 문서

## 프로젝트 기술 스택
<img width="665" alt="스크린샷 2024-06-27 오전 1 41 35" src="https://github.com/20240218-Porthub/porthub_web/assets/99077276/29e083dc-51ae-46ce-9fa6-6305069c6e7b">

## 프로젝트 구조 (dto, controller, service, repository)

## domain구조, layer 구조
<img width="610" alt="스크린샷 2024-06-27 오전 1 43 18" src="https://github.com/20240218-Porthub/porthub_web/assets/99077276/cd62ff34-2a92-49c3-b191-97f148f1244b">

- 계층형 구조를 사용: 패키지 구조를 이해하기 수월함, 기업과 같이 상대적으로 큰 규모가 아니기 때문에 프로젝트에 적합한 layer 구조를 선택

## spring security 설정
- 스프링 시큐리티
- 
## 클래스 다이어그램, uml 구조

## 무중단 배포 셋팅

## github관리 (main → push할 때 배포 자동화)

## 레지스터(로그인&회원가입)

## 포트폴리오 등록(마크다운, 오디오 및 이미지 태그)

## 세션, 쿠키 등

## Import 결제 서비스, 멘토링

## 채팅 서비스

## 로그인 세션 사용

## 시연 방법
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
   <summary>application.yml 파일을 src/main/resources에 생성</summary>
   <div markdown="2">
   
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
   
