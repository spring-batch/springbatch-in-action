package com.batch.demo.library.listener;

import com.batch.demo.library.repository.SignguEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

@Slf4j
@RequiredArgsConstructor
public class SignguJobListener implements JobExecutionListener {

    private final SignguEntityRepository signguEntityRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        signguEntityRepository.deleteAll();
        log.info("[LOG] [TB_SIGNGU] [SIZE] [{}]", signguEntityRepository.findAll().size());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }
}
