package com.batch.demo.library.repository;

import com.batch.demo.library.domain.Sido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SidoEntityRepository extends JpaRepository<Sido, Integer> {

    Sido findByCtprvnNm(String ctprvnNm);

    Sido findByCtprvnCode(Integer ctprvnCode);
}
