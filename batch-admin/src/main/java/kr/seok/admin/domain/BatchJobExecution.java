package kr.seok.admin.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

//@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
//@Table(name = "BATCH_JOB_EXECUTION")
public class BatchJobExecution {
//    @Id
//    @Column(name = "JOB_EXECUTION_ID")
    private Long jobExecutionId;
//    @Column(name = "VERSION")
    private Long version;
//    @Column(name = "JOB_INSTANCE_ID")
    private Long jobInstanceId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(name = "START_TIME")
    private LocalDateTime startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(name = "END_TIME")
    private LocalDateTime endTime;
//    @Column(name = "STATUS")
    private String status;
//    @Column(name = "EXIT_CODE")
    private String exitCode;
//    @Column(name = "EXIT_MESSAGE")
    private String exitMessage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @Column(name = "LAST_UPDATED")
    private LocalDateTime lastUpdated;
//    @Column(name = "JOB_CONFIGURATION_LOCATION")
    private String jobConfigurationLocation;
}
