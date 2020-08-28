package com.batch.domain.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Signgu {

    @SuppressWarnings("NonAsciiCharacters")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum SignguFields {

        시군구코드(0, "시군구코드", "signgu_cd"),
        시군구명(1, "시군구명", "signgu_nm"),
        시도코드(2, "시도코드", "sidoCd");

        private int fieldIdx;
        private String fieldNm;
        private String dbFieldNm;

        public static String[] getFieldNmArrays() {
            return Arrays.stream(SignguFields.values())
                    .map(SignguFields::getFieldNm)
                    .toArray(String[]::new);
        }
        public static String[] getDbFieldNmArrays() {
            return Arrays.stream(SignguFields.values())
                    .map(SignguFields::getDbFieldNm)
                    .toArray(String[]::new);
        }
    }

    private Integer signguCd;
    private String signguNm;
    private Integer sidoCd;

}
