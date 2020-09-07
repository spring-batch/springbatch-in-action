package com.batch.demo.library.repository;

import com.batch.demo.library.domain.LibraryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryEntityRepository extends JpaRepository<LibraryEntity, Integer> {
    LibraryEntity findByLbrryNmAndCtprvnCodeAndSignguCode(String lbrryNm, Integer ctprvnCode, Integer signguCode);
}
