package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchJobExecution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface BatchJobExecutionRepository extends JpaRepository<BatchJobExecution, Long> {

    List<BatchJobExecution> findByJobInstanceId(Long jobInstanceId);
}
