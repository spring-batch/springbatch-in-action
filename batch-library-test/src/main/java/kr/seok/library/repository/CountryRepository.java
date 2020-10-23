package kr.seok.library.repository;

import kr.seok.library.domain.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    Optional<CountryEntity> findByCityIdAndCountryNm(Long cityId, String countryNm);
}
