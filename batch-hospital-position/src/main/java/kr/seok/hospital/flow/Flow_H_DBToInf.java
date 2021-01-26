package kr.seok.hospital.flow;

import com.google.common.collect.Sets;
import kr.seok.hospital.domain.Hospital;
import kr.seok.hospital.domain.HospitalInf;
import kr.seok.hospital.domain.MedicAidIns;
import kr.seok.hospital.domain.dto.DataShareBean;
import kr.seok.hospital.repository.HospitalInfRepository;
import kr.seok.hospital.repository.MedicAidInsRepository;
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
public class Flow_H_DBToInf implements Tasklet {

    private final DataShareBean<List<Hospital>> dataShareBean;
    private final MedicAidInsRepository medicAidInsRepository;
    private final HospitalInfRepository hospitalInfRepository;

    private final Set<MedicAidIns> medicAidIns = Sets.newHashSet();
    private final Set<HospitalInf> hospitalInfSet = Sets.newHashSet();

    @Override
    public RepeatStatus execute(
            StepContribution contribution, ChunkContext chunkContext) {
        log.info("parallelStep2 : {}", dataShareBean.getSize());

        List<Hospital> hospitals = dataShareBean.getData("hospital");

        if(hospitals.isEmpty()) return RepeatStatus.FINISHED;

        hospitals.forEach(h -> {
            MedicAidIns m = toMedicEntity(h);
            medicAidIns.add(m);
            hospitalInfSet.add(toHospitalInfEntity(h, m));
        });

        log.info("MedicAidIns 사이즈: {}", medicAidIns.size());
        log.info("hospitalInfSet 사이즈: {}", hospitalInfSet.size());

         // 단방향 연관관계가 걸려 있어서 HospitalInf 엔티티를 저장한다고해서 한 번에 저장되지 않음
        medicAidInsRepository.saveAll(medicAidIns);
        hospitalInfRepository.saveAll(hospitalInfSet);

        return RepeatStatus.FINISHED;
    }

    private MedicAidIns toMedicEntity(Hospital hospital) {
        return MedicAidIns.builder()
                .id(hospital.getFstAidMedicInsCd())
                .aid_medic_ins_nm(hospital.getFstAidMedicInsNm())
                .build();
    }

    private HospitalInf toHospitalInfEntity(Hospital hospital, MedicAidIns medicEntity) {
        return HospitalInf.builder()
                .id(hospital.getId())
                .addr(hospital.getAddr())
                .edOperYn(hospital.getEdOperYn())
                .edPhone(hospital.getEdPhone())
                .etc(hospital.getEtc())
                .medicAidIns(medicEntity)
                .hosCate(hospital.getHosCate())
                .hosCateNm(hospital.getHosCateNm())
                .operDescDt(hospital.getOperDescDt())
                .operNm(hospital.getOperNm())
                .phone1(hospital.getPhone1())
                .zipCode1(hospital.getZipCode1())
                .zipCode2(hospital.getZipCode2())
                .build();
    }
}
