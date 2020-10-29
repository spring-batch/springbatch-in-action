package kr.seok.admin.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BATCH_JOB_INSTANCE")
public class BatchJobInstance {
    @Id
    @Column(name = "JOB_INSTANCE_ID")
    private Long jobInstanceId;
    @Column(name = "VERSION")
    private Long version;
    @Column(name = "JOB_NAME")
    private String jobName;
    @Column(name = "JOB_KEY")
    private String jobKey;
}
