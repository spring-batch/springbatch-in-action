package com.batch.mapper;

import com.batch.domain.oracle.LibraryTmp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

/**
 * 도서관 데이터 CSV -> Memory 로 올리기 위한 Mapper
 */
@Slf4j
public class LibraryFileToDBMapper implements FieldSetMapper<LibraryTmp> {

    @Override
    public LibraryTmp mapFieldSet(FieldSet fieldSet) {
        if(fieldSet == null) {
            return null;
        }
        LibraryTmp libraryTmp = new LibraryTmp();
        libraryTmp.setLbrryNm(fieldSet.readString("도서관명"));
        libraryTmp.setCtprvnNm(fieldSet.readString("시도명"));
        libraryTmp.setSignguNm(fieldSet.readString("시군구명"));
        libraryTmp.setLbrrySe(fieldSet.readString("도서관유형"));
        libraryTmp.setCloseDay(fieldSet.readString("휴관일"));
        libraryTmp.setWeekdayOperOpenHhmm(fieldSet.readString("평일운영시작시각"));
        libraryTmp.setWeekdayOperCloseHhmm(fieldSet.readString("평일운영종료시각"));
        libraryTmp.setSatOperOpenHhmm(fieldSet.readString("토요일운영시작시각"));
        libraryTmp.setSatOperCloseHhmm(fieldSet.readString("토요일운영종료시각"));
        libraryTmp.setHolidayOperOpenHhmm(fieldSet.readString("공휴일운영시작시각"));
        libraryTmp.setHolidayOperCloseHhmm(fieldSet.readString("공휴일운영종료시각"));
        libraryTmp.setSeatCo(fieldSet.readString("열람좌석수"));
        libraryTmp.setBookCo(fieldSet.readString("자료수(도서)"));
        libraryTmp.setPblictnCo(fieldSet.readString("자료수(연속간행물)"));
        libraryTmp.setNoneBookCo(fieldSet.readString("자료수(비도서)"));
        libraryTmp.setLonCo(fieldSet.readString("대출가능권수"));
        libraryTmp.setLonDayCnt(fieldSet.readString("대출가능일수"));
        libraryTmp.setRdnmAdr(fieldSet.readString("소재지도로명주소"));
        libraryTmp.setOperInstitutionNm(fieldSet.readString("운영기관명"));
        libraryTmp.setLbrryPhoneNumber(fieldSet.readString("도서관전화번호"));
        libraryTmp.setPlotAr(fieldSet.readString("부지면적"));
        libraryTmp.setBuldAr(fieldSet.readString("건물면적"));
        libraryTmp.setHomepageUrl(fieldSet.readString("홈페이지주소"));
        libraryTmp.setLatitude(fieldSet.readString("위도"));
        libraryTmp.setLongitude(fieldSet.readString("경도"));
        libraryTmp.setReferenceDate(fieldSet.readString("데이터기준일자"));
        libraryTmp.setInsttCode(fieldSet.readString("제공기관코드"));
        libraryTmp.setInsttNm(fieldSet.readString("제공기관명"));

        return libraryTmp;
    }
}
