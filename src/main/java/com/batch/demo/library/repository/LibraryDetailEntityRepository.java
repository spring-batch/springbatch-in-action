package com.batch.demo.library.repository;

import com.batch.demo.library.domain.LibraryDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryDetailEntityRepository extends JpaRepository<LibraryDetailEntity, Integer> {

    LibraryDetailEntity findByLbrryCode(Integer lbrryCode);
}
