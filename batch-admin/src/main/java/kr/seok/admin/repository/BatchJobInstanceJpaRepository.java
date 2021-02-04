package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchJobInstance;
import kr.seok.admin.repository.querydsl.BatchJobInstanceQuerydslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchJobInstanceJpaRepository extends JpaRepository<BatchJobInstance, Long>, BatchJobInstanceQuerydslRepository {
}
