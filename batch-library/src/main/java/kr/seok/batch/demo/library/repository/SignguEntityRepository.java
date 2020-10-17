package kr.seok.batch.demo.library.repository;


import kr.seok.batch.demo.library.domain.Signgu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignguEntityRepository extends JpaRepository<Signgu, Integer> {
    Signgu findBySignguNm(String signguNm);

    Signgu findBySignguNmAndCtprvnCode(String signguNm, Integer ctprvnCode);

    Optional<Signgu> findBySignguCode(Integer signguCode);
}
