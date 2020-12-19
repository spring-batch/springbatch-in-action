package kr.seok.library.listener;


import kr.seok.library.domain.repository.LibraryTmpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

/**
 * 임시 테이블 데이터 적재 Step
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TmpEntityStepListener implements StepExecutionListener {

    private final LibraryTmpRepository libraryTmpRepository;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        libraryTmpRepository.deleteAllInBatch();
        log.info("[LOG] [TB_TMP_LIBRARY] [INITIALIZE] [SIZE] [{}]", libraryTmpRepository.count());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("[LOG] [TB_TMP_LIBRARY] [FINALLY END] [TOTAL_SIZE] [{}]", libraryTmpRepository.count());
        return ExitStatus.EXECUTING;
    }
}
