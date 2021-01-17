package kr.seok.hospital.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "TB_HOSPITAL_INF")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "ORG_ID", unique = true))
public class HospitalInf extends BaseTimeEntity {

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

}
