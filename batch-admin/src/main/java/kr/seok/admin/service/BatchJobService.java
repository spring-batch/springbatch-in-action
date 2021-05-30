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

    public Map<String, Set<JobInstanceDto>> getJobInstanceGroupByJobName() {
        return batchJobInstanceJpaRepository.findJobInstanceGroupByJobName();
    }

    public BatchJobInstance getJobInstances(Long jobInstanceId) {
        return batchJobInstanceJpaRepository.findByJobInstanceId(jobInstanceId);
    }

    public List<BatchJobInstance> getJobInstanceByJobName(String jobName) {
        return batchJobInstanceJpaRepository.findAllByJobName(jobName);
    }
}
