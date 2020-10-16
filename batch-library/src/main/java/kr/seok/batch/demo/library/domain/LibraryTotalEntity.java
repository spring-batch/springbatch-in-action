package kr.seok.batch.demo.library.domain;

import lombok.*;

import java.math.BigDecimal;
import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryTotalEntity {
    @SuppressWarnings("NonAsciiCharacters")
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum LibraryTotalExcelFields {
        도서관코드(0, "도서관코드"),
        도서관명(1, "도서관명"),

        시도코드(2, "시도코드"),
        시도명(3, "시도명"),

        시군구코드(4, "시군구코드"),
        시군구명(5, "시군구명"),

        도서관유형(6, "도서관유형"),
        휴관일(7, "휴관일"),
        평일운영시작시각(8, "평일운영시작시각"),
        평일운영종료시각(9, "평일운영종료시각"),
        토요일운영시작시각(10, "토요일운영시작시각"),
        토요일운영종료시각(11, "토요일운영종료시각"),
        공휴일운영시작시각(12, "공휴일운영시작시각"),
        공휴일운영종료시각(13, "공휴일운영종료시각"),
        열람좌석수(14, "열람좌석수"),
        자료수_도서(15, "자료수(도서)"),
        자료수_연속간행물(16, "자료수(연속간행물)"),
        자료수_비도서(17, "자료수(비도서)"),
        대출가능권수(18, "대출가능권수"),
        대출가능일수(19, "대출가능일수"),
        소재지도로명주소(20, "소재지도로명주소"),
        운영기관명(21, "운영기관명"),
        도서관전화번호(22, "도서관전화번호"),
        부지면적(23, "부지면적"),
        건물면적(24, "건물면적"),
        홈페이지주소(25, "홈페이지주소"),
        위도(26, "위도"),
        경도(27, "경도"),
        데이터기준일자(28, "데이터기준일자"),
        제공기관코드(29, "제공기관코드"),
        제공기관명(30, "제공기관명");

        private int columnIdx;
        private String fieldsNm;

        public static String[] getFieldNmArrays() {
            return Arrays.stream(LibraryTotalExcelFields.values())
                    .map(LibraryTotalExcelFields::getFieldsNm)
                    .toArray(String[]::new);
        }
    }

    private Integer lbrryCode;                    /* 도서관명         */
    private String lbrryNm;                     /* 도서관명         */
    private Integer ctprvnCode;                   /* 시도명          */
    private String ctprvnNm;                   /* 시도명          */
    private Integer signguCode;                   /* 시군구명         */
    private String signguNm;                   /* 시군구명         */
    private String lbrrySe;                     /* 도서관유형        */
    private String closeDay;                    /* 휴관일          */
    private String weekdayOperOpenHhmm;         /* 평일운영시작시간     */
    private String weekdayOperCloseHhmm;        /* 평일운영종료시간     */
    private String satOperOpenHhmm;             /* 토요일운영시작시각    */
    private String satOperCloseHhmm;            /* 토요일운영종료시각    */
    private String holidayOperOpenHhmm;         /* 공휴일운영시작시각    */
    private String holidayOperCloseHhmm;        /* 공휴일운영종료시각    */
    private String seatCo;                      /* 열람좌석수        */
    private String bookCo;                      /* 자료수(도서)      */
    private String pblictnCo;                   /* 자료수(연속간행물)   */
    private String nonebookCo;                  /* 자료수(비도서)     */
    private String lonCo;                       /* 대출가능권수       */
    private String londayCnt;                   /* 대출가능일수       */
    private String rdnmAdr;                     /* 소재지도로명주소     */
    private String operinstitutionNm;           /* 운영기관명        */
    private String lbrryPhonenumber;            /* 도서관전화번호      */
    private String homepageUrl;                 /* 홈페이지주소       */
    private BigDecimal plotAr;                  /* 부지면적         */
    private BigDecimal buldAr;                  /* 건물면적         */
    private BigDecimal latitude;                /* 위도           */
    private BigDecimal longitude;               /* 경도             */
    private String referenceDate;               /* 데이터기준일자      */
    private String insttCode;                   /* 제공기관코드       */
    private String insttNm;                     /* 제공기관명         */
    /* aggregate */
    private Integer librryNumBySido;            /* 시도 별 도서관 수   */
    private Integer librryNumBySigngu;          /* 시군구 별 도서관 수  */
    private Integer librryNumByType;            /* 유형 별 도서관 수   */
}
