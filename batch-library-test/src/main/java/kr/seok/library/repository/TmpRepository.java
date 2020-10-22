package kr.seok.library.repository;

import kr.seok.library.domain.TmpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmpRepository extends JpaRepository<TmpEntity, Long> {
}
