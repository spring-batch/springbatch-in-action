package kr.seok.admin.repository;

import kr.seok.admin.domain.BatchJobInstance;
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

    /* JobList 조회 */
    public List<BatchJobInstance> getJobInstanceListPerJobName() {
        return sqlSession.selectList(NAMESPACE + "SELECT_JOB_INSTANCES_LIST");
    }
    /* 특정 Job List의 Instance 조회 */
    public List<BatchJobInstance> getJobInstances(String jobName) {
        return sqlSession.selectList(NAMESPACE + "SELECT_JOB_INSTANCES", jobName);
    }
}
