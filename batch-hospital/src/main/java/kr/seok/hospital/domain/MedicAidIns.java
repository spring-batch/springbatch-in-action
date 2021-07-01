package kr.seok.hospital.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity(name = "medicAidIns")
@Getter
@Table(name = "TB_MEDIC_AID_INS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "aid_medic_ins_nm"})
public class MedicAidIns extends BaseTimeEntity {

    @Id
    @Column(name = "AID_MEDIC_INS_ID", unique = true)  /* 응급의료기관코드 */
    private String id;

    @Column(name = "AID_MEDIC_INS_NM")  /* 응급의료기관코드명 */
    private String aid_medic_ins_nm;

    @Builder
    public MedicAidIns(String id, String aid_medic_ins_nm) {
        this.id = id;
        this.aid_medic_ins_nm = aid_medic_ins_nm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MedicAidIns)) return false;
        final MedicAidIns that = (MedicAidIns) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
