package com.batch.demo.library.domain.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@SuppressWarnings("NonAsciiCharacters")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum SidoCodeFields {

    전국	(00, "전국"),
    서울특별시	(11, "서울특별시"),
    부산광역시	(21, "부산광역시"),
    대구광역시	(22, "대구광역시"),
    인천광역시	(23, "인천광역시"),
    광주광역시	(24, "광주광역시"),
    대전광역시	(25, "대전광역시"),
    울산광역시	(26, "울산광역시"),
    세종특별자치시	(29, "세종특별자치시"),
    경기도	(31, "경기도"),
    강원도	(32, "강원도"),
    충청북도	(33, "충청북도"),
    충청남도	(34, "충청남도"),
    전라북도	(35, "전라북도"),
    전라남도	(36, "전라남도"),
    경상북도	(37, "경상북도"),
    경상남도	(38, "경상남도"),
    제주도	(39, "제주도"),
    제주특별자치도	(39, "제주특별자치도");

    private Integer fieldCode;
    private String fieldNm;

    public static Integer[] getFieldCodeArrays() {
        return Arrays.stream(SidoCodeFields.values())
                .map(SidoCodeFields::getFieldCode)
                .toArray(Integer[]::new);
    }
    public static String[] getFieldNmArrays() {
        return Arrays.stream(SidoCodeFields.values())
                .map(SidoCodeFields::getFieldNm)
                .toArray(String[]::new);
    }
}