package kr.seok.hospital.domain.dto;

import kr.seok.hospital.domain.Hospital;
import lombok.Data;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Data
public class HospitalFileDto {
    private String id;
    private String addr;
    private String hosCate;
    private String hosCateNm;
    private String fstAidMedicInsCd;
    private String fstAidMedicInsNm;
    private String edOperYn;
    private String etc;
    private String operDescDt;
    private String simpleMap;
    private String operNm;
    private String phone1;
    private String edPhone;

    private String operHourMonC;
    private String operHourTueC;
    private String operHourWedC;
    private String operHourThuC;
    private String operHourFriC;
    private String operHourSatC;
    private String operHourSunC;
    private String operHourHolC;

    private String operHourMonS;
    private String operHourTueS;
    private String operHourWedS;
    private String operHourThuS;
    private String operHourFriS;
    private String operHourSatS;
    private String operHourSunS;
    private String operHourHolS;

    private String zipCode1;
    private String zipCode2;

    private Double lon;
    private Double lat;

    private String date;

    public Hospital toEntity() {
        date = getDate().substring(0, date.length() - 2).replaceFirst("\\s", "T");
        return Hospital.builder()
                .id(getId())
                .addr(getAddr())
                .hosCate(getHosCate())
                .hosCateNm(getHosCateNm())
                .edOperYn(getEdOperYn())
                .etc(getEtc())
                .phone1(getPhone1())
                .edPhone(getEdPhone())
                .date(LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME))
                .operNm(getOperNm())

                .operDescDt(getOperDescDt())
                .fstAidMedicInsCd(getFstAidMedicInsCd())
                .fstAidMedicInsNm(getFstAidMedicInsNm())
                .operHourFriC(getOperHourFriC())
                .operHourFriS(getOperHourFriS())
                .operHourHolC(getOperHourHolC())
                .operHourHolS(getOperHourHolS())
                .operHourMonC(getOperHourMonC())
                .operHourMonS(getOperHourMonS())
                .operHourSatC(getOperHourSatC())

                .operHourSatS(getOperHourSatS())
                .operHourSunC(getOperHourSunC())
                .operHourSunS(getOperHourSunS())
                .operHourThuC(getOperHourThuC())
                .operHourThuS(getOperHourThuS())
                .operHourTueC(getOperHourTueC())
                .operHourTueS(getOperHourTueS())
                .operHourWedC(getOperHourWedC())
                .operHourWedS(getOperHourWedS())
                .simpleMap(getSimpleMap())

                .zipCode1(getZipCode1())
                .zipCode2(getZipCode2())
                .lat(getLat())
                .lon(getLon())

                .build();
    }

    /* 엑셀에서 가져올 필드 개수 만큼만 추가해야 함 */
    public static String[] getFields() {
        Field[] fields = HospitalFileDto.class.getDeclaredFields();
        return Arrays.stream(fields)
                .map(Field::getName)
                .toArray(String[]::new);
    }
}
