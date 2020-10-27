package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchJobExecutionParams;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BatchJobExecutionParamsRepository extends JpaRepository<BatchJobExecutionParams, Long> {
    List<BatchJobExecutionParams> findByJobExecutionId(Long jobExecutionId);
}
