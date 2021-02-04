package kr.seok.admin.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Table(name = "BATCH_STEP_EXECUTION_CONTEXT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BatchStepExecutionContext implements Serializable {
    @Id
    @Column(name = "STEP_EXECUTION_ID")
    private Long stepExecutionId;
    @Column(name = "SHORT_CONTEXT")
    private String shortContext;
    @Column(name = "SERIALIZED_CONTEXT")
    private String serializedContext;
}
