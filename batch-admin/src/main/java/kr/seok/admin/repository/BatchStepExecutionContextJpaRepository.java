package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchStepExecutionContext;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchStepExecutionContextJpaRepository extends JpaRepository<BatchStepExecutionContext, Long> {
}
