package kr.seok.batch.demo.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "TB_LBRRY_DETAIL")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LibraryDetailEntity {

    @Id
    @Column(name = "lbrry_code")
    private Integer lbrryCode;                     /* 도서관명         */
    @Column(name = "plot_ar")
    private BigDecimal plotAr;                      /* 부지면적         */
    @Column(name = "buld_ar")
    private BigDecimal buldAr;                      /* 건물면적         */
    private BigDecimal latitude;                    /* 위도           */
    private BigDecimal longitude;                   /* 경도           */
    @Column(name = "reference_date")
    private String referenceDate;               /* 데이터기준일자      */
    @Column(name = "instt_code")
    private String insttCode;                   /* 제공기관코드       */
    @Column(name = "instt_nm")
    private String insttNm;                     /* 제공기관명        */
}
