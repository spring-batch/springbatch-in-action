package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchJobInstance;
import kr.seok.admin.domain.BatchJobNameInterface;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BatchJobInstanceRepository extends JpaRepository<BatchJobInstance, Long> {
    @Query(nativeQuery = true,
            value = "SELECT JOB_NAME AS jobName FROM BATCH_JOB_INSTANCE GROUP BY JOB_NAME"
    )
    List<BatchJobNameInterface> groupByJobNames();

    @Query(nativeQuery = true,
            value = "SELECT JOB_NAME AS jobName FROM BATCH_JOB_INSTANCE WHERE 1=1 AND JOB_NAME = :jobName GROUP BY JOB_NAME"
    )
    List<BatchJobNameInterface> groupByJobNames(@Param("jobName") String jobName);

    List<BatchJobInstance> findByJobName(String jobName);
}
