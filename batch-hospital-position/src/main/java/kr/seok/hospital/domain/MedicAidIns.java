package kr.seok.hospital.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "TB_MEDIC_AID_INS")
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "aid_medic_ins_nm"})
public class MedicAidIns extends BaseTimeEntity {

    @Id
    @Column(name = "AID_MEDIC_INS_ID", unique = true)
    private String id;

    @Column(name = "AID_MEDIC_INS_NM")
    private String aid_medic_ins_nm;

    @Builder
    public MedicAidIns(String id, String aid_medic_ins_nm) {
        this.id = id;
        this.aid_medic_ins_nm = aid_medic_ins_nm;
    }
}
