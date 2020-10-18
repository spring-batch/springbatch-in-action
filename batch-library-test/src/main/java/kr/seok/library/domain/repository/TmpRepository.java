package kr.seok.library.domain.repository;

import kr.seok.library.domain.TmpEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TmpRepository extends JpaRepository<TmpEntity, Long> {
    @Override
    <S extends TmpEntity> boolean exists(Example<S> example);
}
