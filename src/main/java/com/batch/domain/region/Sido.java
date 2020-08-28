package com.batch.domain.region;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sido {

    @Getter
    @AllArgsConstructor
    public enum SidoFields {
        순서(0, "순서", "id"),
        조직도시도코드(1, "조직도시도코드", "ctprvnCd"),
        조직도시도명(2, "조직도시도명", "ctprvnNm");

        private int fieldIdx;
        private String fieldNm;
        private String dbFieldNm;

        public static String[] getFieldNmArrays() {
            return Arrays.stream(SidoFields.values())
                    .map(SidoFields::getFieldNm)
                    .toArray(String[]::new);
        }
        public static String[] getDbFieldNmArrays() {
            return Arrays.stream(SidoFields.values())
                    .map(SidoFields::getDbFieldNm)
                    .toArray(String[]::new);
        }
    }
    private Integer orderId;
    private Integer ctprvnCd;
    private String ctprvnNm;
}
