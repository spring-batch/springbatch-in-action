package kr.seok.library.domain;

import lombok.*;

import java.util.Arrays;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FileDto {

    private String libNm;       /* 도서관 명 */
    private String sidoNm;      /* 시도 명 */
    private String signguNm;    /* 시군구 명 */
    private String libType;     /* 도서관 유형 */

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum FileFields {

        libNm("libNm", "LIBRARY_NM"),
        sidoNm("sidoNm", "SIDO_NM"),
        signguNM("signguNm", "SIGNGU_NM"),
        libType("libType", "LIBRARY_TYPE")
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
                .libNm(libNm)
                .libType(libType)
                .sidoNm(sidoNm)
                .signguNm(signguNm)
                .build();
    }

}
