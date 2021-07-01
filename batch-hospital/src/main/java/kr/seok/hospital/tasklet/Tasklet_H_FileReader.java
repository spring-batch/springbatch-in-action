package kr.seok.hospital.tasklet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

@Slf4j
@Configuration
public class Tasklet_H_FileReader implements Tasklet, StepExecutionListener {

    private final FileUtils fileUtils;
    private final String filePath;
    private List<String> lines;

    public Tasklet_H_FileReader(FileUtils fileUtils, @Value(value = "${file.path}") String filePath) {
        this.fileUtils = fileUtils;
        this.filePath = filePath;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        ClassPathResource classPathResource = new ClassPathResource(filePath);
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
