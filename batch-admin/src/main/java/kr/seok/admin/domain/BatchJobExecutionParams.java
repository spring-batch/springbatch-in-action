package kr.seok.admin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

//@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
//@Table(name = "BATCH_JOB_EXECUTION_PARAMS")
public class BatchJobExecutionParams implements Serializable {
//    @Id
//    @Column(name = "JOB_EXECUTION_ID")
    private Long jobExecutionId;
//    @Column(name = "TYPE_CD")
    private String typeCd;
//    @Column(name = "KEY_NAME")
    private String keyName;
//    @Column(name = "STRING_VAL")
    private String stringVal;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(name = "DATE_VAL")
    private LocalDateTime dateVal;
//    @Column(name = "LONG_VAL")
    private Long longVal;
//    @Column(name = "DOUBLE_VAL")
    private Double doubleVal;
//    @Column(name = "IDENTIFYING")
    private Character identifying;

}
