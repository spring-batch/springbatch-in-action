package com.batch.demo.library.domain;

import lombok.*;

import javax.persistence.*;
import java.util.Arrays;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_SIGNGU")
public class Signgu {

    @SuppressWarnings("NonAsciiCharacters")
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum SignguFields {

        시군구코드(0, "시군구코드", "signgu_code"),
        시군구명(1, "시군구명", "signgu_nm"),
        시도코드(2, "시도코드", "ctprvn_code"),
        읍면동코드(3, "읍면동코드", "eupmyeondong_code"),
        읍면동명(4, "읍면동명", "eupmyeondong_nm");

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "signgu_code")
    private Integer signguCode;

    @Column(name = "signgu_nm")
    private String signguNm;
    @Column(name = "ctprvn_code")
    private Integer ctprvnCode;
    @Column(name = "eupmyeondong_code")
    private Integer eupMyeonDongCode;
    @Column(name = "eupmyeondong_nm")
    private String eupMyeonDongNm;
}
