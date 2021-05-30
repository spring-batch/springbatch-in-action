package kr.seok.batch.demo.library.listener;


import kr.seok.batch.demo.library.repository.SignguEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

@Slf4j
public class SignguStep3Listener implements StepExecutionListener {

    private SignguEntityRepository signguEntityRepository;

    public SignguStep3Listener(SignguEntityRepository signguEntityRepository) {
        this.signguEntityRepository = signguEntityRepository;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        signguEntityRepository.deleteAll();
        int signguTableSize = signguEntityRepository.findAll().size();
        log.info("[LOG] [TB_SIGNGU] [INITIALIZE] [SIZE] [{}]", signguTableSize);
        if (signguTableSize != 0) throw new RuntimeException("데이터 삭제 오류");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.EXECUTING;
    }
}
