package kr.seok.hospital.tasklet;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class Tasklet_H_FileReader implements Tasklet, StepExecutionListener {

    private final FileUtils fileUtils;
    private List<String> lines;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ClassPathResource classPathResource = new ClassPathResource("files/seoul_hospital_position_info_utf8.csv");
        fileUtils.getFile(classPathResource);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        /* 데이터 */
        lines = fileUtils.readFileLine();
        return RepeatStatus.FINISHED;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        stepExecution
                .getJobExecution()
                .getExecutionContext()
                .put("hospitalDto", this.lines);
        return ExitStatus.COMPLETED;
    }


}
