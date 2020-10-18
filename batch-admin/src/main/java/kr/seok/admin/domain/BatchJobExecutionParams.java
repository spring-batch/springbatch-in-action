package kr.seok.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BATCH_JOB_EXECUTION_PARAMS")
public class BatchJobExecutionParams {
    @Id
    @Column(name = "JOB_EXECUTION_ID")
    private Long jobExecutionId;
    @Column(name = "TYPE_CD")
    private String typeCd;
    @Column(name = "KEY_NAME")
    private String keyName;
    @Column(name = "STRING_VAL")
    private String stringVal;
    @Column(name = "DATE_VAL")
    private LocalDateTime dateVal;
    @Column(name = "LONG_VAL")
    private Long longVal;
    @Column(name = "DOUBLE_VAL")
    private Double doubleVal;
    @Column(name = "IDENTIFYING")
    private Character identifying;

}
