package kr.seok.admin.controller;

import kr.seok.admin.service.BatchJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class BatchJobController {

    private final BatchJobService batchJobService;

    /**
     *  JOB_NAME을 기준으로 배치 인스턴스의 리스트를 가져온다.
     */
    @GetMapping("/batchJobInstances")
    public ResponseEntity<?> getInstances() {
        return ResponseEntity.ok().body(batchJobService.getJobInstance());
    }

    @GetMapping("/batchJobExecutions")
    public ResponseEntity<?> getExecutions(
            @RequestParam("jobName") String jobName) {
        return ResponseEntity.ok().body("{}");
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
