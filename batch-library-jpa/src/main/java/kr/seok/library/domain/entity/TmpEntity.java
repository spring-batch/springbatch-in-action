package kr.seok.library.domain.entity;

import kr.seok.common.excel.annotation.DefaultBodyStyle;
import kr.seok.common.excel.annotation.DefaultHeaderStyle;
import kr.seok.common.excel.annotation.ExcelColumn;
import kr.seok.common.excel.annotation.ExcelColumnStyle;
import kr.seok.common.excel.style.DefaultExcelCellStyle;
import lombok.*;

import javax.persistence.*;
import java.util.Arrays;

@Entity(name = "TMP_ENTITY")
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_TMP_LIBRARY")
@DefaultHeaderStyle(
        style = @ExcelColumnStyle(
                excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "GREY_HEADER")
)
@DefaultBodyStyle(
        style = @ExcelColumnStyle(
                excelCellStyleClass = DefaultExcelCellStyle.class, enumName = "BODY")
)
public class TmpEntity {

    /* 테이블 */
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TMP_ID")
    private Long tmpId;

    /* 시도 명 */
    @Column(name = "CITY_NM")
    @ExcelColumn(headerName = "시도군")
    private String cityNm;

    /* 시군구 명 */
    @Column(name = "COUNTRY_NM")
    @ExcelColumn(headerName = "동읍면")
    private String countryNm;

    /* 도서관 명 */
    @Column(name = "LIBRARY_NM")
    @ExcelColumn(headerName = "도서관 명")
    private String libraryNm;

    /* 도서관 유형 */
    @Column(name = "LIBRARY_TYPE")
    @ExcelColumn(headerName = "도서관 유형")
    private String libraryType;

    @Getter
    @AllArgsConstructor
    public enum TmpFields {

        tmpId("TMP_ID"),
        cityNm("CITY_NM"),
        countryNm("COUNTRY_NM"),
        libraryNm("LIBRARY_NM"),
        libraryType("LIBRARY_TYPE");

        private String field;

        public static String[] getFields() {
            return Arrays.stream(TmpFields.values())
                    .map(TmpFields::getField)
                    .toArray(String[]::new);
        }
    }
}
