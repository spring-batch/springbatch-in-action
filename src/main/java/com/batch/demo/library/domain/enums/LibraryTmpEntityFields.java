package com.batch.demo.library.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum LibraryTmpEntityFields {
    lbrryNm("lbrryNm", "LBRRY_NM"),                     /* 도서관명 */
    ctprvnNm("ctprvnNm", "CTPRVN_NM"),
    signguNm("signguNm", "SIGNGU_NM"),
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
    plotAr("plotAr", "PLOT_AR"),
    buldAr("buldAr", "BULD_AR"),
    homepageUrl("homepageUrl", "HOMEPAGEURL"),                                  /* */

    latitude("latitude", "LATITUDE"),
    longitude("longitude", "LONGITUDE"),
    referenceDate("referenceDate", "REFERENCE_DATE"),
    insttCode("insttCode", "INSTT_CODE"),
    insttNm("insttNm", "INSTT_NM");

    private String classFieldNm;
    private String dbFieldNm;

    public static String[] getClassFieldArrays() {
        return Arrays.stream(LibraryTmpEntityFields.values())
                .map(LibraryTmpEntityFields::getClassFieldNm)
                .toArray(String[]::new);
    }
    public static String[] getDBFieldArrays() {
        return Arrays.stream(LibraryTmpEntityFields.values())
                .map(LibraryTmpEntityFields::getDbFieldNm)
                .toArray(String[]::new);
    }
}
