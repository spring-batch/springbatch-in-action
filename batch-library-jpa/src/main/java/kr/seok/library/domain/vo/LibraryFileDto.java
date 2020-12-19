package kr.seok.library.domain.vo;

import kr.seok.library.domain.entity.LibraryTmpEntity;
import lombok.*;

import java.util.Arrays;

@Data
@NoArgsConstructor
public class LibraryFileDto {

    private String libraryNm;       /* 도서관 명 */
    private String cityNm;      /* 시도 명 */
    private String countryNm;    /* 시군구 명 */
    private String libraryType;     /* 도서관 유형 */

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum LibraryFileFields {

        libraryNm("libraryNm", "LIBRARY_NM"),
        cityNm("cityNm", "CITY_NM"),
        countryNM("countryNm", "COUNTRY_NM"),
        libraryType("libraryType", "LIBRARY_TYPE");

        private String fieldNm;
        private String dbNm;

        /* 필드명 리스트 조회 */
        public static String[] getFieldNms() {
            return Arrays.stream(LibraryFileFields.values())
                    .map(LibraryFileFields::getFieldNm)
                    .toArray(String[]::new);
        }

        /* DB명 리스트 조회 */
        public static String[] getDbNms() {
            return Arrays.stream(LibraryFileFields.values())
                    .map(LibraryFileFields::getDbNm)
                    .toArray(String[]::new);
        }
    }

    public LibraryTmpEntity toEntity() {
        return LibraryTmpEntity.builder()
                .cityNm(cityNm)
                .countryNm(countryNm)
                .libraryNm(libraryNm)
                .libraryType(libraryType)
                .build();
    }

}
