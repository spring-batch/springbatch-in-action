package kr.seok.hospital.repository;

import kr.seok.hospital.domain.Hospital;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class HospitalJpaRepositoryTest {

    @Autowired
    HospitalJpaRepository hospitalJpaRepository;

    @BeforeEach
    public void setUp() {
        Hospital hospital = Hospital.builder()
                .id("A0001")
                .addr("서울")
                .hosCateNm("치과의원")
                .build();
        hospitalJpaRepository.save(hospital);
    }

    @Test
    @DisplayName("repository 테스트")
    void testCase1() {

        List<Hospital> hList = hospitalJpaRepository.findAll();
        hList.forEach(h -> System.out.println("h -> " + h));
        assertThat(hList.get(0).getId()).contains("A0001");

    }
}
