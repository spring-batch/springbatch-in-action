package kr.seok.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "BATCH_STEP_EXECUTION_CONTEXT")
public class BatchStepExecutionContext {
//    @Id
//    @Column(name = "STEP_EXECUTION_ID")
    private Long stepExecutionId;
//    @Column(name = "SHORT_CONTEXT")
    private String shortContext;
//    @Column(name = "SERIALIZED_CONTEXT")
    private String serializedContext;
}
