package kr.seok.hospital.listener;

import kr.seok.hospital.repository.HospitalJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Listener_A_DB implements JobExecutionListener {
    private final HospitalJpaRepository hospitalJpaRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        hospitalJpaRepository.deleteAllInBatch();
        log.info("JOB 실행 테이블 비우기 : TB_HOSPITAL({})", hospitalJpaRepository.count());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("JOB 실행 후 : TB_HOSPITAL({})", hospitalJpaRepository.count());
    }
}
