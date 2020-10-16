package kr.seok.batch.demo.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibraryDTO {

    private String lbrryNm;                     /* 도서관명         */
    private String ctprvnNm;                    /* 시도명          */
    private String signguNm;                    /* 시군구명         */
    private String lbrrySe;                     /* 도서관유형        */
    private String closeDay;                    /* 휴관일          */
    private String weekdayOperOpenHhmm;         /* 평일운영시작시간     */
    private String weekdayOperCloseHhmm;        /* 평일운영종료시간     */
    private String satOperOpenHhmm;             /* 토요일운영시작시각    */
    private String satOperCloseHhmm;            /* 토요일운영종료시각    */
    private String holidayOperOpenHhmm;         /* 공휴일운영시작시각    */
    private String holidayOperCloseHhmm;        /* 공휴일운영종료시각    */
    private String seatCo;                     /* 열람좌석수        */
    private String bookCo;                     /* 자료수(도서)      */
    private String pblictnCo;                  /* 자료수(연속간행물)   */
    private String noneBookCo;                 /* 자료수(비도서)     */
    private String lonCo;                      /* 대출가능권수       */
    private String lonDayCnt;                  /* 대출가능일수       */
    private String rdnmAdr;                     /* 소재지도로명주소     */
    private String operInstitutionNm;           /* 운영기관명        */
    private String lbrryPhoneNumber;            /* 도서관전화번호      */
    private String plotAr;                      /* 부지면적         */
    private String buldAr;                      /* 건물면적         */
    private String homepageUrl;                 /* 홈페이지주소       */
    private String latitude;                /* 위도           */
    private String longitude;               /* 경도           */
    private String referenceDate;        /* 데이터기준일자      */
    private String insttCode;                   /* 제공기관코드       */
    private String insttNm;                     /* 제공기관명        */

    private String ctprvnCode;                    /* 시도코드         */
    private String signguCode;                    /* 시군구코드          */

    @Builder
    public LibraryTmpEntity toEntity () {
        return LibraryTmpEntity.builder()
                .lbrryNm(lbrryNm)
                .ctprvnNm(ctprvnNm)
                .signguNm(signguNm)
                .lbrrySe(lbrrySe)
                .closeDay(closeDay)
                .homepageUrl(homepageUrl)
                .lbrryPhonenumber(lbrryPhoneNumber)
                .bookCo(bookCo)
                .lonCo(lonCo)
                .londayCnt(lonDayCnt)
                .nonebookCo(noneBookCo)
                .pblictnCo(pblictnCo)
                .seatCo(seatCo)
                .rdnmAdr(rdnmAdr)
                .operinstitutionNm(operInstitutionNm)
                .weekdayOperCloseHhmm(weekdayOperCloseHhmm)
                .weekdayOperOpenHhmm(weekdayOperOpenHhmm)
                .satOperCloseHhmm(satOperCloseHhmm)
                .satOperOpenHhmm(satOperOpenHhmm)
                .holidayOperCloseHhmm(holidayOperCloseHhmm)
                .holidayOperOpenHhmm(holidayOperOpenHhmm)
                .referenceDate(referenceDate)
                .buldAr(buldAr)
                .plotAr(plotAr)
                .latitude(latitude)
                .longitude(longitude)
                .insttCode(insttCode)
                .insttNm(insttNm)
                .build();
    }

}
