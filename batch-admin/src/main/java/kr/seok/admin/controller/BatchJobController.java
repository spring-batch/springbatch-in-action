package kr.seok.admin.controller;

import kr.seok.admin.domain.BatchJobExecution;
import kr.seok.admin.service.BatchJobExecutionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BatchJobController {

    private final BatchJobExecutionService batchJobExecutionService;

    public BatchJobController(BatchJobExecutionService batchJobExecutionService) {
        this.batchJobExecutionService = batchJobExecutionService;
    }

    @GetMapping("/")
    public ResponseEntity<?> getList() {
        List<BatchJobExecution> jobList = batchJobExecutionService.getBatchList();
        return ResponseEntity.ok(jobList);
    }
}
