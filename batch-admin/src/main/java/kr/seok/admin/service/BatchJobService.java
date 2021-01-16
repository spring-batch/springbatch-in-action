package kr.seok.admin.service;

import kr.seok.admin.domain.BatchJobExecution;
import kr.seok.admin.domain.BatchJobInstance;
import kr.seok.admin.repository.BatchJobExecutionDao;
import kr.seok.admin.repository.BatchJobInstanceDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BatchJobService {

    private final BatchJobInstanceDao batchJobInstanceDao;
    private final BatchJobExecutionDao batchJobExecutionDao;

    @Transactional(readOnly = true)
    public List<BatchJobInstance> getBatchInstances(String jobName) {
        return batchJobInstanceDao.getJobInstances(jobName);
    }

    @Transactional(readOnly = true)
    public List<BatchJobExecution> getJobExecutionsPerJobName(String JobName) {
        return batchJobExecutionDao.getJobExecutions(JobName);
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
