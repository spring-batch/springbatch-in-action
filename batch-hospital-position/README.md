# 서울특별시 병의원 위치 정보 데이터 관리
- [서울 열린데이터 광장 - 서울특별시 병의원 위치 정보](http://data.seoul.go.kr/dataList/OA-20337/S/1/datasetView.do)

## 데이터 정보
- 공공데이터포털 전국 병의원 위치 찾기 api를 통해 서울특별시 병의원 위치 정보데이터를 받아 데이터 공개

- 필드 정보

|컬럼No|필드명(영문)|필드명(한글)|
|:---|:---|:---|
|1|HPID|기관ID|
|2|DUTYADDR|주소|
|3|DUTYDIV|병원분류|
|4|DUTYDIVNAM|병원분류명|
|5|DUTYEMCLS|응급의료기관코드|
|6|DUTYEMCLSNAME|응급의료기관코드명|
|7|DUTYERYN|응급실운영여부(1/2)|
|8|DUTYETC|비고|
|9|DUTYINF|기관설명상세|
|10|DUTYMAPIMG|간이약도|
|11|DUTYNAME|기관명|
|12|DUTYTEL1|대표전화1|
|13|DUTYTEL3|응급실전화|
|14|DUTYTIME1C|진료시간(월요일)C|
|15|DUTYTIME2C|진료시간(화요일)C|
|16|DUTYTIME3C|진료시간(수요일)C|
|17|DUTYTIME4C|진료시간(목요일)C|
|18|DUTYTIME5C|진료시간(금요일)C|
|19|DUTYTIME6C|진료시간(토요일)C|
|20|DUTYTIME7C|진료시간(일요일)C|
|21|DUTYTIME8C|진료시간(공휴일)C|
|22|DUTYTIME1S|진료시간(월요일)S|
|23|DUTYTIME2S|진료시간(화요일)S|
|24|DUTYTIME3S|진료시간(수요일)S|
|25|DUTYTIME4S|진료시간(목요일)S|
|26|DUTYTIME5S|진료시간(금요일)S|
|27|DUTYTIME6S|진료시간(토요일)S|
|28|DUTYTIME7S|진료시간(일요일)S|
|29|DUTYTIME8S|진료시간(공휴일)S|
|30|POSTCDN1|우편번호1|
|31|POSTCDN2|우편번호2|
|32|WGS84LON|병원경도|
|33|WGS84LAT|병원위도|
|34|WORK_DTTM작업시간|

## 데이터 관리
- 공공데이터에서 제공하는 데이터를 분석하여 사용할 수 있도록 스키마 재정의 및 재가공

## 배치 프로세스 작성
1. 파일 데이터를 읽어 DTO에 저장
    - csv 파일의 필드정보를 갖는 DTO를 작성
    - 해당 DTO 클래스를 `refrect`를 통해 전체 `field`명 조회하는 `getFieldNames()` 작성
    - 파일에서 chunk 단위로 읽어와 저장한 `dto`의 데이터를 `entity`로 저장하는 `toEntity()` 메서드 작성

2. 임시 테이블을 필요한 중요 정보와 세부 정보로 구분하여 테이블관리
   - 기관 코드를 기준으로 병원 `기본정보`, `위치`, `시간` 필드로 분류하여 해당 테이블로 정의한다. 
   - 병원기본정보: TB_HOSPITAL_INF
   - 병원위치정보: TB_HOSPITAL_POS
   - 병원시간정보: TB_HOSPITAL_DTT

## 코드레벨 전체 아키텍처
- 특정 작업에 대한 `Job` Prototype 클래스 작성 후 `Step`으로 리펙토링
- 구현 방식 & 속도를 생각하면서 다양하게 구현할 것

## 개선 사항
- 배치 bulk 프로세스 
   - 속도 개선
      - 상황
         - 파일을 읽는 속도는 FlatFileItemReader와 일반 BufferedReader 와의 속도차이는 미미
         - 파일을 쓰는 속도에서 차이가 발생하고 줄일 수 있는 것을 확인
         - 데이터의 PK가 이미 존재하기 때문에 `@GeneratedValue`를 사용하지 않는 상황
      - jpaItemWriter
         - 작동 방식
            - JpaItemWriter를 통한 `write()` 작업이 `merge()`를 기본 Mode로 구현
            - `usePersist(true)` 의 설정이 없는 경우 `merge()` 작동 방식으로 인하여 select(id 채번) -> insert(data 입력)로 수행
            - `usePersist(true)`이 설정되어 있는 경우 `persist()`로 작동하여 `select`를 생략, 바로 `insert()` 처리
            - `persist()`로 사용하는 경우 새로운 객체를 저장할 때만 사용해야함
         - 단점
            - 기본적으로 bulk insert를 수행하지 못함
            - auto-increment가 아닌 경우 batch insert 사용 가능
        
      - JdbcItemWriter
         - 연관관계가 들어가면 insert가 필요한 경우 직접 구현해야 하는 부분이 너무 많음
         - 컴파일체크, 타입, SQL 쿼리 문자열 문제 발생 가능
         - 속도가 비교적 빠름
      
      - Jdbc & Jpa (연관관계 작성시 해당 방법으로 처리예정)
         - OneToMany 연관관계 조회하는 경우, 부모 Entity를 조회하여 PK값을 저장
         - 부모 ID값으로 자식 Entity를 JdbcItemWriter로 batch insert 처리

   - [참고](https://jojoldu.tistory.com/507)
   

