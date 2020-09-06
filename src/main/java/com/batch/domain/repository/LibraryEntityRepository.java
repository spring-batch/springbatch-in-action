package com.batch.domain.repository;

import com.batch.domain.batch.LibraryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryEntityRepository extends JpaRepository<LibraryEntity, Integer> {
    LibraryEntity findByLbrryNmAndCtprvnCodeAndSignguCode(String lbrryNm, Integer ctprvnCode, Integer signguCode);
}
