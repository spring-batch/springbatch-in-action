package kr.seok.library.repository;

import kr.seok.library.domain.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<CityEntity, Long> {
    Optional<CityEntity> findByCityNm(String cityNm);
}
