package kr.seok.admin.service;

import kr.seok.admin.domain.BatchJobExecution;
import kr.seok.admin.domain.BatchJobInstance;
import kr.seok.admin.domain.BatchJobNameInterface;
import kr.seok.admin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BatchJobService {

    private final BatchJobExecutionRepository batchJobExecutionRepository;
    private final BatchJobExecutionContextRepository batchJobExecutionContextRepository;
    private final BatchJobExecutionParamsRepository batchJobExecutionParamsRepository;
    private final BatchJobInstanceRepository batchJobInstanceRepository;
    private final BatchStepExecutionRepository batchStepExecutionRepository;
    private final BatchStepExecutionContextRepository batchStepExecutionContextRepository;

    @Autowired
    public BatchJobService(
            BatchJobExecutionRepository batchJobExecutionRepository,
            BatchJobExecutionContextRepository batchJobExecutionContextRepository,
            BatchJobExecutionParamsRepository batchJobExecutionParamsRepository,
            BatchJobInstanceRepository batchJobInstanceRepository,
            BatchStepExecutionRepository batchStepExecutionRepository,
            BatchStepExecutionContextRepository batchStepExecutionContextRepository
    ) {
        this.batchJobExecutionRepository = batchJobExecutionRepository;
        this.batchJobExecutionContextRepository = batchJobExecutionContextRepository;
        this.batchJobExecutionParamsRepository = batchJobExecutionParamsRepository;
        this.batchJobInstanceRepository = batchJobInstanceRepository;
        this.batchStepExecutionRepository = batchStepExecutionRepository;
        this.batchStepExecutionContextRepository = batchStepExecutionContextRepository;
    }

    @Transactional(readOnly = true)
    public List<BatchJobNameInterface> getBatchInstances() {
        List<BatchJobNameInterface> jobInstances = batchJobInstanceRepository.groupByJobNames();
        return jobInstances;
    }

    @Transactional(readOnly = true)
    public Map<Long, Object> getBatchJobInstances() {
        String jobName = "TOTAL_PROCESS_JOB";
        List<BatchJobInstance> jobInstances = batchJobInstanceRepository.findByJobName(jobName);
        Map<Long, Object> executionPerInstance = new HashMap<>();

        for(BatchJobInstance instance : jobInstances) {
            Long jobInstanceId = instance.getJobInstanceId();
            List<BatchJobExecution> executions = batchJobExecutionRepository.findByJobInstanceId(jobInstanceId);

            executionPerInstance.put(jobInstanceId, executions);
        }

        return executionPerInstance;
    }
}
