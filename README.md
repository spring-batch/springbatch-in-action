# SpringBatch Saturday Study

## 스터디
- 일자: 2020.08.22
- 장소: 홍대입구역 ['셀스스터디'](https://hd.sels.co.kr/)
- 인원: 6명

## 배치 관련 업무 공유
1. 통계 테이블 구성 (파티셔닝)
2. 검색 데이터 통합 테이블
3. 정산
4. 데이터 집계

## 스터디 진행 방식
1. 스프링 이론
    1) [Spring.io](https://docs.spring.io/spring-batch/docs/current/reference/html/index.html)

2. 스프링 배치 실습 아이디어
    1) 도서관 데이터 -> csv 파일
    2) LoL 데이터
    3) 코로나 데이터

3. 스프링 배치 관리
    1) Retry 전략
    2) Jenkins 를 통한 schedule
    3) Transaction 처리 방법

4. 참고 사이트
    1) [기억보다 기록을](https://jojoldu.tistory.com/category/Spring%20Batch)
    2) [quartz](https://blog.kingbbode.com/posts/spring-batch-quartz)
    3) [Jenkins로 배치](https://jojoldu.tistory.com/313)

## 스터디 회의록
1. [주제 정하기](docs/README.md)


## 프로젝트 Spec
- SpringFramework

|library|version|
|:-----|:-----|
|spring-boot-starter-batch|2.1.9|
|spring-boot-starter-data-jpa|2.1.9|

- Excel

|library|version|
|:-----|:-----|
|poi-ooxml|4.1.2|
|poi|4.1.2|

- DBMS

|library|version|
|:-----|:-----|
|h2|1.4.119|
|ojdbc8-production|19.7.0.0|
|mysql-connector-java|8.0.17|

### 해당 프로젝스 설정 시 db_mysql, db_oracle 내용
- MySQL
```yaml
spring:

  # JPA 설정
  jpa:
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
#    generate-ddl: true
    show_sql: true
    properties:
      hibernate:
        format_sql: true
#        ddl-auto: update
        ddl-auto: none # 실행하지 않음
#       ddl-auto: create-drop
#        ddl-auto: create
#       ddl-auto: validate

  # MySQL DataSource
  datasource:
    # Spring Batch Schema MySQL
    batch:
      jdbc-url: jdbc:mysql://{ip}:{port}/{schema}
      username: {db_username}
      password: {db_password}
      driver-class-name: com.mysql.cj.jdbc.Driver
      sql-script-encoding: UTF-8

    # Domain DB
    domain:
      jdbc-url: jdbc:mysql://{ip}:{port}/{schema}
      username: 
      password: 
      driver-class-name: com.mysql.cj.jdbc.Driver
      sql-script-encoding: UTF-8
```

- Oracle
```yaml
spring:
  # JPA 설정
  jpa:
    database-platform: org.hibernate.dialect.Oracle10gDialect

  # Oracle DataSource
  datasource:
    # DB Tuning Schema
    oracle:
      jdbc-url: jdbc:oracle:thin:@//{ip}:{port}/{sid}
      username: {db_username}
      password: {db_password}
      driver-class-name: oracle.jdbc.OracleDriver
      sql-script-encoding: UTF-8

```