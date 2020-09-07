package com.batch.demo.library.listener;

import com.batch.demo.library.repository.LibraryTmpEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Library CSV -> TmpEntity 작업 시 Table 비우기
 */
@Slf4j
public class LibraryTmpJobListener implements JobExecutionListener {

    private final LibraryTmpEntityRepository libraryTmpEntityRepository;

    public LibraryTmpJobListener(LibraryTmpEntityRepository libraryTmpEntityRepository) {
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
