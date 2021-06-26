package kr.seok.admin.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.seok.admin.domain.BatchJobInstance;
import kr.seok.admin.dto.JobInstanceDto;
import kr.seok.admin.service.BatchJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@Api(value = "BatchJobExecution")
@RestController
@RequestMapping(value = "/api")
@RequiredArgsConstructor
public class BatchJobController {

    private final BatchJobService batchJobService;

    @ApiOperation(value = "JobName 별 JobInstance 조회")
    @GetMapping("/batchJobInstances")
    public ResponseEntity<?> getInstances() {
        Map<String, Set<JobInstanceDto>> findGroupByJobName = batchJobService.getJobInstanceGroupByJobName();
        return ResponseEntity.ok().body(findGroupByJobName);
    }

    @ApiOperation(value = "단일 Job Instance 조회")
    @GetMapping("/jobInstance/jobInstanceId/{jobInstanceId}")
    public ResponseEntity<?> findJobInstanceByJobInstanceId(@PathVariable("jobInstanceId") Long jobInstanceId) {

        BatchJobInstance jobInstances = batchJobService.getJobInstances(jobInstanceId);

        return ResponseEntity.ok().body(jobInstances);
    }

    @GetMapping("/jobInstance/jobName/{jobName}")
    public ResponseEntity<?> findJobInstanceByJobName(@PathVariable("jobName") String jobName) {
        return ResponseEntity.ok()
                .body(batchJobService.getJobInstanceByJobName(jobName));
    }

    @GetMapping("/batchJobExecutions")
    public ResponseEntity<?> getExecutions(@RequestParam("jobName") String jobName) {
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
