package kr.seok.hospital.flow;

import com.google.common.collect.Sets;
import kr.seok.hospital.domain.Hospital;
import kr.seok.hospital.domain.HospitalPos;
import kr.seok.hospital.domain.dto.DataShareBean;
import kr.seok.hospital.repository.HospitalPosJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Flow_H_DBToPos implements Tasklet {

    private final HospitalPosJpaRepository hospitalPosJpaRepository;
    private final DataShareBean<List<Hospital>> dataShareBean;
    private Set<HospitalPos> hospitalPos = Sets.newHashSet();

    @Override
    public RepeatStatus execute(
            StepContribution contribution, ChunkContext chunkContext) {

        log.info("parallelStep4 : {}", dataShareBean.getSize());

        List<Hospital> hospitals = dataShareBean.getData("hospital");

        hospitals.forEach(h -> {
            hospitalPos.add(toHospitalPosEntity(h));
        });

        log.info("hospitalPos 사이즈: {}", hospitalPos.size());
        hospitalPosJpaRepository.saveAll(hospitalPos);

        return RepeatStatus.FINISHED;
    }

    private HospitalPos toHospitalPosEntity(Hospital hospital) {
        return HospitalPos.builder()
                .id(hospital.getId())
                .lat(hospital.getLat())
                .lon(hospital.getLon())
                .simpleMap(hospital.getSimpleMap())
                .build();
    }
}
