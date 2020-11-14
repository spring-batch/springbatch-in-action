package kr.seok.library.domain;


import kr.seok.common.excel.annotation.ExcelColumn;
import kr.seok.library.domain.entity.TmpEntity;
import lombok.Data;

@Data
public class FileCSVDto {

    @ExcelColumn(headerName = "도서관 명", dbName = "LIBRARY_NM")
    private String libraryNm;
    @ExcelColumn(headerName = "시도군", dbName = "CITY_NM")
    private String cityNm;
    @ExcelColumn(headerName = "동읍면", dbName = "COUNTRY_NM")
    private String countryNm;
    @ExcelColumn(headerName = "도서관 유형", dbName = "LIBRARY_TYPE")
    private String libraryType;

    public TmpEntity toEntity() {
        return TmpEntity.builder()
                .cityNm(cityNm)
                .countryNm(countryNm)
                .libraryNm(libraryNm)
                .libraryType(libraryType)
                .build();
    }

}
