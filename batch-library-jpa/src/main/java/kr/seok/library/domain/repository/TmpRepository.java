package kr.seok.library.domain.repository;

import kr.seok.library.domain.entity.TmpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmpRepository extends JpaRepository<TmpEntity, Long> {
}
