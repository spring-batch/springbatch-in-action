package com.batch.domain.repository;

import com.batch.domain.batch.LibraryDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryDetailEntityRepository extends JpaRepository<LibraryDetailEntity, Integer> {
    LibraryDetailEntity findByLbrryCode(Integer lbrryCode);
}
