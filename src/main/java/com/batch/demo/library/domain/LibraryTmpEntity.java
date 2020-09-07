package com.batch.demo.library.domain;

import com.batch.demo.library.domain.enums.SidoCodeFields;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CSV_TABLE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibraryTmpEntity {

    @Id
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
    @Column(name = "plot_ar")
    private String plotAr;                      /* 부지면적         */
    @Column(name = "buld_ar")
    private String buldAr;                      /* 건물면적         */
    private String homepageUrl;                 /* 홈페이지주소       */
    private String latitude;                /* 위도           */
    private String longitude;               /* 경도           */
    @Column(name = "reference_date")
    private String referenceDate;        /* 데이터기준일자      */
    @Column(name = "instt_code")
    private String insttCode;                   /* 제공기관코드       */
    @Column(name = "instt_nm")
    private String insttNm;                     /* 제공기관명        */

    public Sido toSido() {
        Integer code = 0;
        for(SidoCodeFields nm : SidoCodeFields.values())
            if(nm.getFieldNm().equals(ctprvnNm)) code = nm.getFieldCode();

        return Sido.builder()
                .ctprvnCode(code)
                .ctprvnNm(ctprvnNm)
                .build();
    }
}
