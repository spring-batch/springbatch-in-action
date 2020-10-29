package kr.seok.admin.dto;

import kr.seok.admin.domain.BatchJobExecution;
import kr.seok.admin.domain.BatchStepExecution;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class BatchJobDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobExecutionRequest {
        @NotEmpty
        private String jobName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StepExecutionRequest {
        @NotNull
        private Long jobExecutionId;
    }
}
