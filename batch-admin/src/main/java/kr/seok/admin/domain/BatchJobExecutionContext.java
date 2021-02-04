package kr.seok.admin.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Table(name = "BATCH_JOB_EXECUTION_CONTEXT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BatchJobExecutionContext implements Serializable {

    @Id
    @Column(name = "JOB_EXECUTION_ID")
    private Long jobExecutionId;

    @Column(name = "SHORT_CONTEXT")
    private String shortContext;

    @Column(name = "SERIALIZED_CONTEXT")
    private String serializedContext;
}
