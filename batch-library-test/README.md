# 스터디용 테스트 모듈

## 매주 스터디 회고

- 2020.10.17

1. 배치 설정
2. CSV 파일 기반 읽기
3. 임시 테이블에 쓰기

- 2020.10.24

1. 배치 과제 리뷰
2. 필료한 개념 리스트 정리하기

- 2020.10.31

1. 배치 프로세스 개선점 찾기
2. 배치 관리

- 2020.11.07

1. 배치 엑셀 리포트 작성
2. 배치 전체 플로우 정리

## 해당 모듈 클래스 정리

1. FileToTmpPrototype
	- 전국도서관표준데이터.csv 파일의 **필요한 컬럼만 읽어** 임시테이블(TB_TMP_LIBRARY)에 저장할 수 있는 Job
	- 구성 정보
		- Job
		- Step
			- FlatFileItemReader
			- ItemProcessor
			- JpaItemWriter

2. TmpToCityPrototype
	- 임시테이블에에 저장되어 있는 전국도서관표쥰데이터에서 유일한 시도군 값만 필터링하여 시도군 테이블(TB_CITY)에 저장
	- 구성 정보
		- Job
		- Step
			- JdbcCursorItemReader
			- CompositeItemProcessor
				- tmpToCityProcessor
			- CompositeItemWriter
				- JpaItemWriter

3. TmpToCountryPrototype
	- 임시테이블에에 저장되어 있는 전국도서관표쥰데이터에서 유일한 동읍면 데이터만 필터링하여 동읍면 테이블(TB_COUNTRY)에 저장
	- 구성 정보
		- Job
		- Step
			- JdbcCursorItemReader
			- CompositeItemProcessor
				- tmpToCityProcessor
			- CompositeItemWriter
				- JpaItemWriter

4. TmpToLibraryPrototype
	- 임시테이블에에 저장되어 있는 전국도서관표쥰데이터에서 유일한 도서관 데이터만 필터링하여 도서관 테이블(TB_LIBRARY)에 저장
	- 구성 정보
		- Job
		- Step
			- JdbcCursorItemReader
			- CompositeItemProcessor
				- tmpToCityProcessor
			- CompositeItemWriter
				- JpaItemWriter

5. TmpToMultiWriterPrototype
	- Composite 테스트를 위한 demo 클래스
		- 임시 테이블에서 데이터를 한 번만 읽어 정규화 테이블(시군구, 동읍면, 도서관)에 넣도록 하기 위해서 시도
		- CompositeItemxxx 는 체이닝의 역할로 A -> B B -> C와 같은 방식의 데이터 타입을 전달하는 역할임

	- CompositeItemProcessor
	- CompositeItemWriter

	- 개선
		- CompositeItemWriter를 사용하여 Delegate 처리
		- 각 Delegates에 등록된 Writer는 TmpEntity 데이터를 City, Country, Library 세 테이블에 각각 유일한 값으로만 저장

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

7. MultiDBToReportPrototype
	- Job
		- Multi 테이블에 있는 데이터를 가공하여 Report에 저장하는 Step을 수행

8. LibrarySummaryDemo
	- Job
		- FileToTmpStep
		- [TmpToMultiDbStep](/src/main/java/kr/seok/library/step/TmpToMultiDbStep.java)
			- CompositeItemWriter를 이렇게 Jpa로 사용하는 경우 하나의 ItemWriter안에서 save로 가능
			- Jpa외에 다른 Writer를 사용하는 경우에는 각 ItemWriter를 구현해야하기 때문에 분리가 필요
		- [MultiDBToReportStep](/src/main/java/kr/seok/library/step/MultiDBToReportStep.java)
			- 정규화된 테이블로부터 필요한 데이터를 가져와 Excel로 작성하는 Step
			- [AbstractExcelItemWriter](/src/main/java/kr/seok/library/writer/AbstractExcelItemWriter.java)
				- 공통으로 사용할 수 있는 엑셀 작성 추상클래스

	- 결과
		- 전체 도서관 파일 데이터에 대해 임시 테이블에 저장
		- 임시 테이블에 저장된 데이터를 정규화 테이블로 구성
		- 정규화 테이블에 저장된 데이터를 Report로 생성하는 작업을 수행

	- 문제점
		- (Writer) 임시테이블에서 정규화 처리된 각 테이블에 적재하는 경우 기존의 경우 Step 별로 City, Country, Library 테이블에 저장하는 경우 read, filter, write count를 저장한다.
		- (Manage) 기존의 내용을 개선하여 하나의 Step으로 구성하는 경우 Writer가 한 번에 처리 되기 때문에 이력 테이블의 READ_COUNT, FILTER_COUNT, WRITE_COUNT로 관리할 수 없다.
		- 데이터를 읽고, 필터링하고, 쓰기에 대한 이력을 관리하고 싶은 경우, 임시테이블의 전체 데이터를 읽고 각 Step으로 나누어 쓰는 방향으로 구성을 사용해야함
		- (Excel) 엑셀 작성 시 동일한 작업을 필드 별로 작성하여 동적으로 생성할 수 없는 문제

	- 개선한 내용
		- 임시 테이블에서 가져온 데이터를 정규화 처리하여 각각의 테이블에 저장하는 방식을 각각의 테이블별 ItemWriter를 만들어 CompositeItemWriter를 통해 하나의 트랜잭션으로 처리할 수 있도록 개선
		- 엑셀 작성 코드 작성 시 공통으로 사용할 수 있는 코드를 추상화 클래스를 만들어 상속받아 사용할 수 있도록
		  개선 [AbstractExcelItemWriter](/src/main/java/kr/seok/library/writer/AbstractExcelItemWriter.java)

## 배치 로그 분석

- 하나의 Job, Step, Reader, Processor, Writer 사이클
