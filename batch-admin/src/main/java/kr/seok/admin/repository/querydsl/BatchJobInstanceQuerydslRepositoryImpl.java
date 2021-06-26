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
 * 쿼리 조회 시 Querydsl은 QType Dto를 사용하고, Java8 lambda stream()을 이용하여 데이터 조작을 하도록 한다.
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
