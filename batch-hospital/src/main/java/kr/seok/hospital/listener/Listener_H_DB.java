package kr.seok.hospital.listener;

import kr.seok.hospital.repository.HospitalDttJpaRepository;
import kr.seok.hospital.repository.HospitalInfJpaRepository;
import kr.seok.hospital.repository.HospitalPosJpaRepository;
import kr.seok.hospital.repository.MedicAidInsJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Listener_H_DB implements JobExecutionListener {

    private final MedicAidInsJpaRepository medicAidInsJpaRepository;
    private final HospitalInfJpaRepository hospitalInfJpaRepository;
    private final HospitalDttJpaRepository hospitalDttJpaRepository;
    private final HospitalPosJpaRepository hospitalPosJpaRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        hospitalInfJpaRepository.deleteAllInBatch();
        hospitalDttJpaRepository.deleteAllInBatch();
        hospitalPosJpaRepository.deleteAllInBatch();
        medicAidInsJpaRepository.deleteAllInBatch();

        log.info("JOB 실행 테이블 비우기 : TB_HOSPITAL_INF({})", hospitalInfJpaRepository.count());
        log.info("JOB 실행 테이블 비우기 : TB_HOSPITAL_DTT({})", hospitalDttJpaRepository.count());
        log.info("JOB 실행 테이블 비우기 : TB_HOSPITAL_POS({})", hospitalPosJpaRepository.count());
        log.info("JOB 실행 테이블 비우기 : TB_MEDIC_AID_INS({})", medicAidInsJpaRepository.count());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }
}
