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
- 파일 데이터를 읽어 DTO에 저장
    - csv 파일의 필드정보를 갖는 DTO를 작성
    - 해당 DTO 클래스를 `refrect`를 통해 전체 `field`명 조회하는 `getFieldNames()` 작성
    -  파일에서 읽어와 저장한 `dto`의 데이터를 `entity`로 저장하는 `toEntity()` 메서드 작성
    
