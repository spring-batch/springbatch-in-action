package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchJobExecutionContext;
import kr.seok.admin.repository.querydsl.BatchJobExecutionContextQuerydslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchJobExecutionContextJpaRepository extends JpaRepository<BatchJobExecutionContext, Long>, BatchJobExecutionContextQuerydslRepository {
}
