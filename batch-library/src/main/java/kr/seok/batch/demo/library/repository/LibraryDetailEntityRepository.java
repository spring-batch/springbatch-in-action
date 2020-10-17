package kr.seok.batch.demo.library.repository;


import kr.seok.batch.demo.library.domain.LibraryDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LibraryDetailEntityRepository extends JpaRepository<LibraryDetailEntity, Integer> {

    Optional<LibraryDetailEntity> findByLbrryCode(Integer lbrryCode);
}
