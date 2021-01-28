package kr.seok.hospital.repository.querydsl;

import kr.seok.hospital.domain.Hospital;
import kr.seok.hospital.domain.HospitalInf;
import kr.seok.hospital.domain.dto.HospitalEsEntity;

import java.util.List;

public interface HospitalQuerydslRepository {
    List<Hospital> groupByCore();
    List<HospitalEsEntity> findFetchAll();
}
