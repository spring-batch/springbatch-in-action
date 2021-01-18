package kr.seok.hospital.repository;

import kr.seok.hospital.domain.HospitalInf;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalInfRepository extends JpaRepository<HospitalInf, String> {
}
