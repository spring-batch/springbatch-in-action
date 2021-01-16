package kr.seok.hospital.domain.dto;

import kr.seok.hospital.domain.Hospital;
import lombok.Data;

import java.time.LocalDateTime;

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

    private LocalDateTime date;

    public Hospital toEntity() {
        return Hospital.builder()
                .id(getId())
                .hosCate(getHosCate())
                .date(getDate())
                .edOperYn(getEdOperYn())
                .etc(getEtc())
                .edPhone(getEdPhone())
                .fstAidMedicInsCd(getFstAidMedicInsCd())
                .hosCateNm(getHosCateNm())
                .lat(getLat())
                .lon(getLon())

                .fstAidMedicInsNm(getFstAidMedicInsNm())
                .operDescDt(getOperDescDt())
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
                .addr(getAddr())
                .operNm(getOperNm())

                .phone1(getPhone1())
                .simpleMap(getSimpleMap())
                .zipCode1(getZipCode1())
                .zipCode2(getZipCode2())

                .build();
    }
}
