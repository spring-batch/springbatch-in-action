package kr.seok.hospital.listener;

import kr.seok.hospital.repository.HospitalDttRepository;
import kr.seok.hospital.repository.HospitalInfRepository;
import kr.seok.hospital.repository.HospitalPosRepository;
import kr.seok.hospital.repository.MedicAidInsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Listener_H_DB implements JobExecutionListener {

    private final MedicAidInsRepository medicAidInsRepository;
    private final HospitalInfRepository hospitalInfRepository;
    private final HospitalDttRepository hospitalDttRepository;
    private final HospitalPosRepository hospitalPosRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        hospitalInfRepository.deleteAllInBatch();
        hospitalDttRepository.deleteAllInBatch();
        hospitalPosRepository.deleteAllInBatch();
        medicAidInsRepository.deleteAllInBatch();

        log.info("JOB 실행 테이블 비우기 : TB_HOSPITAL_INF({})", hospitalInfRepository.count());
        log.info("JOB 실행 테이블 비우기 : TB_HOSPITAL_DTT({})", hospitalDttRepository.count());
        log.info("JOB 실행 테이블 비우기 : TB_HOSPITAL_POS({})", hospitalPosRepository.count());
        log.info("JOB 실행 테이블 비우기 : TB_MEDIC_AID_INS({})", medicAidInsRepository.count());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }
}
