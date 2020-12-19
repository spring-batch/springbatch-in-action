package kr.seok.library.domain.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "libraryEntity")
@NoArgsConstructor
@Table(name = "TB_LIBRARY")
/* libraryNm 필드와 cityEntity, countryEntity, libraryTypeEntity 엔티티로 LibraryEntity 를 구분 */
@EqualsAndHashCode(callSuper = false, of = {"libraryNm", "cityEntity", "countryEntity", "libraryTypeEntity"})
@AttributeOverride(name = "id", column = @Column(name = "LIBRARY_ID"))
public class LibraryEntity extends CommonEntity implements Serializable {

    @Column(name = "LIBRARY_NM")
    private String libraryNm;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "CITY_ID")
    private CityEntity cityEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "COUNTRY_ID")
    private CountryEntity countryEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "LIBRARY_TYPE_ID")
    private LibraryTypeEntity libraryTypeEntity;

    @Builder
    public LibraryEntity(String libraryNm, CityEntity cityEntity, CountryEntity countryEntity, LibraryTypeEntity libraryTypeEntity) {
        this.libraryNm = libraryNm;
        this.cityEntity = cityEntity;
        this.countryEntity = countryEntity;
        this.libraryTypeEntity = libraryTypeEntity;
    }
}
