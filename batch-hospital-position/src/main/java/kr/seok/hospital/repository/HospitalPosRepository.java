package kr.seok.hospital.repository;

import kr.seok.hospital.domain.HospitalPos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalPosRepository extends JpaRepository<HospitalPos, String> {
}
