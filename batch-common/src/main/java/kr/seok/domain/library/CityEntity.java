package kr.seok.domain.library;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "TB_CITY")
public class CityEntity {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CITY_ID")
    private Long cityId;

    @Column(name = "SIDO_NM")
    private String sidoNm;

    @Builder
    public CityEntity(Long cityId, String sidoNm) {
        this.cityId = cityId;
        this.sidoNm = sidoNm;
    }
}
