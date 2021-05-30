package kr.seok.library.domain.repository;

import kr.seok.library.domain.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<CityEntity, Long> {
    CityEntity findByCityNm(String cityNm);
}
