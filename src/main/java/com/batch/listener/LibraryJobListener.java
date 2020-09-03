package com.batch.listener;

import com.batch.domain.repository.LibraryTmpEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LibraryJobListener implements JobExecutionListener {

    private final LibraryTmpEntityRepository libraryTmpEntityRepository;

    public LibraryJobListener(LibraryTmpEntityRepository libraryTmpEntityRepository) {
        this.libraryTmpEntityRepository = libraryTmpEntityRepository;
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        libraryTmpEntityRepository.deleteAll();
        log.info("[LOG] [CSV_TABLE] [SIZE] [{}]", libraryTmpEntityRepository.findAll().size());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }
}
