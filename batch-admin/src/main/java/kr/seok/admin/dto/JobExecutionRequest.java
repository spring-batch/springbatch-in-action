package kr.seok.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobExecutionRequest {

    private String jobName;
    private Long jobInstanceId;
}
