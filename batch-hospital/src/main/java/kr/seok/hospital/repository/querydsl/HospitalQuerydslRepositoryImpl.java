package kr.seok.hospital.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.seok.hospital.domain.Hospital;
import kr.seok.hospital.domain.HospitalInf;
import kr.seok.hospital.domain.QHospitalDtt;
import kr.seok.hospital.domain.dto.HospitalEsEntity;
import kr.seok.hospital.domain.dto.QHospitalEsEntity;

import javax.persistence.EntityManager;
import java.util.List;

import static kr.seok.hospital.domain.QHospital.hospital;
import static kr.seok.hospital.domain.QHospitalDtt.hospitalDtt;
import static kr.seok.hospital.domain.QHospitalInf.hospitalInf;
import static kr.seok.hospital.domain.QHospitalPos.hospitalPos;
import static kr.seok.hospital.domain.QMedicAidIns.medicAidIns;

public class HospitalQuerydslRepositoryImpl implements HospitalQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public HospitalQuerydslRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Hospital> groupByCore() {
        return queryFactory
                .selectFrom(hospital)
                .fetch();
    }

    @Override
    public List<HospitalEsEntity> findLeftJoinAll() {
        return queryFactory
                .select(new QHospitalEsEntity(
                        hospitalInf.id,
                        hospitalInf.addr,
                        hospitalInf.hosCate,
                        hospitalInf.hosCateNm,
                        hospitalInf.medicAidIns.id,
                        hospitalInf.medicAidIns.aid_medic_ins_nm,
                        hospitalInf.edOperYn,
                        hospitalInf.etc,
                        hospitalInf.operDescDt,
                        hospitalInf.operNm,
                        hospitalInf.phone1,
                        hospitalInf.edPhone,
                        hospitalDtt.operHourMonC,
                        hospitalDtt.operHourTueC,
                        hospitalDtt.operHourWedC,
                        hospitalDtt.operHourThuC,
                        hospitalDtt.operHourFriC,
                        hospitalDtt.operHourSatC,
                        hospitalDtt.operHourSunC,
                        hospitalDtt.operHourHolC,
                        hospitalDtt.operHourMonS,
                        hospitalDtt.operHourTueS,
                        hospitalDtt.operHourWedS,
                        hospitalDtt.operHourThuS,
                        hospitalDtt.operHourFriS,
                        hospitalDtt.operHourSatS,
                        hospitalDtt.operHourSunS,
                        hospitalDtt.operHourHolS,
                        hospitalInf.zipCode1,
                        hospitalInf.zipCode2
//                        hospitalPos.lat,
//                        hospitalPos.lon
//                        hospitalInf.date
                ))
                .from(hospitalInf)
                .leftJoin(hospitalInf.medicAidIns, medicAidIns)
                .leftJoin(hospitalInf.hospitalDtt, hospitalDtt)
                .leftJoin(hospitalInf.hospitalPos, hospitalPos)
                .fetch();
    }
}
