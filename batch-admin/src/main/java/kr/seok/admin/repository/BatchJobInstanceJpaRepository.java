package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchJobInstance;
import kr.seok.admin.repository.querydsl.BatchJobInstanceQuerydslRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface BatchJobInstanceJpaRepository extends JpaRepository<BatchJobInstance, Long>, BatchJobInstanceQuerydslRepository {

    @Query(value = "select b from BatchJobInstance b where b.jobInstanceId = :jobInstanceId")
    BatchJobInstance findByJobInstanceId(@Param("jobInstanceId") Long jobInstanceId);

    @Query(value = "select b from BatchJobInstance b where b.jobName = :jobName")
    List<BatchJobInstance> findAllByJobName(@Param("jobName") String jobName);

}
