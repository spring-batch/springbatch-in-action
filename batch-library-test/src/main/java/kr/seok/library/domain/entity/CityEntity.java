package kr.seok.library.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@Table(name = "TB_CITY")
@AttributeOverride(name = "id", column = @Column(name = "CITY_ID"))
public class CityEntity extends CommonEntity implements Serializable {

    @Column(name = "CITY_NM")
    private String cityNm;

    @Builder
    public CityEntity(String cityNm) {
        this.cityNm = cityNm;
    }
}
