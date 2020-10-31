package kr.seok.admin.controller;

import kr.seok.admin.domain.*;
import kr.seok.admin.dto.BatchJobDto;
import kr.seok.admin.dto.JobExecutionRequest;
import kr.seok.admin.dto.JobInstanceRequest;
import kr.seok.admin.service.BatchJobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
public class BatchJobController {

    private final BatchJobService batchJobExecutionService;

    public BatchJobController(BatchJobService batchJobExecutionService) {
        this.batchJobExecutionService = batchJobExecutionService;
    }

    /**
     *  JOB_NAME을 기준으로 배치 인스턴스의 리스트를 가져온다.
     */
    @GetMapping("/batchJobInstances")
    public ResponseEntity<?> getInstances(@ModelAttribute JobInstanceRequest request) {
        List<BatchJobInstance> jobList = batchJobExecutionService.getBatchInstances(request);
        return ResponseEntity.ok(jobList);
    }

    @GetMapping("/batchJobExecutions")
    public ResponseEntity<?> getExecutions(
            @Valid @ModelAttribute JobExecutionRequest request
    ) {
        Map<Long, Object> jobExecutions = batchJobExecutionService.getJobExecutionsPerJobName(request);
        return ResponseEntity.ok().body(jobExecutions);
    }
//
//    @GetMapping("/batchStepExecutions")
//    public ResponseEntity<?> getStepExecutions(
//            @Valid @ModelAttribute BatchStepExecution.RequestBody executionStepRequest
//    ) {
//        Map<Long, Object> stepExecutions = batchJobExecutionService.getStepExecutionsPerJobExecution(executionStepRequest);
//        return ResponseEntity.ok().body(stepExecutions);
//    }
}
