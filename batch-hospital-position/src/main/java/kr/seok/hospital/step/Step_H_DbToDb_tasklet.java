package kr.seok.hospital.step;

import com.google.common.collect.Sets;
import kr.seok.hospital.domain.*;
import kr.seok.hospital.listener.Listener_H_DB;
import kr.seok.hospital.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Step_H_DbToDb_tasklet {

    private final StepBuilderFactory stepBuilderFactory;
    private final HospitalRepository hospitalRepository;
    private final HospitalInfRepository hospitalInfRepository;
    private final HospitalDttRepository hospitalDttRepository;
    private final HospitalPosRepository hospitalPosRepository;
    private final MedicAidInsRepository medicAidInsRepository;

    private final Listener_H_DB listenerHDb;

    private static final String STEP_NAME = "STEP_H_DbToDb";

    public Step hDbToDbStep() {
        return stepBuilderFactory.get(STEP_NAME + "_STEP")
                .listener(listenerHDb)
                .tasklet((contribution, chunkContext) -> {
                    List<Hospital> hospitals = hospitalRepository.findAll();

                    Set<MedicAidIns> medicAidIns = Sets.newHashSet();
                    Set<HospitalInf> hospitalInfSet = Sets.newHashSet();
                    Set<HospitalDtt> hospitalDttSet = Sets.newHashSet();
                    Set<HospitalPos> hospitalPos = Sets.newHashSet();

                    hospitals.forEach(h -> {
                        MedicAidIns m = toMedicEntity(h);
                        medicAidIns.add(m);
                        hospitalInfSet.add(toHospitalInfEntity(h, m));
                        hospitalDttSet.add(toHospitalDttEntity(h));
                        hospitalPos.add(toHospitalPosEntity(h));
                    });

                    log.info("MedicAidIns 사이즈: {}", medicAidIns.size());
                    log.info("hospitalInfSet 사이즈: {}", hospitalInfSet.size());
                    log.info("hospitalDttSet 사이즈: {}", hospitalDttSet.size());
                    log.info("hospitalPos 사이즈: {}", hospitalPos.size());

                    /* 데이터 저장 */
                    medicAidInsRepository.saveAll(medicAidIns);
                    hospitalInfRepository.saveAll(hospitalInfSet);
                    hospitalDttRepository.saveAll(hospitalDttSet);
                    hospitalPosRepository.saveAll(hospitalPos);

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    private HospitalPos toHospitalPosEntity(Hospital hospital) {
        return HospitalPos.builder()
                .id(hospital.getId())
                .lat(hospital.getLat())
                .lon(hospital.getLon())
                .simpleMap(hospital.getSimpleMap())
                .build();
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
