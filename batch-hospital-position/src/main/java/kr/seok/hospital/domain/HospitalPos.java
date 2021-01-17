package kr.seok.hospital.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "TB_HOSPITAL_POS")
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "ORG_ID", unique = true))
public class HospitalPos extends BaseTimeEntity {

    @Column(name = "SIMPLE_MAP") /* 간이약도 */
    private String simpleMap;

    @Column(name = "LONGITUDE") /* 병원경도 */
    private Double lon;
    @Column(name = "LATITUDE") /* 병원위도 */
    private Double lat;
}
