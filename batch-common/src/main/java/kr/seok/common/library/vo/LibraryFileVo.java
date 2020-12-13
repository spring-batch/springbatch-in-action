package kr.seok.common.library.vo;

import kr.seok.common.entity.EntityField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibraryFileVo {

    @EntityField(fieldName = "libraryNm", columnName = "LIBRARY_NM")
    private String libraryNm;

    @EntityField(fieldName = "cityNm", columnName = "CITY_NM")
    private String cityNm;

    @EntityField(fieldName = "countryNm", columnName = "COUNTRY_NM")
    private String countryNm;

    @EntityField(fieldName = "libraryType", columnName = "LIBRARY_TYPE")
    private String libraryType;

}
