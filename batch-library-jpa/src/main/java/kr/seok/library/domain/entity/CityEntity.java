package kr.seok.library.domain.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Table(name = "TB_CITY")
/* cityNm 필드로 해당 CityEntity를 구분 */
@EqualsAndHashCode(callSuper = false, of = "cityNm")
@AttributeOverride(name = "id", column = @Column(name = "CITY_ID"))
public class CityEntity extends CommonEntity implements Serializable {

    @Column(name = "CITY_NM")
    private String cityNm;

    @Builder
    public CityEntity(String cityNm) {
        this.cityNm = cityNm;
    }
}
