package kr.seok.admin.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@Table(name = "BATCH_JOB_INSTANCE")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BatchJobInstance implements Serializable {

    @Id
    @Column(name = "JOB_INSTANCE_ID")
    private Long jobInstanceId;
    @Column(name = "VERSION")
    private Long version;
    @Column(name = "JOB_NAME")
    private String jobName;
    @Column(name = "JOB_KEY")
    private String jobKey;

    @Builder
    public BatchJobInstance(Long jobInstanceId, Long version, String jobName, String jobKey) {
        this.jobInstanceId = jobInstanceId;
        this.version = version;
        this.jobName = jobName;
        this.jobKey = jobKey;
    }
}
