# oopscraft-application platform
웹어플리케션이 메인 요소가 아닌 Standalne 어플리케이션 개발 시 사용하기 위한 반가공 형태의 개발플랫폼이다.

비슷한 프레임워크로 spring-boot를 들수 잇다.

spring-boot와의 가장 큰 차이점이라면

1. spring-boot의 경우 모든 부분이 spring-framework 기반으로 돌아감으로 디테일한 설정이 필요한 백엔드 어플리케이션에서는 핸들링에 제약이 있으나 해당 플랫폼은 POJO 기반에 웹채널등 spring-framework가 담당하는 부분만 위하는 형태로 구성된다.

2. spring-boot가 자동자의 단위부품을 제공해주는 것이라면 해당 플랫폼에서는 항상 비슷한 형태로 개발되는 모듈은 이미 개발된 상태로 제공되어진다.

주요 특징은 아래와 같다.

## Platform for standalone application development.

## No need web server (including embedded tomcat webserer)

## suporting Enterprise Level Web Apllication via spring-framework

## Athentication and Authorization with spring-security.

## Integration with JPA (spring-data-jpa and Hibernate) and  Mybatis together

## Built in Common application component (based on web-fragment)


