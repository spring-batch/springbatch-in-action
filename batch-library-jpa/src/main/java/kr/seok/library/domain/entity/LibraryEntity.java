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

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "TB_LIBRARY")
@NoArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "LIBRARY_ID"))
public class LibraryEntity extends CommonEntity implements Serializable {

    @Column(name = "CITY_ID")
    private Long cityId;

    @Column(name = "COUNTRY_ID")
    private Long countryId;

    @Column(name = "LIBRARY_NM")
    private String libraryNm;

    @Column(name = "LIBRARY_TYPE")
    private String libraryType;

    @Builder
    public LibraryEntity(Long cityId, Long countryId, String libraryNm, String libraryType) {
        this.cityId = cityId;
        this.countryId = countryId;
        this.libraryNm = libraryNm;
        this.libraryType = libraryType;
    }
}
