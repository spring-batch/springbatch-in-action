package kr.seok.batch.demo.library.listener;


import kr.seok.batch.demo.library.repository.SignguEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
public class CustomSignguJobListener implements JobExecutionListener {
    private SignguEntityRepository signguEntityRepository;

    public CustomSignguJobListener(SignguEntityRepository signguEntityRepository) {
        this.signguEntityRepository = signguEntityRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        signguEntityRepository.deleteAll();
        int deletedTableSize = signguEntityRepository.findAll().size();
        log.info("[LOG] [TB_SIDO] [DELETE_ALL] [CHECK SIZE:{}]", deletedTableSize);
        if (deletedTableSize != 0) throw new RuntimeException("데이터가 안 지워짐");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }
}
