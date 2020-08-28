package com.batch.domain.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Region {

    @SuppressWarnings("NonAsciiCharacters")
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum RegionField {

        대분류(0, "대분류", "sidoCd", "SIDO_CD"),
        시도(1, "시도", "sidoNm", "SIDO_NM"),
        중분류(2, "중분류", "signguCd", "SIGNGU_CD"),
        시군구(3, "시군구", "signguNm", "SIGNGU_NM"),
        소분류(4, "소분류", "eupmyeonDongCd", "EUPMYEON_DONG_CD"),
        읍면동(5, "읍면동", "eupmyeonDongNm", "EUPMYEON_DONG_NM");

        private int fieldIdx;
        private String fieldNm;
        private String varFieldNm;
        private String dbFieldNm;

        /**
         * @return CSV에서 LineMapper로 읽는 경우 변수 Arrays
         */
        public static String[] getFieldNmArrays() {
            return Arrays.stream(RegionField.values())
                    .map(RegionField::getFieldNm)
                    .toArray(String[]::new);
        }

        /**
         * @return Java에서 사용할 변수 Arrays
         */
        public static String[] getVarFieldNmArrays() {
            return Arrays.stream(RegionField.values())
                    .map(RegionField::getVarFieldNm)
                    .toArray(String[]::new);
        }

        /**
         * @return DB에서 사용할 변수 Arrays
         */
        public static String[] getDbFieldNmArrays() {
            return Arrays.stream(RegionField.values())
                    .map(RegionField::getDbFieldNm)
                    .toArray(String[]::new);
        }
    }

    private Integer sidoCd;
    private String sidoNm;
    private Integer signguCd;
    private String signguNm;
    private Integer eupmyeonDongCd;
    private String eupmyeonDongNm;

}