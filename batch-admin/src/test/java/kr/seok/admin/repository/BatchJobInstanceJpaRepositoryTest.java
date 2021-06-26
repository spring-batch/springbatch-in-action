package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchJobInstance;
import kr.seok.admin.dto.JobInstanceDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles(value = "test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName(value = "BATCH_JOB_INSTANCE")
class BatchJobInstanceJpaRepositoryTest {

    @Autowired
    private BatchJobInstanceJpaRepository batchJobInstanceJpaRepository;

    @DisplayName("배치 잡 인스턴스 전체 데이터 조회 테스트")
    @Test
    void testCase1() {
        List<BatchJobInstance> all = batchJobInstanceJpaRepository.findAll();
        assertThat(all).isNotNull();
    }

    @DisplayName("findAllByJobName - JobName 으로 잡 인스턴스 리스트 조회 테스트")
    @Test
    void testCase2() {
        List<BatchJobInstance> testJob = batchJobInstanceJpaRepository.findAllByJobName("testJob");
        assertThat(testJob).isNotNull();
    }

    @DisplayName("findByJobInstanceId - JobInstanceId로 잡 인스턴스 단일 조회 테스트")
    @Test
    void testCase3() {
        BatchJobInstance jobInstance = batchJobInstanceJpaRepository.findByJobInstanceId(1L);
        assertThat(jobInstance).isNotNull();
    }

    @DisplayName("jobInstanceGroupByJobName - JobName으로 그룹화하여 실행된 JobInstance 조회 테스트")
    @Test
    void testCase4() {
        Map<String, Set<JobInstanceDto>> jobInstanceGroupByJobName = batchJobInstanceJpaRepository.findJobInstanceGroupByJobName();

        assertThat(jobInstanceGroupByJobName).isNotNull();
    }
}
