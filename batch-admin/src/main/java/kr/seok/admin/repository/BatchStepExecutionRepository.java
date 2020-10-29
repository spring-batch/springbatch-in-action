package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchStepExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;

import java.util.List;

public interface BatchStepExecutionRepository extends JpaRepository<BatchStepExecution, Long> {
    List<BatchStepExecution> findByJobExecutionId(Long jobExecutionId);
}
