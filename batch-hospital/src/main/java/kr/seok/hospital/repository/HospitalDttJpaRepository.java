package kr.seok.hospital.repository;

import kr.seok.hospital.domain.HospitalDtt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalDttJpaRepository extends JpaRepository<HospitalDtt, String> {
}
