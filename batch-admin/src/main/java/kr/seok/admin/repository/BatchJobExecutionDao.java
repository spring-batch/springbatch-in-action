package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchJobExecution;
import kr.seok.admin.domain.BatchJobInstance;
import kr.seok.admin.dto.JobExecutionRequest;
import kr.seok.admin.dto.JobInstanceRequest;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BatchJobExecutionDao {
    protected static final String NAMESPACE = "BatchJobMapper.";

    private final SqlSession sqlSession;

    public BatchJobExecutionDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<BatchJobExecution> getJobExecutions(JobExecutionRequest request) {
        return sqlSession.selectList(NAMESPACE + "SELECT_JOB_EXECUTIONS", request);
    }
}
