package com.batch.mapper;

import com.batch.domain.Record;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class LibraryMapper implements FieldSetMapper<Record> {

    @Override
    public Record mapFieldSet(FieldSet fieldSet) throws BindException {
        if(fieldSet == null) {
            return null;
        }
        Record record = new Record();
        record.set도서관명(fieldSet.readString("도서관명"));
        record.set시도명(fieldSet.readString("시도명"));
        record.set시군구명(fieldSet.readString("시군구명"));
        record.set도서관유형(fieldSet.readString("도서관유형"));
        record.set휴관일(fieldSet.readString("휴관일"));
        record.set평일운영시작시각(fieldSet.readString("평일운영시작시각"));
        record.set평일운영종료시각(fieldSet.readString("평일운영종료시각"));
        record.set토요일운영시작시각(fieldSet.readString("토요일운영시작시각"));
        record.set토요일운영종료시각(fieldSet.readString("토요일운영종료시각"));
        record.set공휴일운영시작시각(fieldSet.readString("공휴일운영시작시각"));
        record.set공휴일운영종료시각(fieldSet.readString("공휴일운영종료시각"));
        record.set열람좌석수(fieldSet.readString("열람좌석수"));
        record.set자료수_도서(fieldSet.readString("자료수(도서)"));
        record.set자료수_연속간행물(fieldSet.readString("자료수(연속간행물)"));
        record.set자료수_비도서(fieldSet.readString("자료수(비도서)"));
        record.set대출가능권수(fieldSet.readString("대출가능권수"));
        record.set대출가능일수(fieldSet.readString("대출가능일수"));
        record.set소재지도로명주소(fieldSet.readString("소재지도로명주소"));
        record.set운영기관명(fieldSet.readString("운영기관명"));
        record.set도서관전화번호(fieldSet.readString("도서관전화번호"));
        record.set부지면적(fieldSet.readString("부지면적"));
        record.set건물면적(fieldSet.readString("건물면적"));
        record.set홈페이지주소(fieldSet.readString("홈페이지주소"));
        record.set위도(fieldSet.readString("위도"));
        record.set경도(fieldSet.readString("경도"));
        record.set데이터기준일자(fieldSet.readString("데이터기준일자"));
        record.set제공기관코드(fieldSet.readString("제공기관코드"));
        record.set제공기관명(fieldSet.readString("제공기관명"));

        return record;
    }
}
