package kr.seok.admin.controller;

import kr.seok.admin.domain.*;
import kr.seok.admin.service.BatchJobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<?> getInstances(
            @RequestParam("jobName") String jobName
    ) {
        List<BatchJobInstance> jobList = batchJobExecutionService.getBatchInstances(jobName);
        return ResponseEntity.ok().body(jobList);
    }

    @GetMapping("/batchJobExecutions")
    public ResponseEntity<?> getExecutions(
            @RequestParam("jobName") String jobName
    ) {
        List<BatchJobExecution> jobExecutions = batchJobExecutionService.getJobExecutionsPerJobName(jobName);
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
