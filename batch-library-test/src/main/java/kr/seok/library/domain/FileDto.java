package kr.seok.library.domain;

import lombok.*;

import java.util.Arrays;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileDto {

    private String libraryNm;       /* 도서관 명 */
    private String cityNm;      /* 시도 명 */
    private String countryNM;    /* 시군구 명 */
    private String libraryType;     /* 도서관 유형 */

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum FileFields {

        libraryNm("libraryNm", "LIBRARY_NM"),
        cityNm("cityNm", "CITY_NM"),
        countryNM("countryNm", "COUNTRY_NM"),
        libraryType("libraryType", "LIBRARY_TYPE")
        ;

        private String fieldNm;
        private String dbNm;

        /* 필드명 리스트 조회 */
        public static String[] getFieldNms() {
            return Arrays.stream(FileFields.values())
                    .map(FileFields::getFieldNm)
                    .toArray(String[]::new);
        }

        /* DB명 리스트 조회 */
        public static String[] getDbNms() {
            return Arrays.stream(FileFields.values())
                    .map(FileFields::getDbNm)
                    .toArray(String[]::new);
        }
    }

    public TmpEntity toEntity() {
        return TmpEntity.builder()
                .cityNm(cityNm)
                .countryNm(countryNM)
                .libraryNm(libraryNm)
                .libraryType(libraryType)
                .build();
    }

}