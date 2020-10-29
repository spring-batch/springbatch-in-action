package kr.seok.admin.service;

import kr.seok.admin.domain.*;
import kr.seok.admin.dto.BatchJobDto;
import kr.seok.admin.dto.BatchJobExecutionDto;
import kr.seok.admin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.query.AbstractJpaQuery;
import org.springframework.data.jpa.repository.query.JpaParametersParameterAccessor;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;
import org.springframework.data.jpa.repository.query.JpaQueryExecution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
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
        return batchJobInstanceRepository.groupByJobNames();
    }

    @Transactional(readOnly = true)
    public Map<Long, Object> getJobExecutionsPerJobName(
            BatchJobDto.@Valid JobExecutionRequest executionJobRequest
    ) {
        /* JOB_NAME에 대한 INSTANCE 리스트 조뢰 */
        String jobName = executionJobRequest.getJobName();
        List<BatchJobInstance> jobInstances = batchJobInstanceRepository.findByJobName(jobName);

        List<BatchJobExecutionDto> responseJobExecutions = new ArrayList<>();

        Map<Long, Object> executionPerInstance = new HashMap<>();
        /* JobName으로 조회된 JobInstance 리스트 */
        for(BatchJobInstance item : jobInstances) {
            Long jobInstanceId = item.getJobInstanceId();
            List<BatchJobExecution> batchJobExecutions = batchJobExecutionRepository.findByJobInstanceId(jobInstanceId);

            /* JobInstance에 대한 JobExecution List */
            for(BatchJobExecution executionItem : batchJobExecutions) {
                responseJobExecutions.add(
                        BatchJobExecutionDto.builder()
                                .jobName(jobName)
                                .jobInstanceId(jobInstanceId)
                                .startTime(executionItem.getStartTime())
                                .lastUpdated(executionItem.getLastUpdated())
                                .jobExecutionId(executionItem.getJobExecutionId())
                                .exitMessage(executionItem.getExitMessage())
                                .exitCode(executionItem.getExitCode())
                                .endTime(executionItem.getEndTime())
                                .createTime(executionItem.getCreateTime())
                                .status(executionItem.getStatus())
                                .build()
                );
                List<BatchJobExecutionParams> params = batchJobExecutionParamsRepository.findByJobExecutionId(executionItem.getJobExecutionId());
                executionPerInstance.put(jobInstanceId, params);
            }
        }
        return executionPerInstance;
    }

    @Transactional(readOnly = true)
    public Map<Long, Object> getStepExecutionsPerJobExecution(
            BatchStepExecution.@Valid RequestBody executionStepRequest
    ) {
        /* */
        Long jobExecutionId = executionStepRequest.getJobExecutionId();
        Map<Long, Object> stepExecutionMap = new HashMap<>();

        List<BatchStepExecution> stepExecutions = batchStepExecutionRepository.findByJobExecutionId(jobExecutionId);
        stepExecutionMap.put(jobExecutionId, stepExecutions);

        return stepExecutionMap;
    }
}
