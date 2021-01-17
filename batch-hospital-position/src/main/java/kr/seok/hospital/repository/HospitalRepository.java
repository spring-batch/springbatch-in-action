package kr.seok.hospital.repository;

import kr.seok.hospital.domain.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 병원 전체 데이터 테이블 관련 쿼리
 */
public interface HospitalRepository extends JpaRepository<Hospital, String>, HospitalQuerydslRepository {
}
