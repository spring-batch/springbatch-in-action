package kr.seok.hospital.domain.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalEsEntity {

    private String id;

    private String orgId;

    private String addr;
    private String hosCate;
    private String hosCateNm;

    private String fstAidMedicInsCd;
    private String fstAidMedicInsNm;

    private String edOperYn;

    private String etc;

    private String operDescDt;

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

    private LocalDateTime date;

    @Builder
    @QueryProjection
    public HospitalEsEntity(String id, String orgId, String addr, String hosCate, String hosCateNm, String fstAidMedicInsCd, String fstAidMedicInsNm, String edOperYn, String etc, String operDescDt, String operNm, String phone1, String edPhone, String operHourMonC, String operHourTueC, String operHourWedC, String operHourThuC, String operHourFriC, String operHourSatC, String operHourSunC, String operHourHolC, String operHourMonS, String operHourTueS, String operHourWedS, String operHourThuS, String operHourFriS, String operHourSatS, String operHourSunS, String operHourHolS, String zipCode1, String zipCode2, LocalDateTime date) {
        this.id = id;
        this.orgId = orgId;
        this.addr = addr;
        this.hosCate = hosCate;
        this.hosCateNm = hosCateNm;
        this.fstAidMedicInsCd = fstAidMedicInsCd;
        this.fstAidMedicInsNm = fstAidMedicInsNm;
        this.edOperYn = edOperYn;
        this.etc = etc;
        this.operDescDt = operDescDt;
        this.operNm = operNm;
        this.phone1 = phone1;
        this.edPhone = edPhone;
        this.operHourMonC = operHourMonC;
        this.operHourTueC = operHourTueC;
        this.operHourWedC = operHourWedC;
        this.operHourThuC = operHourThuC;
        this.operHourFriC = operHourFriC;
        this.operHourSatC = operHourSatC;
        this.operHourSunC = operHourSunC;
        this.operHourHolC = operHourHolC;
        this.operHourMonS = operHourMonS;
        this.operHourTueS = operHourTueS;
        this.operHourWedS = operHourWedS;
        this.operHourThuS = operHourThuS;
        this.operHourFriS = operHourFriS;
        this.operHourSatS = operHourSatS;
        this.operHourSunS = operHourSunS;
        this.operHourHolS = operHourHolS;
        this.zipCode1 = zipCode1;
        this.zipCode2 = zipCode2;
        this.date = date;
    }
}
