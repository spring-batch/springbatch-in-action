package kr.seok.library.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
    public LibraryEntity(String libraryNm, String libraryType) {
        this.libraryNm = libraryNm;
        this.libraryType = libraryType;
    }
}
