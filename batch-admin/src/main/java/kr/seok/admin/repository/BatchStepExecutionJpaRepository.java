package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchStepExecution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchStepExecutionJpaRepository extends JpaRepository<BatchStepExecution, Long> {
}
