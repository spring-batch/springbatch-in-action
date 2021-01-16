package kr.seok.hospital.repository;

import kr.seok.hospital.domain.Hospital;

import java.util.List;

public interface HospitalQuerydslRepository {
    List<Hospital> groupByCore();
}
