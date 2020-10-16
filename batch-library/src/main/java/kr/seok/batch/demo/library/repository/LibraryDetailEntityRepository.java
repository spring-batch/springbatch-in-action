package kr.seok.batch.demo.library.repository;


import kr.seok.batch.demo.library.domain.LibraryDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryDetailEntityRepository extends JpaRepository<LibraryDetailEntity, Integer> {

    LibraryDetailEntity findByLbrryCode(Integer lbrryCode);
}
