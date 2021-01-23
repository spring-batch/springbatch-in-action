package kr.seok.hospital.flow;

import kr.seok.hospital.domain.Hospital;
import kr.seok.hospital.domain.dto.DataShareBean;
import kr.seok.hospital.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Flow_H_DbOrigin implements Tasklet {

    private final DataShareBean<List<Hospital>> dataShareBean;
    private final HospitalRepository hospitalRepository;

    @Override
    public RepeatStatus execute(
            StepContribution contribution, ChunkContext chunkContext) {

        List<Hospital> hospitals = hospitalRepository.findAll();
        dataShareBean.putData("hospital", hospitals);

        log.info("parallelStep1 : {}", hospitals.size());

        return RepeatStatus.FINISHED;
    }
}
