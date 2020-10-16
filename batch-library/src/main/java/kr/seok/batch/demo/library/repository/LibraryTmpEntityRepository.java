package kr.seok.batch.demo.library.repository;


import kr.seok.batch.demo.library.domain.LibraryTmpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryTmpEntityRepository extends JpaRepository<LibraryTmpEntity, String> {
}
