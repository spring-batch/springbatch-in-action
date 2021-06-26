package kr.seok.admin.repository;

import javassist.NotFoundException;
import kr.seok.admin.domain.BatchJobExecution;
import kr.seok.admin.exception.NotFoundDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles(value = "test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("BATCH_JOB_EXECUTION")
class BatchJobExecutionContextJpaRepositoryTest {

    @Autowired
    private BatchJobExecutionJpaRepository batchJobExecutionJpaRepository;

    @DisplayName("findAll() - 데이터 조회 테스트")
    @Test
    void testCase1() {
        List<BatchJobExecution> all = batchJobExecutionJpaRepository.findAll();
        assertThat(all).isNotNull();
    }

    @DisplayName("findById() - 단일 데이터 조회")
    @Test
    void testCase2() {
        BatchJobExecution jobExecution = batchJobExecutionJpaRepository.findById(1L)
                .orElseThrow(NotFoundDataException::new);

        System.out.println(jobExecution);
        assertThat(jobExecution).isNotNull();
    }
}
