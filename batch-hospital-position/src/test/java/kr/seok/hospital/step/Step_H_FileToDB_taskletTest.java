package kr.seok.hospital.step;

import kr.seok.hospital.domain.dto.HospitalFileDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@SpringBootTest
class Step_H_FileToDB_taskletTest {

    @Autowired
    private  StepBuilderFactory stepBuilderFactory;
    @Autowired
    private EntityManagerFactory entityManagerFactory;

    Step_H_FileToDB_tasklet fileToDB_tasklet;

    @BeforeEach
    public void setUp() {
        fileToDB_tasklet = new Step_H_FileToDB_tasklet(stepBuilderFactory, entityManagerFactory);
    }

    @Test
    void testCase1() {

        List<HospitalFileDto> hospital = fileToDB_tasklet.readFile();
//        hospital.forEach(System.out::println);
    }
}
