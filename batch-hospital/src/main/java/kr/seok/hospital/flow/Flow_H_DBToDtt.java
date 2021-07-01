package kr.seok.hospital.flow;

import com.google.common.collect.Sets;
import kr.seok.hospital.domain.Hospital;
import kr.seok.hospital.domain.HospitalDtt;
import kr.seok.hospital.domain.dto.DataShareBean;
import kr.seok.hospital.repository.HospitalDttJpaRepository;
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
public class Flow_H_DBToDtt implements Tasklet {

    private final DataShareBean<List<Hospital>> dataShareBean;
    private final HospitalDttJpaRepository hospitalDttJpaRepository;
    private Set<HospitalDtt> hospitalDttSet = Sets.newHashSet();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("parallelStep3 ::  Data is {}", dataShareBean.getSize() != 0);

        List<Hospital> hospital = dataShareBean.getData("hospital");

        for(Hospital item : hospital) {
            hospitalDttSet.add(toHospitalDttEntity(item));
        }

        log.info("hospitalDttSet 사이즈: {}", hospitalDttSet.size());
        hospitalDttJpaRepository.saveAll(hospitalDttSet);

        return RepeatStatus.FINISHED;
    }

    private HospitalDtt toHospitalDttEntity(Hospital hospital) {
        return HospitalDtt.builder()
                .id(hospital.getId())
                .operHourFriC(hospital.getOperHourFriC())
                .operHourFriS(hospital.getOperHourFriS())
                .operHourHolC(hospital.getOperHourHolC())
                .operHourHolS(hospital.getOperHourHolS())
                .operHourMonC(hospital.getOperHourMonC())
                .operHourMonS(hospital.getOperHourMonS())
                .operHourSatC(hospital.getOperHourSatC())
                .operHourSatS(hospital.getOperHourSatS())
                .operHourSunC(hospital.getOperHourSunC())
                .operHourSunS(hospital.getOperHourSunS())
                .operHourThuS(hospital.getOperHourThuS())
                .operHourThuC(hospital.getOperHourThuC())
                .operHourTueC(hospital.getOperHourTueC())
                .operHourTueS(hospital.getOperHourTueS())
                .operHourWedC(hospital.getOperHourWedC())
                .operHourWedS(hospital.getOperHourWedS())
                .build();
    }
}
