# Spring Batch with JPA

- 기존 구성을 JPA 기반으로 수정

## 추가

- 법정동코드로 지역 코드 테이블 생성
	- txt 파일로 제공 > DB 스키마 구성

## 프로세스 순서

1. Step 1

- FlatFileItemReader를 이용해서 csv 파일 읽어 JpaItemWriter를 이용하여 DB에 데이터 적재
- 파일 경로 읽기: setResource
- 파일 인코딩 설정: setEncoding
- 헤더 row skip하기: setLinesToSkip
- 각 row 읽기: setLineMapper

2. Step 2

- JpaPagingItemReader와 JpaItemWriter를 이용하여 데이터 조회 및 등록
- Entity 간의 관계 설정, 유일한 값을 저장하여 등록해야 하는 이슈
- 반복되는 코드를 제거하는 것이 핵심

3. Step 3

- JpaPagingItemReader > ExcelItemWriter
- 여러 테이블에 등록된 데이터를 정제하여 필요한 내용의 리포트 생성  
