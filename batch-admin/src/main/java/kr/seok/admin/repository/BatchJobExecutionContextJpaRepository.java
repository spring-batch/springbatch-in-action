package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchJobExecutionContext;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchJobExecutionContextJpaRepository extends JpaRepository<BatchJobExecutionContext, Long> {
}
