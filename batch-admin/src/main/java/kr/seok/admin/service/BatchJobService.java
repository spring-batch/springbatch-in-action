package kr.seok.admin.service;

import kr.seok.admin.domain.BatchJobExecution;
import kr.seok.admin.domain.BatchJobInstance;
import kr.seok.admin.dto.BatchJobDto;
import kr.seok.admin.dto.BatchJobExecutionDto;
import kr.seok.admin.dto.JobExecutionRequest;
import kr.seok.admin.dto.JobInstanceRequest;
import kr.seok.admin.repository.BatchJobExecutionDao;
import kr.seok.admin.repository.BatchJobInstanceDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class BatchJobService {

    private final BatchJobInstanceDao batchJobInstanceDao;
    private final BatchJobExecutionDao batchJobExecutionDao;

    @Transactional(readOnly = true)
    public List<BatchJobInstance> getBatchInstances(JobInstanceRequest request) {
        return batchJobInstanceDao.getJobInstances(request);
    }

    @Transactional(readOnly = true)
    public Map<Long, Object> getJobExecutionsPerJobName(
            JobExecutionRequest request
    ) {

        String jobName = request.getJobName();

        /* 1차 적으로 */
        Map<Long, Object> executionPerInstance = new HashMap<>();
        List<BatchJobExecutionDto> responseJobExecutions = new ArrayList<>();

        /* JOB_NAME에 대한 INSTANCE 리스트 조뢰 */
        List<BatchJobInstance> jobInstances = getBatchInstances(
                JobInstanceRequest.builder().jobName(jobName).build()
        );

        /* 조회된 JobInstance의 리스트의 execution 및 executionParams를 조회 */
        for(BatchJobInstance instance : jobInstances) {
            Long jobInstanceId = instance.getJobInstanceId();
            request.setJobInstanceId(jobInstanceId);
            List<BatchJobExecution> jobExecutions = batchJobExecutionDao.getJobExecutions(request);

            for(BatchJobExecution execution : jobExecutions) {
                Long jobExecutionId = execution.getJobExecutionId();

            }
        }

//        for(BatchJobInstance instance : jobInstances) {
//            /* JobName으로 조회된 JobInstance 리스트 */
//            Long jobInstanceId = instance.getJobInstanceId();
//            List<BatchJobExecution> jobExecutions = batchJobExecutionRepository.findByJobInstanceId(jobInstanceId);
//
//            for(BatchJobExecution execution : jobExecutions) {
//                Long jobExecutionId = execution.getJobExecutionId();
//                /* JobInstance에 대한 JobExecution List */
//                List<BatchJobExecutionParams> params = batchJobExecutionParamsRepository.findAllByJobExecutionId(jobExecutionId);
//                System.out.println("실행 : " + execution);
//                String keyParams = "";
//                for(BatchJobExecutionParams param : params) {
//                    System.out.println("파라미터 : " + param);
//                    keyParams += param.getKeyName() + ",";
//                }
//
//                responseJobExecutions.add(
//                        BatchJobExecutionDto.builder()
//                                .jobName(jobName)
//                                .jobInstanceId(jobInstanceId)
//                                .jobExecutionId(jobExecutionId)
//                                .keyName(keyParams.substring(0, keyParams.length() - 1))
//                                .startTime(execution.getStartTime())
//                                .lastUpdated(execution.getLastUpdated())
//                                .exitCode(execution.getExitCode())
//                                .exitMessage(execution.getExitMessage())
//                                .createTime(execution.getCreateTime())
//                                .endTime(execution.getEndTime())
//                                .status(execution.getStatus())
//                                .build()
//                );
//                executionPerInstance.put(jobInstanceId, responseJobExecutions);
//            }
//        }
//        return executionPerInstance;
        return null;
    }
//
//    @Transactional(readOnly = true)
//    public Map<Long, Object> getStepExecutionsPerJobExecution(
//            BatchStepExecution.@Valid RequestBody executionStepRequest
//    ) {
//        /* Job 실행 아이디 */
//        Long jobExecutionId = executionStepRequest.getJobExecutionId();
//        Map<Long, Object> stepExecutionMap = new HashMap<>();
//
//        /* Step 실행 아이디 */
//        List<BatchStepExecution> stepExecutions = batchStepExecutionRepository.findByJobExecutionId(jobExecutionId);
//        stepExecutionMap.put(jobExecutionId, stepExecutions);
//
//        return stepExecutionMap;
//    }
}
