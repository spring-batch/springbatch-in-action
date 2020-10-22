package kr.seok.library.domain.entity;

import lombok.*;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@Table(name = "TB_COUNTRY")
@AttributeOverride(name = "id", column = @Column(name = "COUNTRY_ID"))
public class CountryEntity extends CommonEntity implements Serializable {

    @Column(name = "CITY_ID")
    private Long cityId;

    @Column(name = "COUNTRY_NM")
    private String countryNm;

    @Builder
    public CountryEntity(Long cityId, String countryNm) {
        this.cityId = cityId;
        this.countryNm = countryNm;
    }
}
