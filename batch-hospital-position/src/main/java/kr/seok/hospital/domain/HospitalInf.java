package kr.seok.hospital.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "TB_HOSPITAL_INF")
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HospitalInf extends BaseTimeEntity {

    @Id
    @Column(name = "ORG_ID", unique = true)
    private String id;
    @Column(name = "HOS_CATE") /* 병원분류 */
    private String hosCate;
    @Column(name = "HOS_CATE_NM") /* 병원분류명 */
    private String hosCateNm;
    @Column(name = "FST_AID_MEDIC_INS_CD") /* 응급의료기관코드 */
    private String fstAidMedicInsCd;
    @Column(name = "FST_AID_MEDIC_INS_NM") /* 응급의료기관코드명 */
    private String fstAidMedicInsNm;
    @Column(name = "ED_OPER_YN") /* 응급실운영여부(1/2) */
    private String edOperYn;
    @Column(name = "ETC") /* 비고 */
    private String etc;
    @Column(name = "OPER_DESC_DT") /* 기관설명상세 */
    private String operDescDt;

    @Column(name = "OPER_NM") /* 기관명 */
    private String operNm;
    @Column(name = "PHONE1") /* 대표전화1 */
    private String phone1;
    @Column(name = "ED_PHONE") /* 응급실전화 */
    private String edPhone;

    @Column(name = "ADDRESS") /* 주소 */
    private String addr;

    @Column(name = "ZIP_CODE1") /* 우편번호1 */
    private String zipCode1;
    @Column(name = "ZIP_CODE2") /* 우편번호2 */
    private String zipCode2;

    @Builder
    public HospitalInf(String id, String hosCate, String hosCateNm, String fstAidMedicInsCd, String fstAidMedicInsNm, String edOperYn, String etc, String operDescDt, String operNm, String phone1, String edPhone, String addr, String zipCode1, String zipCode2) {
        this.id = id;
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
        this.addr = addr;
        this.zipCode1 = zipCode1;
        this.zipCode2 = zipCode2;
    }
}
