package kr.seok.hospital.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.seok.hospital.domain.Hospital;

import javax.persistence.EntityManager;
import java.util.List;

import static kr.seok.hospital.domain.QHospital.hospital;

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
}
