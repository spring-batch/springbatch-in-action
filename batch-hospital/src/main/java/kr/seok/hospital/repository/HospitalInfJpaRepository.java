package kr.seok.hospital.repository;

import kr.seok.hospital.domain.HospitalInf;
import kr.seok.hospital.repository.querydsl.HospitalQuerydslRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HospitalInfJpaRepository extends JpaRepository<HospitalInf, String>, HospitalQuerydslRepository {

    List<HospitalInf> findTop3ByOrderByIdDesc();
}
