package kr.seok.library.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity(name = "libraryTmpEntity")
@Table(name = "TB_TMP_LIBRARY")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = {"cityNm", "countryNm", "libraryNm", "libraryType"})
@AttributeOverride(name = "id", column = @Column(name = "TMP_ID"))
public class LibraryTmpEntity extends CommonEntity implements Serializable {

    /* 시도 명 */
    @Column(name = "CITY_NM")
    private String cityNm;

    /* 시군구 명 */
    @Column(name = "COUNTRY_NM")
    private String countryNm;

    /* 도서관 명 */
    @Column(name = "LIBRARY_NM")
    private String libraryNm;

    /* 도서관 유형 */
    @Column(name = "LIBRARY_TYPE")
    private String libraryType;

    @Builder
    public LibraryTmpEntity(String cityNm, String countryNm, String libraryNm, String libraryType) {
        this.cityNm = cityNm;
        this.countryNm = countryNm;
        this.libraryNm = libraryNm;
        this.libraryType = libraryType;
    }

    public CityEntity toCityEntity() {
        return CityEntity.builder()
                .cityNm(cityNm)
                .build();
    }
}
