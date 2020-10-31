package kr.seok.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

public class BatchJobDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StepExecutionRequest {
        @NotNull
        private Long jobExecutionId;
    }
}
