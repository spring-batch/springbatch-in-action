package kr.seok.admin.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.seok.admin.dto.JobInstanceDto;
import kr.seok.admin.dto.QJobInstanceDto;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;
import static kr.seok.admin.domain.QBatchJobInstance.batchJobInstance;

/**
 * BatchJobInstance 엔티티 관련 Querydsl 클래스
 */
public class BatchJobInstanceQuerydslRepositoryImpl implements BatchJobInstanceQuerydslRepository {

    private final JPAQueryFactory queryFactory;

    public BatchJobInstanceQuerydslRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Map<String, Set<JobInstanceDto>> findJobInstanceGroupByJobName() {

        // jobName 으로 groupBy
        List<JobInstanceDto> jobInsList = queryFactory.
                select(new QJobInstanceDto(
                        batchJobInstance.jobInstanceId,
                        batchJobInstance.version,
                        batchJobInstance.jobName,
                        batchJobInstance.jobKey
                ))
                .from(batchJobInstance)
                .fetch();

        // dto -> groupBy
        return jobInsList.stream()
                .collect(
                        // JobName을 기준으로 Key : JobInstance List로 정리
                        groupingBy(JobInstanceDto::getJobName, toSet())
                );
    }
}
