package com.batch.demo.library.listener;

import com.batch.demo.library.repository.LibraryTmpEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;

/**
 * Library CSV -> TmpEntity 작업 시 Table 비우기
 */
@Slf4j
public class LibraryTmpStepListener implements StepExecutionListener{

    private final LibraryTmpEntityRepository libraryTmpEntityRepository;

    public LibraryTmpStepListener(LibraryTmpEntityRepository libraryTmpEntityRepository) {
        this.libraryTmpEntityRepository = libraryTmpEntityRepository;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        libraryTmpEntityRepository.deleteAll();
        log.info("[LOG] [CSV_TABLE] [INITIALIZE] [SIZE] [{}]", libraryTmpEntityRepository.findAll().size());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.EXECUTING;
    }
}
