package kr.seok.admin.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BATCH_JOB_INSTANCE")
public class BatchJobInstance {
    @Id
    @Column(name = "JOB_EXECUTION_ID")
    private Long jobExecutionId;
    @Column(name = "VERSION")
    private Long version;
    @Column(name = "JOB_NAME")
    private String jobName;
    @Column(name = "JOB_KEY")
    private String jobKey;
}
