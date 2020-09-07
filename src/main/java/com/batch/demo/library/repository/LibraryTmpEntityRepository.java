package com.batch.demo.library.repository;

import com.batch.demo.library.domain.LibraryTmpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryTmpEntityRepository extends JpaRepository<LibraryTmpEntity, String> {
}
