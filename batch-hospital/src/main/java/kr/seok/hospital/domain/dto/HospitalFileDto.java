package kr.seok.hospital.domain.dto;

import kr.seok.hospital.domain.Hospital;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/* 엑셀 컬럼 순으로 정렬된 필드로 순서 주의 */
@Data
@Builder
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
        date = getDate()
                .substring(0, date.length() - 2)
                .replaceFirst("\\s", "T");
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
    public static HospitalFileDto getSplitData(String line) {
        String[] s = line
                .replaceAll("\"", "")
                .split(",");
        return HospitalFileDto.builder()
                .id(s[0])
                .addr(s[1])
                .hosCate(s[2])
                .hosCateNm(s[3])
                .fstAidMedicInsCd(s[4])
                .fstAidMedicInsNm(s[5])
                .edOperYn(s[6])
                .etc(s[7])
                .operDescDt(s[8])
                .simpleMap(s[9])

                .operNm(s[10])
                .phone1(s[11])
                .edPhone(s[12])
                .operHourMonC(s[13])
                .operHourTueC(s[14])
                .operHourWedC(s[15])
                .operHourThuC(s[16])
                .operHourFriC(s[17])
                .operHourSatC(s[18])
                .operHourSunC(s[19])

                .operHourHolC(s[20])
                .operHourMonS(s[21])
                .operHourTueS(s[22])
                .operHourWedS(s[23])
                .operHourThuS(s[24])
                .operHourFriS(s[25])
                .operHourSatS(s[26])
                .operHourSunS(s[27])
                .operHourHolS(s[28])
                .zipCode1(s[29])

                .zipCode2(s[30])
                .lat(Double.parseDouble(s[31]))
                .lon(Double.parseDouble(s[32]))
                .date(s[33])

                .build();
    }
}
