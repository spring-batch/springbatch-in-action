package kr.seok.library.domain.repository;

import kr.seok.library.domain.entity.LibraryTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryTypeRepository extends JpaRepository<LibraryTypeEntity, Long> {
    LibraryTypeEntity findByLibraryType(String libraryType);
}
