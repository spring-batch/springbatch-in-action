package kr.seok.admin.controller;

import kr.seok.admin.dto.JobInstanceDto;
import kr.seok.admin.service.BatchJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class BatchJobController {

    private final BatchJobService batchJobService;

    @GetMapping("/batchJobInstances")
    public ResponseEntity<?> getInstances() {
        Map<String, Set<JobInstanceDto>> findGroupByJobName = batchJobService.getJobInstanceGroupByJobName();
        return ResponseEntity.ok().body(findGroupByJobName);
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
