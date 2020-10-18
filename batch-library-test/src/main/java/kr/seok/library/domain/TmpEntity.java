package kr.seok.library.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_TMP_LIBRARY")
public class TmpEntity {

    /* 테이블 */
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long tmpId;

    /* 도서관 명 */
    @Column(name = "LIBRARY_NM")
    private String libNm;

    /* 시도 명 */
    @Column(name = "SIDO_NM")
    private String sidoNm;

    /* 시군구 명 */
    @Column(name = "SIGNGU_NM")
    private String signguNm;

    /* 도서관 유형 */
    @Column(name = "LIBRARY_TYPE")
    private String libType;

    @Builder
    public TmpEntity(String libNm, String sidoNm, String signguNm, String libType) {
        this.libNm = libNm;
        this.sidoNm = sidoNm;
        this.signguNm = signguNm;
        this.libType = libType;
    }
}
