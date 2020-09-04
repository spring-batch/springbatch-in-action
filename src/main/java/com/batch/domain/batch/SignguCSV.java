package com.batch.domain.batch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignguCSV {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum SignguFields {
        largeClass(0, "largeClass", "LARGE_CLASS"),
        sidoNm(1, "sidoNm", "SIDO_NM"),
        midClass(2, "midClass", "MID_CLASS"),
        signguNm(3, "signguNm", "SIGNGU_NM"),
        smallClass(4, "smallClass", "SMALL_CLASS"),
        eupMyeonDongNm(5, "eupMyeonDongNm", "EUPMYEONDONG_NM"),
        enSidoNm(6, "enSidoNm", "EN_SIDO_NM"),
        chiSidoNm(7, "chiSidoNm", "CHI_SIDO_NM");

        private Integer fieldIdx;
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
    private Integer largeClass;
    private String sidoNm;
    private Integer midClass;
    private String signguNm;
    private Integer smallClass;
    private String eupMyeonDongNm;
    private String enSidoNm;
    private String chiSidoNm;

    public Signgu toEntity() {
        return Signgu.builder()
                .ctprvnCd(largeClass)
                .signguCd(smallClass)
                .signguNm(signguNm + " " + eupMyeonDongNm)
                .build();
    }
}
