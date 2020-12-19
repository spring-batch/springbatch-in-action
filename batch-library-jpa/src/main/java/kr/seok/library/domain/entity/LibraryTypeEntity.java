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
@Table(name = "TB_LIBRARY_TYPE")
/* libraryType 필드로 해당 LibraryTypeEntity 를 구분 */
@EqualsAndHashCode(callSuper = false, of = "libraryType")
@AttributeOverride(name = "id", column = @Column(name = "LIBRARY_TYPE_ID"))
public class LibraryTypeEntity extends CommonEntity implements Serializable {

    @Column(name = "LIBRARY_TYPE")
    private String libraryType;

    @Builder
    public LibraryTypeEntity(String libraryType) {
        this.libraryType = libraryType;
    }
}
