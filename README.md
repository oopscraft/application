# Oopscraft Application Platform

웹어플리케션의 화면 위주의 서비스가 메인 요소가 아닌 백엔드 Standalne 어플리케이션 개발 시 사용하기 위한 반가공 형태의 개발플랫폼이다.
(예를 들면 배치스케줄 시스템, 모니터링 시스템, 백엔드 API 어플리케이션 등..)

비슷한 아마도 현재 가장 많이 사용되는 프레임워크로 spring-boot를 들수 잇다.

spring-boot와의 가장 큰 차이점이라면

1. spring-boot의 경우 모든 부분이 spring-framework 기반으로 돌아감으로 디테일한 설정이 필요한 백엔드 어플리케이션에서는 핸들링에 제약이 있으나 해당 플랫폼은 POJO 기반에 웹채널등 spring-framework가 담당하는 부분만 위하는 형태로 구성된다.
2. spring-boot가 자동자의 단위부품을 제공해주는 것이라면 해당 플랫폼에서는 항상 비슷한 형태로 개발되는 모듈은 이미 설정, 개발된 상태로 제공되어진다.

![PlantUML model](http://www.plantuml.com/plantuml/png/SoWkIImgAStDuNBAJrBGjLDmpCbCJbMmKiX8pSd9JqzBAGPXBeVKl1IW8W00)

     ┌───┐          ┌─────┐
     │Bob│          │Alice│
     └─┬─┘          └──┬──┘
       │hellogfdsgfds  │   
       │──────────────>│   
     ┌─┴─┐          ┌──┴──┐
     │Bob│          │Alice│
     └───┘          └─────┘

주요 특징은 아래와 같다.

| Library        			| NAME          | TITLE |
|-------------------------------------- |----------------|--------|
| **Daemon Instance**					| Pure Java      | Cool1  |
| **Embedded Tomcat**					| Embedded Web Server      | Cool1  |
| **spring-framework**				| IOC Container and Core Framework      			 | Are2           |
| **spring-security**      			| Security Library           | Cool1  |
| **JPA(spring-data + Hibernate)**	| Data Access Object(ORM)           | Cool1  |
| **Mybatis**   						| Data Access Obect(MAPPER)           | Cool1  |
| **JWT(JSON Web Token)**				| Security Token           | Cool1  |

## Download and Build Application
```bash
// clones source from github
user@host> git clone https://github.com/oopscraft/application.git

// maven build
user@host> ./build.sh
```

## Configuration
```
user@host> vim conf/application.properties
```

## Start and Stop Application
```
// starts application
user@host> application.sh start

// tails log 
user@host> application.sh log

// show status application
user@host> application.sh status

// shutdown application
user@host> application.sh stop
```

## Platform for standalone application development.
Standalone 백엔드 어플리케이션 개발을 위한 플랫폼이다.
<code>
user@host> build.sh 
</code>

## No need web server (including embedded tomcat webserer)
Embedded Tocmat을 내장하고 있음으로 웹서버가 필요없는 독립서버이다.

## supporting Enterprise Level Web Apllication via spring-framework
웹채널의 경우 이미 기업환경에서 검증이 된 spring-framework 기반으로 구성한다.

## Athentication and Authorization with spring-security
비지니스 환경에서의 복잡한 인증,권한 구조를 수요하기 위하여 spring-security 라이브러리를 데이터베이스와 연동하여 구현된 User-Group-Role-Authority 으로 구현된 기본 모듈을 제공한다.

## Integration with JPA (spring-data-jpa and Hibernate) and  Mybatis together
Multiple Database 환경을 제공하기 위하여 JPA 와 복잡한 업무 비지니스를 구현하기 위한 Mybatis를 모두 사용하여 개발하는 방법을 제공한다.

## Built in Common application component (based on web-fragment)
대부분의 시스템에서 사용되는 공통 모듈이 구현된 형태로 제공되며 해당 구현된 모듈은 web-framgment 표준으로 해당 어플리케이션 구동 시 자동으로 로드되도록 한다.



