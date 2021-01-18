package kr.seok.hospital.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "TB_HOSPITAL_DTT")
@EqualsAndHashCode(of = {"id"}, callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalDtt extends BaseTimeEntity {

    @Id
    @Column(name = "ORG_ID", unique = true)
    private String id;
    @Column(name = "OPER_HOUR_MON_C") /* 진료시간(월요일)C */
    private String operHourMonC;
    @Column(name = "OPER_HOUR_TUE_C") /* 진료시간(화요일)C */
    private String operHourTueC;
    @Column(name = "OPER_HOUR_WED_C") /* 진료시간(수요일)C */
    private String operHourWedC;
    @Column(name = "OPER_HOUR_THU_C") /* 진료시간(목요일)C */
    private String operHourThuC;
    @Column(name = "OPER_HOUR_FRI_C") /* 진료시간(금요일)C */
    private String operHourFriC;
    @Column(name = "OPER_HOUR_SAT_C") /* 진료시간(토요일)C */
    private String operHourSatC;
    @Column(name = "OPER_HOUR_SUN_C") /* 진료시간(일요일)C */
    private String operHourSunC;
    @Column(name = "OPER_HOUR_HOL_C") /* 진료시간(공휴일)C */
    private String operHourHolC;

    @Column(name = "OPER_HOUR_MON_S") /* 진료시간(월요일)S */
    private String operHourMonS;
    @Column(name = "OPER_HOUR_TUE_S") /* 진료시간(화요일)S */
    private String operHourTueS;
    @Column(name = "OPER_HOUR_WED_S") /* 진료시간(수요일)S */
    private String operHourWedS;
    @Column(name = "OPER_HOUR_THU_S") /* 진료시간(목요일)S */
    private String operHourThuS;
    @Column(name = "OPER_HOUR_FRI_S") /* 진료시간(금요일)S */
    private String operHourFriS;
    @Column(name = "OPER_HOUR_SAT_S") /* 진료시간(토요일)S */
    private String operHourSatS;
    @Column(name = "OPER_HOUR_SUN_S") /* 진료시간(일요일)S */
    private String operHourSunS;
    @Column(name = "OPER_HOUR_HOL_S") /* 진료시간(공휴일)S */
    private String operHourHolS;

    @Builder
    public HospitalDtt(String id, String operHourMonC, String operHourTueC, String operHourWedC, String operHourThuC, String operHourFriC, String operHourSatC, String operHourSunC, String operHourHolC, String operHourMonS, String operHourTueS, String operHourWedS, String operHourThuS, String operHourFriS, String operHourSatS, String operHourSunS, String operHourHolS) {
        this.id = id;
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
    }
}
