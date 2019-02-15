# Platform for Standalone Application Development
<hr/>

## Concept
This is platform for Java-based standalone application development such as daemon proces kind of RESTful API Server, Scheduling Server, TCP/IP Server, APM Monitoring Server and so on.

![PlantUML model](http://www.plantuml.com/plantuml/png/XLAnZXGn3Etz5QwHHlGfLYT1GmKI9Q884-yHQP8CjdC88VwTTIQUPMckSlBUi_qSvqaqilQo0MZMZN0dlZOkkJxZXxSV0Rwy-OhBSi5tMwQgS8I0dxYj98ATC-RgxAJnflzC_4ACmHHBYXf1D2ev_XiGH-aBbbQpDWRqKt3Hy2OEtomzQBJjWb6HQi9szPmjNmd-_ElaVTJuBGe5Tta7qZnXsucZvbO5fa_kTMsbn9g6Ly-nWXCF9mZecD7NRDoFJlpB2GF4VBfC6yhm2_UHB5TeerN398zt0nmbSJbvG1VEYFjSXxP3kxDDv3rSZelRcw_rK7iu3cUEtwckBP4SLpJpGZx1epBHeMIIlgdZzwPEs7eCGFjMdSMskLgS0picsfW4Krlbn9YKCxsGOAMV1pKvj3Qaw_TWS2-swN4V1fv9IsE26LyVovN_Z-KuBcS_ix9uJm7mjXVxuV14DVMo_GK0)



### Java-based Main Application 
JAVA Main Application

### Including Embedded WebServer
Embedded Tocmat을 내장하고 있음으로 웹서버가 필요없는 독립서버이다.

### Supporting Enterprise Level Web Application via spring-framework
웹채널의 경우 이미 기업환경에서 검증이 된 spring-framework 기반으로 구성한다.

### Authentication and Authorization with spring-security
비지니스 환경에서의 복잡한 인증,권한 구조를 수요하기 위하여 spring-security 라이브러리를 데이터베이스와 연동하여 구현된 User-Group-Role-Authority 으로 구현된 기본 모듈을 제공한다.

### Integration with JPA (spring-data and Hibernate) and  Mybatis together
Multiple Database 환경을 제공하기 위하여 JPA 와 복잡한 업무 비지니스를 구현하기 위한 Mybatis를 모두 사용하여 개발하는 방법을 제공한다.

### Administrator Web Console
Application을 관리하기 위한 관리 Web Console을 제공한다.

### Include RESTful API Structure
JWT(JSON Web Token) 인증 기반의 RESTful API 기능을 기본적으로 포함하고 있으며 해당 기본구조를 기초로 비지니스에 맞는 API를 확장 구현할수 있도록 구성한다.
Swagger 문서화 도구를 이용한 API 문서화 기능을 포함한다.

### CMS(Contents Management System) with Theme and Skin Functionality
웹채널을 구성할 경우 해당 웹사이트의 디자인 테마 및 게시판,블로그 등의 스킨을 적용할수 있는 다중 컨텐츠 관리 기능을 제공한다. 

### Built in Common Application Component (based on web-fragment technology)
대부분의 시스템에서 사용되는 공통 모듈이 구현된 형태로 제공되며 해당 구현된 모듈은 web-framgment 표준으로 해당 어플리케이션 구동 시 자동으로 로드되도록 한다.


## Including Open Sources
| Open Sources     						| Description      							|
|-------------------------------------- |------------------------------------------ |
| **Daemon Instance**					| Pure Java      							|
| **Embedded Tomcat**					| Embedded Web Server      					|
| **Srping Framework**				| IOC Container and Core Framework    	 	|
| **Spring Security**      			| Web Channel Security Library           	|
| **JPA(spring-data + Hibernate)**	| Data Access Object(ORM)           		|
| **Mybatis**   						| Data Access Obect(MAPPER)           		|
| **JWT(JSON Web Token)**				| Web Channel Security Token           		|


## Download and Build Binary
First, download the source code from the git hub repository.
And then, executes build shell script. **__(build.sh)__**
```bash
// clones source from github
user@host> git clone https://github.com/oopscraft/application.git

// maven build
user@host> build.sh
```


## Configuration
Open the **${APP_HOME}/conf/application.properties__** file below.
This file contains application configuration information that can be modified.
```bash
user@host> vim conf/application.properties
...
################################################################################
# Application Configuration
################################################################################
# application theme configuration
application.configuration.theme=__application

# webServer
application.webServer.port=10001
application.webSerer.ssl=false
application.webSerer.keyStorePath=conf/ssl/keystore
application.webSerer.keyStoreType=pkcs12
application.webSerer.keyStorePass=abcd1234

###########################################
# MYSQL
########################################### 
# dataSource default connection pool
application.dataSource.driver=org.mariadb.jdbc.Driver
application.dataSource.url= jdbc:mariadb://oopscraft.iptime.org:3306/app
application.dataSource.username=app
application.dataSource.password=djvmfflzpdltus!
application.dataSource.initialSize=5
application.dataSource.maxActive=10
application.dataSource.validationQuery=select 1

# entityManagerFactory properties
application.entityManagerFactory.databasePlatform=org.hibernate.dialect.MySQLDialect
application.entityManagerFactory.packagesToScan=

# sqlSessionFactory properties
application.sqlSessionFactory.databaseId=MYSQL
application.sqlSessionFactory.mapperLocations=
```


## Controls Application
```bash
// starts application
user@host> application.sh start

// tails log 
user@host> application.sh log

// show status application
user@host> application.sh status

// shutdown application
user@host> application.sh stop
```


## Application Development from Platform
```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>${org.springframework-version}</version>
    <scope>provided</scope>
</dependency>
```


## References
http://batman.oopscraft.net
http://soma.oopscraft.net


## More Information
### Official Website
http://duice.oopscraft.net

### Source Repository
https://github.com/oopscraft/duice

### Distribution Download
http://duice.oopscraft.net/dist/duice.zip

### Licence
Anyone can use it freely. Modify the source or allow re-creation. However, you must state that you have the original creator. However, we can not grant patents or licenses for reproductives. (Modifications or reproductions must be shared with the public.)

Licence: LGPL(GNU Lesser General Public License version 3) Copyright (C) 2017 duice.oopscraft.net Contact chomookun@gmail.com
