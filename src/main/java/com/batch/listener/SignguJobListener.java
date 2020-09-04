package com.batch.listener;

import com.batch.domain.repository.SignguEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SignguJobListener implements JobExecutionListener {

    private final SignguEntityRepository signguEntityRepository;

    public SignguJobListener(SignguEntityRepository signguEntityRepository) {
        this.signguEntityRepository = signguEntityRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        signguEntityRepository.deleteAll();
        log.info("[LOG] [TB_SIGNGU] [SIZE] [{}]", signguEntityRepository.findAll().size());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }
}
