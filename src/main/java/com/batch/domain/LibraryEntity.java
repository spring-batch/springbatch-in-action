package com.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.util.Arrays;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibraryEntity {

    @SuppressWarnings("NonAsciiCharacters")
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum CSVFields {
        도서관명("도서관명", 0),
        시도명("시도명", 1),
        시군구명("시군구명", 2),
        도서관유형("도서관유형", 3),
        휴관일("휴관일", 4),
        평일운영시작시각("평일운영시작시각", 5),
        평일운영종료시각("평일운영종료시각", 6),
        토요일운영시작시각("토요일운영시작시각", 7),
        토요일운영종료시각("토요일운영종료시각", 8),
        공휴일운영시작시각("공휴일운영시작시각", 9),
        공휴일운영종료시각("공휴일운영종료시각", 10),
        열람좌석수("열람좌석수", 11),
        자료수_도서("자료수(도서)", 12),
        자료수_연속간행물("자료수(연속간행물)", 13),
        자료수_비도서("자료수(비도서)", 14),
        대출가능권수("대출가능권수", 15),
        대출가능일수("대출가능일수", 16),
        소재지도로명주소("소재지도로명주소", 17),
        운영기관명("운영기관명", 18),
        도서관전화번호("도서관전화번호", 19),
        부지면적("부지면적", 20),
        건물면적("건물면적", 21),
        홈페이지주소("홈페이지주소", 22),
        위도("위도", 23),
        경도("경도", 24),
        데이터기준일자("데이터기준일자", 25),
        제공기관코드("제공기관코드", 26),
        제공기관명("제공기관명", 27);

        private String fieldsNm;
        private int columnIdx;

        public static String[] getFieldNmArrays() {
            return Arrays.stream(Record.RecordFields.values())
                    .map(Record.RecordFields::getFieldsNm)
                    .toArray(String[]::new);
        }
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum LibraryDBFields {
        lbrryNm("lbrryNm"),                     /* 도서관명 */
        ctprvnNm("ctprvnNm"),                    /* 시도명  */
        signguNm("signguNm"),                    /* 시군구명 */
        lbrrySe("lbrrySe"),                     /* 도서관유형    */
        closeDay("closeDay"),                    /* 휴관일      */
        weekdayOperOpenHhmm("weekdayOperOpenHhmm"),  /* 평일운영시작시간 */
        weekdayOperCloseHhmm("weekdayOperCloseHhmm"), /* 평일운영종료시간 */
        satOperOperOpenHhmm("satOperOperOpenHhmm"),  /* */
        satOperCloseHhmm("satOperCloseHhmm"),     /* */
        holidayOperOpenHhmm("holidayOperOpenHhmm"),  /* */
        holidayCloseOpenHhmm("holidayCloseOpenHhmm"), /* */
        seatCo("seatCo"),                     /* */
        bookCo("bookCo"),                     /* */
        pblictnCo("pblictnCo"),                  /* */
        noneBookCo("noneBookCo"),                 /* */
        lonCo("lonCo"),                      /* */
        lonDaycnt("lonDaycnt"),                  /* */
        rdnmadr("rdnmadr"),                     /* */
        operInstitutionNm("operInstitutionNm"),           /* */
        phoneNumber("phoneNumber"),                 /* */
        plotAr("plotAr"),                      /* */
        buldAr("buldAr"),                      /* */
        homepageUrl("homepageUrl"),                 /* */
        latitude("latitude"),                    /* */
        longitude("longitude"),                   /* */
        referenceDate("referenceDate"),        /* */
        insttCode("insttCode"),                   /* */
        insttNm("insttNm");                     /* */

        private String fieldNm;

        public static String[] getFieldArrays() {
            return Arrays.stream(LibraryDBFields.values())
                    .map(LibraryDBFields::getFieldNm)
                    .toArray(String[]::new);
        }
    }
    private String lbrryNm;                     /* 도서관명         */
    private String ctprvnNm;                    /* 시도명          */
    private String signguNm;                    /* 시군구명         */
    private String lbrrySe;                     /* 도서관유형        */
    private String closeDay;                    /* 휴관일          */
    private String weekdayOperOpenHhmm;         /* 평일운영시작시간     */
    private String weekdayOperCloseHhmm;        /* 평일운영종료시간     */
    private String satOperOperOpenHhmm;         /* 토요일운영시작시각    */
    private String satOperCloseHhmm;            /* 토요일운영종료시각    */
    private String holidayOperOpenHhmm;         /* 공휴일운영시작시각    */
    private String holidayCloseOpenHhmm;        /* 공휴일운영종료시각    */
    private String seatCo;                     /* 열람좌석수        */
    private String bookCo;                     /* 자료수(도서)      */
    private String pblictnCo;                  /* 자료수(연속간행물)   */
    private String noneBookCo;                 /* 자료수(비도서)     */
    private String lonCo;                      /* 대출가능권수       */
    private String lonDaycnt;                  /* 대출가능일수       */
    private String rdnmadr;                     /* 소재지도로명주소     */
    private String operInstitutionNm;           /* 운영기관명        */
    private String phoneNumber;                 /* 도서관전화번호      */
    private String plotAr;                      /* 부지면적         */
    private String buldAr;                      /* 건물면적         */
    private String homepageUrl;                 /* 홈페이지주소       */
    private String latitude;                    /* 위도           */
    private String longitude;                   /* 경도           */
    private String referenceDate;        /* 데이터기준일자      */
    private String insttCode;                   /* 제공기관코드       */
    private String insttNm;                     /* 제공기관명        */

}
