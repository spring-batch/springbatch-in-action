package kr.seok.library.listener;

import kr.seok.library.domain.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LibraryStepListener implements StepExecutionListener {
    private final LibraryRepository libraryRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        libraryRepository.deleteAllInBatch();
        log.info("[LOG] [TB_LIBRARY] [INITIALIZE] [SIZE] [{}]", libraryRepository.count());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("[LOG] [TB_LIBRARY] [FINALLY END] [TOTAL_SIZE] [{}]", libraryRepository.count());
        return ExitStatus.EXECUTING;
    }
}
