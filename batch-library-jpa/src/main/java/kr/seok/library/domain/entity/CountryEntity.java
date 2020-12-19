package kr.seok.library.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Table(name = "TB_COUNTRY")
/* cityNm, countryNm 필드로 해당 CountryEntity 를 구분 */
@EqualsAndHashCode(callSuper = false, of = {"cityEntity", "countryNm"})
@AttributeOverride(name = "id", column = @Column(name = "COUNTRY_ID"))
public class CountryEntity extends CommonEntity implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CITY_ID")
    private CityEntity cityEntity;

    @Column(name = "COUNTRY_NM")
    private String countryNm;

    @Builder
    public CountryEntity(CityEntity cityEntity, String countryNm) {
        this.cityEntity = cityEntity;
        this.countryNm = countryNm;
    }
}
