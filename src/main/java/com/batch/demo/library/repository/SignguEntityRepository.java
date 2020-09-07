package com.batch.demo.library.repository;

import com.batch.demo.library.domain.Signgu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignguEntityRepository extends JpaRepository<Signgu, Integer> {
    Signgu findBySignguNm(String signguNm);

    Signgu findBySignguNmAndCtprvnCode(String signguNm, Integer ctprvnCode);

    Signgu findBySignguCode(Integer signguCode);
}
