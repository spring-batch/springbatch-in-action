package kr.seok.library.listener;

import kr.seok.library.domain.repository.LibraryTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LibraryTypeStepListener implements StepExecutionListener {
    private final LibraryTypeRepository libraryTypeRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        libraryTypeRepository.deleteAllInBatch();
        log.info("[LOG] [TB_LIBRARY_TYPE] [INITIALIZE] [SIZE] [{}]", libraryTypeRepository.count());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("[LOG] [TB_LIBRARY_TYPE] [FINALLY END] [TOTAL_SIZE] [{}]", libraryTypeRepository.count());
        return ExitStatus.EXECUTING;
    }
}
