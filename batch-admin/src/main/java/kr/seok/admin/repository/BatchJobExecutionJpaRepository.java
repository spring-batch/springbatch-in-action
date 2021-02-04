package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchJobExecution;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchJobExecutionJpaRepository extends JpaRepository<BatchJobExecution, Long> {
}
