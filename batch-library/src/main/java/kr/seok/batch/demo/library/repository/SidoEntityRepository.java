package kr.seok.batch.demo.library.repository;


import kr.seok.batch.demo.library.domain.Sido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SidoEntityRepository extends JpaRepository<Sido, Integer> {

    Sido findByCtprvnNm(String ctprvnNm);

    Optional<Sido> findByCtprvnCode(Integer ctprvnCode);
}
