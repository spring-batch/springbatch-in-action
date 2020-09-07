package com.batch.demo.library.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

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
