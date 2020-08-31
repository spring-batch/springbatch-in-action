package com.batch.domain.oracle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CSV_TABLE")
public class Library {

    @SuppressWarnings("NonAsciiCharacters")
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum CSVFields {
        도서관명(0, "도서관명"),
        시도명(1, "시도명"),
        시군구명(2, "시군구명"),
        도서관유형(3, "도서관유형"),
        휴관일(4, "휴관일"),
        평일운영시작시각(5, "평일운영시작시각"),
        평일운영종료시각(6, "평일운영종료시각"),
        토요일운영시작시각(7, "토요일운영시작시각"),
        토요일운영종료시각(8, "토요일운영종료시각"),
        공휴일운영시작시각(9, "공휴일운영시작시각"),
        공휴일운영종료시각(10, "공휴일운영종료시각"),
        열람좌석수(11, "열람좌석수"),
        자료수_도서(12, "자료수(도서)"),
        자료수_연속간행물(13, "자료수(연속간행물)"),
        자료수_비도서(14, "자료수(비도서)"),
        대출가능권수(15, "대출가능권수"),
        대출가능일수(16, "대출가능일수"),
        소재지도로명주소(17, "소재지도로명주소"),
        운영기관명(18, "운영기관명"),
        도서관전화번호(19, "도서관전화번호"),
        부지면적(20, "부지면적"),
        건물면적(21, "건물면적"),
        홈페이지주소(22, "홈페이지주소"),
        위도(23, "위도"),
        경도(24, "경도"),
        데이터기준일자(25, "데이터기준일자"),
        제공기관코드(26, "제공기관코드"),
        제공기관명(27, "제공기관명");

        private int columnIdx;
        private String fieldsNm;

        public static String[] getFieldNmArrays() {
            return Arrays.stream(CSVFields.values())
                    .map(CSVFields::getFieldsNm)
                    .toArray(String[]::new);
        }
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum LibraryDBFields {
        lbrryNm("lbrryNm", "LBRRY_NM"),                     /* 도서관명 */
        ctprvnNm("ctprvnNm", "CTPRVN_NM"),                  /* 시도명  */
        signguNm("signguNm", "SIGNGU_NM"),                  /* 시군구명 */
        lbrrySe("lbrrySe", "LBRRY_SE"),                     /* 도서관유형    */
        closeDay("closeDay", "CLOSE_DAY"),                  /* 휴관일      */
        weekdayOperOpenHhmm("weekdayOperOpenHhmm", "WEEKDAY_OPER_OPEN_HHMM"),       /* 평일운영시작시간 */
        weekdayOperCloseHhmm("weekdayOperCloseHhmm", "WEEKDAY_OPER_CLOSE_HHMM"),    /* 평일운영종료시간 */
        satOperOperOpenHhmm("satOperOperOpenHhmm", "SAT_OPER_OPEN_HHMM"),           /* 토요일운영시작시간 */
        satOperCloseHhmm("satOperCloseHhmm", "SAT_OPER_CLOSE_HHMM"),                /* 토요일운영종료시간 */
        holidayOperOpenHhmm("holidayOperOpenHhmm", "HOLIDAY_OPER_OPEN_HHMM"),       /* 공휴일운영시작시간 */
        holidayCloseOpenHhmm("holidayOperCloseHhmm", "HOLIDAY_OPER_CLOSE_HHMM"),    /* 공휴일운영종료시간 */
        seatCo("seatCo", "SEAT_CO"),                                                /* 열람좌석 수*/
        bookCo("bookCo", "BOOK_CO"),                                                /* */
        pblictnCo("pblictnCo", "PBLICTN_CO"),                                       /* */
        noneBookCo("noneBookCo", "NONEBOOK_CO"),                                    /* */
        lonCo("lonCo", "LON_CO"),                                                   /* */
        lonDaycnt("lonDaycnt", "LONDAY_CNT"),                                       /* */
        rdnmadr("rdnmAdr", "RDNM_ADR"),                                             /* */
        operInstitutionNm("operInstitutionNm", "OPERINSTITUTION_NM"),               /* */
        lbrryPhoneNumber("lbrryPhoneNumber", "LBRRY_PHONENUMBER"),                  /* */
        plotAr("plotAr", "PLOT_AR"),                                                /* */
        buldAr("buldAr", "BULD_AR"),                                                /* */
        homepageUrl("homepageUrl", "HOMEPAGEURL"),                                  /* */
        latitude("latitude", "LATITUDE"),                                           /* */
        longitude("longitude", "LONGITUDE"),                                        /* */
        referenceDate("referenceDate", "REFERENCE_DATE"),                           /* */
        insttCode("insttCode", "INSTT_CODE"),                                       /* */
        insttNm("insttNm", "INSTT_NM");                                             /* */

        private String classFieldNm;
        private String dbFieldNm;

        public static String[] getFieldArrays() {
            return Arrays.stream(LibraryDBFields.values())
                    .map(LibraryDBFields::getClassFieldNm)
                    .toArray(String[]::new);
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lbrry_code", nullable = false)
    private Long lbrryCode;

    @Column(name = "lbrry_nm")
    private String lbrryNm;                     /* 도서관명         */
    @Column(name = "ctprvn_nm")
    private String ctprvnNm;                    /* 시도명          */
    @Column(name = "signgu_nm")
    private String signguNm;                    /* 시군구명         */
    @Column(name = "lbrry_se")
    private String lbrrySe;                     /* 도서관유형        */
    @Column(name = "close_day")
    private String closeDay;                    /* 휴관일          */
    @Column(name = "weekday_oper_open_hhmm")
    private String weekdayOperOpenHhmm;         /* 평일운영시작시간     */
    @Column(name = "weekday_oper_close_hhmm")
    private String weekdayOperCloseHhmm;        /* 평일운영종료시간     */
    @Column(name = "sat_oper_open_hhmm")
    private String satOperOpenHhmm;         /* 토요일운영시작시각    */
    @Column(name = "sat_oper_close_hhmm")
    private String satOperCloseHhmm;            /* 토요일운영종료시각    */
    @Column(name = "holiday_oper_open_hhmm")
    private String holidayOperOpenHhmm;         /* 공휴일운영시작시각    */
    @Column(name = "holiday_oper_close_hhmm")
    private String holidayOperCloseHhmm;        /* 공휴일운영종료시각    */
    @Column(name = "seat_co")
    private String seatCo;                     /* 열람좌석수        */
    @Column(name = "book_co")
    private String bookCo;                     /* 자료수(도서)      */
    @Column(name = "pblictn_co")
    private String pblictnCo;                  /* 자료수(연속간행물)   */
    @Column(name = "nonebook_co")
    private String noneBookCo;                 /* 자료수(비도서)     */
    @Column(name = "lon_co")
    private String lonCo;                      /* 대출가능권수       */
    @Column(name = "londay_cnt")
    private String lonDaycnt;                  /* 대출가능일수       */
    @Column(name = "rdnm_adr")
    private String rdnmAdr;                     /* 소재지도로명주소     */
    @Column(name = "operinstitution_nm")
    private String operInstitutionNm;           /* 운영기관명        */
    @Column(name = "lbrry_phonenumber")
    private String lbrryPhoneNumber;                 /* 도서관전화번호      */
    @Column(name = "plot_ar")
    private String plotAr;                      /* 부지면적         */
    @Column(name = "buld_ar")
    private String buldAr;                      /* 건물면적         */
    private String homepageUrl;                 /* 홈페이지주소       */
    private String latitude;                    /* 위도           */
    private String longitude;                   /* 경도           */
    @Column(name = "reference_date")
    private String referenceDate;               /* 데이터기준일자      */
    @Column(name = "instt_code")
    private String insttCode;                   /* 제공기관코드       */
    @Column(name = "instt_nm")
    private String insttNm;                     /* 제공기관명        */

}
