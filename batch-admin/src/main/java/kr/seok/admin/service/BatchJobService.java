package kr.seok.admin.service;

import kr.seok.admin.domain.BatchJobInstance;
import kr.seok.admin.dto.JobInstanceDto;
import kr.seok.admin.repository.BatchJobInstanceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BatchJobService {

    private final BatchJobInstanceJpaRepository batchJobInstanceJpaRepository;

    /**
     * JobName을 기준으로 JobInstance 조회
     * @return
     */
    public Map<String, Set<JobInstanceDto>> getJobInstanceGroupByJobName() {
        return batchJobInstanceJpaRepository.findJobInstanceGroupByJobName();
    }

    /**
     * JobInstanceId로 Job Instance 조회
     * @param jobInstanceId
     * @return JobInstance 조회
     */
    public BatchJobInstance getJobInstances(Long jobInstanceId) {
        return batchJobInstanceJpaRepository.findByJobInstanceId(jobInstanceId);
    }

    /**
     * JobName으로 JobInstance 조회
     * @param jobName JobName
     * @return JobInstance 조회
     */
    public List<BatchJobInstance> getJobInstanceByJobName(String jobName) {
        return batchJobInstanceJpaRepository.findAllByJobName(jobName);
    }
}
