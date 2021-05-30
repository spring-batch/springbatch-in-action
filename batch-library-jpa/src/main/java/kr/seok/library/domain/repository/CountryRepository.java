package kr.seok.library.domain.repository;

import kr.seok.library.domain.entity.CityEntity;
import kr.seok.library.domain.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {
    CountryEntity findByCityEntityAndCountryNm(CityEntity cityEntity, String countryNm);
}
