package com.batch.mapper;

import com.batch.domain.oracle.LibraryEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * 도서관 데이터 CSV -> Memory 로 올리기 위한 Mapper
 */
@Slf4j
public class LibraryFileToDBMapper implements FieldSetMapper<LibraryEntity> {

    @Override
    public LibraryEntity mapFieldSet(FieldSet fieldSet) {
        if(fieldSet == null) {
            return null;
        }
        LibraryEntity library = new LibraryEntity();
        library.setLbrryNm(fieldSet.readString("도서관명"));
        library.setCtprvnNm(fieldSet.readString("시도명"));
        library.setSignguNm(fieldSet.readString("시군구명"));
        library.setLbrrySe(fieldSet.readString("도서관유형"));
        library.setCloseDay(fieldSet.readString("휴관일"));
        library.setWeekdayOperOpenHhmm(fieldSet.readString("평일운영시작시각"));
        library.setWeekdayOperCloseHhmm(fieldSet.readString("평일운영종료시각"));
        library.setSatOperOpenHhmm(fieldSet.readString("토요일운영시작시각"));
        library.setSatOperCloseHhmm(fieldSet.readString("토요일운영종료시각"));
        library.setHolidayOperOpenHhmm(fieldSet.readString("공휴일운영시작시각"));
        library.setHolidayOperCloseHhmm(fieldSet.readString("공휴일운영종료시각"));
        library.setSeatCo(fieldSet.readString("열람좌석수"));
        library.setBookCo(fieldSet.readString("자료수(도서)"));
        library.setPblictnCo(fieldSet.readString("자료수(연속간행물)"));
        library.setNoneBookCo(fieldSet.readString("자료수(비도서)"));
        library.setLonCo(fieldSet.readString("대출가능권수"));
        library.setLonDaycnt(fieldSet.readString("대출가능일수"));
        library.setRdnmadr(fieldSet.readString("소재지도로명주소"));
        library.setOperInstitutionNm(fieldSet.readString("운영기관명"));
        library.setLbrryPhoneNumber(fieldSet.readString("도서관전화번호"));
        library.setPlotAr(fieldSet.readString("부지면적"));
        library.setBuldAr(fieldSet.readString("건물면적"));
        library.setHomepageUrl(fieldSet.readString("홈페이지주소"));
        library.setLatitude(fieldSet.readString("위도"));
        library.setLongitude(fieldSet.readString("경도"));
        library.setReferenceDate(fieldSet.readString("데이터기준일자"));
        library.setInsttCode(fieldSet.readString("제공기관코드"));
        library.setInsttNm(fieldSet.readString("제공기관명"));

        return library;
    }
}
