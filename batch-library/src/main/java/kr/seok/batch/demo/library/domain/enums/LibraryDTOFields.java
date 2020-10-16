package kr.seok.batch.demo.library.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

@SuppressWarnings("NonAsciiCharacters")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum LibraryDTOFields {
    도서관명(0, "도서관명"),
    시도명(1, "시도명"),
    시군구명(2, "시군구명"),
    도서관유형(3, "도서관유형"),
    휴관일(4, "휴관일"),
    평일운영시작시각(5, "평일운영시작시각"),
    평일운영종료시각(6, "평일운영종료시각"),
    토요일운영시작시각(7, "토요일운영시작시각"),
    토요일운영종료시각(8, "토요일운영종료시각"),
    공휴일운영시작시각(9, "공휴일운영시작시각"),
    공휴일운영종료시각(10, "공휴일운영종료시각"),
    열람좌석수(11, "열람좌석수"),
    자료수_도서(12, "자료수(도서)"),
    자료수_연속간행물(13, "자료수(연속간행물)"),
    자료수_비도서(14, "자료수(비도서)"),
    대출가능권수(15, "대출가능권수"),
    대출가능일수(16, "대출가능일수"),
    소재지도로명주소(17, "소재지도로명주소"),
    운영기관명(18, "운영기관명"),
    도서관전화번호(19, "도서관전화번호"),
    부지면적(20, "부지면적"),
    건물면적(21, "건물면적"),
    홈페이지주소(22, "홈페이지주소"),
    위도(23, "위도"),
    경도(24, "경도"),
    데이터기준일자(25, "데이터기준일자"),
    제공기관코드(26, "제공기관코드"),
    제공기관명(27, "제공기관명");

    private int columnIdx;
    private String fieldsNm;

    public static String[] getFieldNmArrays() {
        return Arrays.stream(LibraryDTOFields.values())
                .map(LibraryDTOFields::getFieldsNm)
                .toArray(String[]::new);
    }
}
