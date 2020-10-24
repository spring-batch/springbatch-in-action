# 스터디용 테스트 모듈

## 개발 목표
- 2020.10.17

1. 배치 설정
2. CSV 파일 기반 읽기
3. 임시 테이블에 쓰기

- 2020.10.24

1. 배치 과제 리뷰
2. 필료한 개념 리스트 정리하기

## 해당 모듈 클래스 정리
1. FileToTmpDemo
    - 전국도서관표준데이터.csv 파일의 **필요한 컬럼만 읽어** 임시테이블(TB_TMP_LIBRARY)에 저장할 수 있는 Job
    - 구성 정보
        - Job
        - Step
            - FlatFileItemReader
            - ItemProcessor
            - JpaItemWriter

2. TmpToCityDemo
    - 임시테이블에에 저장되어 있는 전국도서관표쥰데이터에서 유일한 시도군 값만 필터링하여 시도군 테이블(TB_CITY)에 저장
    - 구성 정보
        - Job
        - Step
            - JdbcCursorItemReader
            - CompositeItemProcessor
                - tmpToCityProcessor
            - CompositeItemWriter
                - JpaItemWriter
 
3. TmpToCountryDemo
    - 임시테이블에에 저장되어 있는 전국도서관표쥰데이터에서 유일한 동읍면 데이터만 필터링하여 동읍면 테이블(TB_COUNTRY)에 저장 
   - 구성 정보
        - Job
        - Step
            - JdbcCursorItemReader
            - CompositeItemProcessor
                - tmpToCityProcessor
            - CompositeItemWriter
                - JpaItemWriter

4. TmpToLibraryDemo
    - 임시테이블에에 저장되어 있는 전국도서관표쥰데이터에서 유일한 도서관 데이터만 필터링하여 도서관 테이블(TB_LIBRARY)에 저장
    - 구성 정보
        - Job
        - Step
            - JdbcCursorItemReader
            - CompositeItemProcessor
                - tmpToCityProcessor
            - CompositeItemWriter
                - JpaItemWriter

5. TmpToMultiProcessorDemo
    - Composite 테스트를 위한 demo 클래스
        - 임시 테이블에서 데이터를 한 번만 읽어 정규화 테이블(시군구, 동읍면, 도서관)에 넣도록 하기 위해서 시도
        - CompositeItemxxx 는 체이닝의 역할로 A -> B B -> C와 같은 방식의 데이터 타입을 전달하는 역할임

    - CompositeItemProcessor
    - CompositeItemWriter
    
6. TotalProcessDemo
    - 위에 5번을 제외한 Job들을 Step Bean으로 생성하여 해당 Step들을 호출하는 Job 클래스
    - Job
        - FileToTmpStep
            - 1번 Job을 Step Bean으로 생성하여 호출
        - TmpToCityStep
            - 2번 Job을 Step Bean으로 생성하여 호출
        - TmpToCountryStep
            - 3번 Job을 Step Bean으로 생성하여 호출
        - TmpToLibraryStep
            - 4번 Job을 Step Bean으로 생성하여 호출

## 배치 로그 분석
- 하나의 Job, Step, Reader, Processor, Writer 사이클

