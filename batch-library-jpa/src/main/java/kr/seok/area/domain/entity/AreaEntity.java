package kr.seok.area.domain.entity;

import kr.seok.library.domain.entity.CommonEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "TB_AREA")
@EqualsAndHashCode(callSuper = false, of = {"areaCd", "bigLocalNm", "midLocalNm", "smallLocalNm", "tooSmallLocalNm", "useYn"})
@AttributeOverride(name = "id", column = @Column(name = "AREA_ID"))
public class AreaEntity extends CommonEntity implements Serializable {
    private String areaCd;
    private String bigLocalNm;
    private String midLocalNm;
    private String smallLocalNm;
    private String tooSmallLocalNm;
    private Boolean useYn;

    @Builder
    public AreaEntity(String areaCd, String bigLocalNm, String midLocalNm, String smallLocalNm, String tooSmallLocalNm, Boolean useYn) {
        this.areaCd = areaCd;
        this.bigLocalNm = bigLocalNm;
        this.midLocalNm = midLocalNm;
        this.smallLocalNm = smallLocalNm;
        this.tooSmallLocalNm = tooSmallLocalNm;
        this.useYn = useYn;
    }
}
