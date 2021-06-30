package kr.seok.admin.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

/**
 * BatchJobExecutionContext 엔티티 관련 Querydsl 클래스
 */
public class BatchJobExecutionContextQuerydslRepositoryImpl implements BatchJobExecutionContextQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public BatchJobExecutionContextQuerydslRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

}
