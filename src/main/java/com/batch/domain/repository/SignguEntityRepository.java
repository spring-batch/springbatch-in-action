package com.batch.domain.repository;

import com.batch.domain.batch.Signgu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignguEntityRepository extends JpaRepository<Signgu, Integer> {
    Signgu findBySignguNm(String signguNm);

    Signgu findBySignguNmAndCtprvnCode(String signguNm, Integer ctprvnCode);

    Signgu findBySignguCode(Integer signguCode);
}
