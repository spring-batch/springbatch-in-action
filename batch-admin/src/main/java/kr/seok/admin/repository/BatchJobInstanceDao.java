package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchJobInstance;
import kr.seok.admin.dto.JobInstanceRequest;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BatchJobInstanceDao {
    protected static final String NAMESPACE = "BatchJobMapper.";

    private final SqlSession sqlSession;

    public BatchJobInstanceDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<BatchJobInstance> getJobInstances(JobInstanceRequest request) {
        return sqlSession.selectList(NAMESPACE + "SELECT_JOB_INSTANCES", request);
    }
}
