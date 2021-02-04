package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchJobExecutionParams;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchJobExecutionParamsJpaRepository extends JpaRepository<BatchJobExecutionParams, Long> {
}
