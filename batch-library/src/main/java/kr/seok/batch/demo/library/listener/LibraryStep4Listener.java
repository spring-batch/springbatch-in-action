package kr.seok.batch.demo.library.listener;

import kr.seok.batch.demo.library.repository.LibraryEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

@Slf4j
public class LibraryStep4Listener implements StepExecutionListener {

    private LibraryEntityRepository libraryEntityRepository;

    public LibraryStep4Listener(LibraryEntityRepository libraryEntityRepository) {
        this.libraryEntityRepository = libraryEntityRepository;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        libraryEntityRepository.deleteAll();
        int libEntityTableSize = libraryEntityRepository.findAll().size();
        log.info("[LOG] [TB_SIGNGU] [INITIALIZE] [SIZE] [{}]", libEntityTableSize);
        if(libEntityTableSize != 0) throw new RuntimeException("데이터 삭제 오류");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.EXECUTING;
    }
}
