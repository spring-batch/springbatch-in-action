package com.batch.domain.batch;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "TB_LBRRY")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibraryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lbrry_code")
    private Integer lbrryCode;                     /* 도서관명         */

    @Column(name = "lbrry_nm")
    private String lbrryNm;                     /* 도서관명         */
    @Column(name = "ctprvn_code")
    private Integer ctprvnCode;                    /* 시도명          */
    @Column(name = "signgu_code")
    private Integer signguCode;                    /* 시군구명         */

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
    private String nonebookCo;                 /* 자료수(비도서)     */
    @Column(name = "lon_co")
    private String lonCo;                      /* 대출가능권수       */
    @Column(name = "londay_cnt")
    private String londayCnt;                  /* 대출가능일수       */
    @Column(name = "rdnm_adr")
    private String rdnmAdr;                     /* 소재지도로명주소     */
    @Column(name = "operinstitution_nm")
    private String operinstitutionNm;           /* 운영기관명        */
    @Column(name = "lbrry_phonenumber")
    private String lbrryPhonenumber;            /* 도서관전화번호      */
    private String homepageUrl;                 /* 홈페이지주소       */

    public LibraryTotalEntity toEntity(Sido sido, Signgu signgu, LibraryDetailEntity libraryDetailEntity) {
        return LibraryTotalEntity.builder()
                .lbrryCode(lbrryCode)

                .ctprvnCode(ctprvnCode)
                .ctprvnNm(sido.getCtprvnNm())
                .signguCode(signguCode)
                .signguNm(signgu.getSignguNm())

                .closeDay(closeDay)
                .weekdayOperCloseHhmm(weekdayOperCloseHhmm)
                .weekdayOperOpenHhmm(weekdayOperOpenHhmm)
                .satOperCloseHhmm(satOperCloseHhmm)
                .satOperOpenHhmm(satOperOpenHhmm)
                .holidayOperCloseHhmm(holidayOperCloseHhmm)
                .holidayOperOpenHhmm(holidayOperOpenHhmm)
                .homepageUrl(homepageUrl)
                .lbrryNm(lbrryNm)
                .lbrryPhonenumber(lbrryPhonenumber)
                .lbrrySe(lbrrySe)
                .lonCo(lonCo)
                .londayCnt(londayCnt)
                .pblictnCo(pblictnCo)
                .rdnmAdr(rdnmAdr)
                .seatCo(seatCo)
                .bookCo(bookCo)
                .nonebookCo(nonebookCo)
                .operinstitutionNm(operinstitutionNm)

                .referenceDate(libraryDetailEntity.getReferenceDate())
                .plotAr(libraryDetailEntity.getPlotAr())
                .buldAr(libraryDetailEntity.getBuldAr())
                .latitude(libraryDetailEntity.getLatitude())
                .longitude(libraryDetailEntity.getLongitude())

                .insttCode(libraryDetailEntity.getInsttCode())
                .insttNm(libraryDetailEntity.getInsttNm())
                .build();
    }
}
