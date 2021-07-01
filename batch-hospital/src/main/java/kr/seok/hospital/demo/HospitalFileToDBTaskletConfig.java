package kr.seok.hospital.demo;

import kr.seok.hospital.listener.Listener_A_DB;
import kr.seok.hospital.repository.HospitalJpaRepository;
import kr.seok.hospital.step.HospitalFileToDBJpaTaskletStep;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Tasklet 기반 배치 프로세스 실행 클래스
 * 1. Listener를 이용한 테이블 초기화
 * 2. 파일 데이터 조회 및 DB 입력 Step 실행
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class HospitalFileToDBTaskletConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final HospitalFileToDBJpaTaskletStep hospitalFileToDBJpaTaskletStep; // 22s

    private final HospitalJpaRepository hospitalJpaRepository;
    private final String JOB_NAME = "H_TASKLET_FILE_TO_DB";

    @Value("${file.path}")
    private String filePath;

    /**
     * Hospital File To DB Tasklet 기반 프로세스 Job 실행
     *
     * @return the job
     */
    @Bean(name = JOB_NAME + "_JOB")
    public Job hFileToDb() {
        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .listener(new Listener_A_DB(hospitalJpaRepository))
                .incrementer(new RunIdIncrementer())
                /* reader -> processor -> writer 프로세스 */
                .start(hospitalFileToDBJpaTaskletStep.hFileToDbStep(filePath))
                .build();
    }

}
