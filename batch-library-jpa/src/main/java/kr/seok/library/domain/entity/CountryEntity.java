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

@Entity
@NoArgsConstructor
@Table(name = "TB_COUNTRY")
/* cityNm, countryNm 필드로 해당 CountryEntity 를 구분 */
@EqualsAndHashCode(callSuper = false, of = {"cityNm", "countryNm"})
public class CountryEntity extends CommonEntity implements Serializable {

    @Column(name = "CITY_NM")
    private String cityNm;

    @Column(name = "COUNTRY_NM")
    private String countryNm;

    @Builder
    public CountryEntity(String cityNm, String countryNm) {
        this.cityNm = cityNm;
        this.countryNm = countryNm;
    }
}
