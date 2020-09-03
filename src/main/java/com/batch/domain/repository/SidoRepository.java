package com.batch.domain.repository;

import com.batch.domain.batch.Sido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SidoRepository extends JpaRepository<Sido, Integer> {
    List<Sido> findAllByCtprvnNm(String ctprvnNm);
}
