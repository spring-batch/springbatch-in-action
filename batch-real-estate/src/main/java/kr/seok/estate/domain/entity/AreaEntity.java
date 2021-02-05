package kr.seok.estate.domain.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Table(name = "TB_AREA")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id", "depth1Nm", "depth2Nm", "depth3Nm", "depth4Nm", "useYn"})
public class AreaEntity implements Serializable {

    @Id
    @Column(name = "AREA_CD")
    private Long id;
    @Column(name = "DEPTH_1_NM")
    private String depth1Nm;
    @Column(name = "DEPTH_2_NM")
    private String depth2Nm;
    @Column(name = "DEPTH_3_NM")
    private String depth3Nm;
    @Column(name = "DEPTH_4_NM")
    private String depth4Nm;
    @Column(name = "USE_YN")
    private Boolean useYn;

    @Builder
    public AreaEntity(Long id, String depth1Nm, String depth2Nm, String depth3Nm, String depth4Nm, Boolean useYn) {
        this.id = id;
        this.depth1Nm = depth1Nm;
        this.depth2Nm = depth2Nm;
        this.depth3Nm = depth3Nm;
        this.depth4Nm = depth4Nm;
        this.useYn = useYn;
    }
}
