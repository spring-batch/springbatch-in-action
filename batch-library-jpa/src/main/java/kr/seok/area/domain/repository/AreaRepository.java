package kr.seok.area.domain.repository;

import kr.seok.area.domain.entity.AreaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<AreaEntity, Long> {
}
