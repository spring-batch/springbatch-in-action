package kr.seok.admin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "BATCH_JOB_EXECUTION_CONTEXT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BatchJobExecutionParams implements Serializable {

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateVal;

    @Column(name = "LONG_VAL")
    private Long longVal;

    @Column(name = "DOUBLE_VAL")
    private Double doubleVal;

    @Column(name = "IDENTIFYING")
    private Character identifying;

}
