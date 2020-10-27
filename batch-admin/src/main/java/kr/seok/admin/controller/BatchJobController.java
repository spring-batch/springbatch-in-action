package kr.seok.admin.controller;

import kr.seok.admin.domain.BatchJobExecution;
import kr.seok.admin.domain.BatchJobNameInterface;
import kr.seok.admin.service.BatchJobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class BatchJobController {

    private final BatchJobService batchJobExecutionService;

    public BatchJobController(BatchJobService batchJobExecutionService) {
        this.batchJobExecutionService = batchJobExecutionService;
    }

    @GetMapping("/batchJobInstances")
    public ResponseEntity<?> getInstances() {
        List<BatchJobNameInterface> jobList = batchJobExecutionService.getBatchInstances();
        return ResponseEntity.ok(jobList);
    }

    @GetMapping("/batchJobExecutions")
    public ResponseEntity<?> getExecutions() {
        Map<Long, Object> jobExecutions = batchJobExecutionService.getBatchJobInstances();
        return ResponseEntity.ok().body(jobExecutions);
    }

    @GetMapping("/batchStepExecutions")
    public ResponseEntity<?> getStepExecutions() {

        return ResponseEntity.ok().body("");
    }
}
