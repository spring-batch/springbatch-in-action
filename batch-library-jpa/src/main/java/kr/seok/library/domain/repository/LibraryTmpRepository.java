package kr.seok.library.domain.repository;

import kr.seok.library.domain.entity.LibraryTmpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryTmpRepository extends JpaRepository<LibraryTmpEntity, Long> {
}
