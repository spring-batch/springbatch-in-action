package com.batch.domain.oracle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_LBRRY")
public class Library {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum LibraryDBFields {
        lbrryCode("lbrryCode","LBRRY_CODE"),
        lbrryNm("lbrryNm", "LBRRY_NM"),                     /* 도서관명 */
        ctprvnCode("ctprvnCode", "CTPRVN_CODE"),            /* 시도 코드  */
        signguCode("signguCode", "SIGNGU_CODE"),            /* 시군구 코드 */
        lbrrySe("lbrrySe", "LBRRY_SE"),                     /* 도서관유형    */
        closeDay("closeDay", "CLOSE_DAY"),                  /* 휴관일      */
        weekdayOperOpenHhmm("weekdayOperOpenHhmm", "WEEKDAY_OPER_OPEN_HHMM"),       /* 평일운영시작시간 */
        weekdayOperCloseHhmm("weekdayOperCloseHhmm", "WEEKDAY_OPER_CLOSE_HHMM"),    /* 평일운영종료시간 */
        satOperOpenHhmm("satOperOpenHhmm", "SAT_OPER_OPEN_HHMM"),                   /* 토요일운영시작시간 */
        satOperCloseHhmm("satOperCloseHhmm", "SAT_OPER_CLOSE_HHMM"),                /* 토요일운영종료시간 */
        holidayOperOpenHhmm("holidayOperOpenHhmm", "HOLIDAY_OPER_OPEN_HHMM"),       /* 공휴일운영시작시간 */
        holidayCloseOpenHhmm("holidayOperCloseHhmm", "HOLIDAY_OPER_CLOSE_HHMM"),    /* 공휴일운영종료시간 */
        seatCo("seatCo", "SEAT_CO"),                                                /* 열람좌석 수*/
        bookCo("bookCo", "BOOK_CO"),                                                /* */
        pblictnCo("pblictnCo", "PBLICTN_CO"),                                       /* */
        noneBookCo("noneBookCo", "NONEBOOK_CO"),                                    /* */
        lonCo("lonCo", "LON_CO"),                                                   /* */
        lonDayCnt("lonDayCnt", "LONDAY_CNT"),                                       /* */
        rdnmAdr("rdnmAdr", "RDNM_ADR"),                                             /* */
        operInstitutionNm("operInstitutionNm", "OPERINSTITUTION_NM"),               /* */
        lbrryPhoneNumber("lbrryPhoneNumber", "LBRRY_PHONENUMBER"),                  /* */
        homepageUrl("homepageUrl", "HOMEPAGEURL");                                  /* */

        private String classFieldNm;
        private String dbFieldNm;

        public static String[] getClassFieldArrays() {
            return Arrays.stream(LibraryDBFields.values())
                    .map(LibraryDBFields::getClassFieldNm)
                    .toArray(String[]::new);
        }
        public static String[] getDBFieldArrays() {
            return Arrays.stream(LibraryDBFields.values())
                    .map(LibraryDBFields::getDbFieldNm)
                    .toArray(String[]::new);
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum LibraryDetialDBFields {
        lbrryCode("lbrryCode","LBRRY_CODE"),                                        /* */
        plotAr("plotAr", "PLOT_AR"),                                                /* */
        buldAr("buldAr", "BULD_AR"),                                                /* */
        latitude("latitude", "LATITUDE"),                                           /* */
        longitude("longitude", "LONGITUDE"),                                        /* */
        referenceDate("referenceDate", "REFERENCE_DATE"),                           /* */
        insttCode("insttCode", "INSTT_CODE"),                                       /* */
        insttNm("insttNm", "INSTT_NM");                                             /* */

        private String classFieldNm;
        private String dbFieldNm;

        public static String[] getClassFieldArrays() {
            return Arrays.stream(LibraryDetialDBFields.values())
                    .map(LibraryDetialDBFields::getClassFieldNm)
                    .toArray(String[]::new);
        }
        public static String[] getDBFieldArrays() {
            return Arrays.stream(LibraryDetialDBFields.values())
                    .map(LibraryDetialDBFields::getDbFieldNm)
                    .toArray(String[]::new);
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lbrry_code", nullable = false)
    private Long lbrryCode;                     /* 도서관 코드       */

    @Column(name = "lbrry_nm")
    private String lbrryNm;                     /* 도서관명         */

    @Column(name = "ctprvn_code")
    private Long ctprvnCode;                    /* 시도 코드        */

    @Column(name = "signgu_code")
     private Long signguCode;                   /* 시군구 코드       */

    @Column(name = "lbrry_se")
    private String lbrrySe;                     /* 도서관유형        */
    @Column(name = "close_day")
    private String closeDay;                    /* 휴관일          */
    @Column(name = "weekday_oper_open_hhmm")
    private String weekdayOperOpenHhmm;         /* 평일운영시작시간     */
    @Column(name = "weekday_oper_close_hhmm")
    private String weekdayOperCloseHhmm;        /* 평일운영종료시간     */
    @Column(name = "sat_oper_open_hhmm")
    private String satOperOpenHhmm;             /* 토요일운영시작시각    */
    @Column(name = "sat_oper_close_hhmm")
    private String satOperCloseHhmm;            /* 토요일운영종료시각    */
    @Column(name = "holiday_oper_open_hhmm")
    private String holidayOperOpenHhmm;         /* 공휴일운영시작시각    */
    @Column(name = "holiday_oper_close_hhmm")
    private String holidayOperCloseHhmm;        /* 공휴일운영종료시각    */
    @Column(name = "seat_co")
    private Integer seatCo;                     /* 열람좌석수        */
    @Column(name = "book_co")
    private Integer bookCo;                     /* 자료수(도서)      */
    @Column(name = "pblictn_co")
    private Integer pblictnCo;                  /* 자료수(연속간행물)   */
    @Column(name = "nonebook_co")
    private Integer noneBookCo;                 /* 자료수(비도서)     */
    @Column(name = "lon_co")
    private Integer lonCo;                      /* 대출가능권수       */
    @Column(name = "londay_cnt")
    private Integer lonDayCnt;                  /* 대출가능일수       */
    @Column(name = "rdnm_adr")
    private String rdnmAdr;                     /* 소재지도로명주소     */
    @Column(name = "operinstitution_nm")
    private String operInstitutionNm;           /* 운영기관명        */
    @Column(name = "lbrry_phonenumber")
    private String lbrryPhoneNumber;            /* 도서관전화번호      */
    @Column(name = "plot_ar")
    private BigDecimal plotAr;                  /* 부지면적         */
    @Column(name = "buld_ar")
    private BigDecimal buldAr;                  /* 건물면적         */
    private String homepageUrl;                 /* 홈페이지주소       */
    private BigDecimal latitude;                /* 위도           */
    private BigDecimal longitude;               /* 경도           */
    @Column(name = "reference_date")
    private LocalDateTime referenceDate;        /* 데이터기준일자      */
    @Column(name = "instt_code")
    private Integer insttCode;                   /* 제공기관코드       */
    @Column(name = "instt_nm")
    private String insttNm;                     /* 제공기관명        */

}
