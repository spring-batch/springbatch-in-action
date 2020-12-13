package kr.seok.library.job;

import kr.seok.library.step.FileToTmpStep;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LibraryJobConfig {

    public static final String JOB_NAME = "JPA_VERSION_LIBRARY_BATCH";
    private final JobBuilderFactory jobBuilderFactory;

    /* Step */
    private final FileToTmpStep fileToTmpStep;

    @Bean(name = JOB_NAME + "_JOB")
    public Job jpaLibraryJob() {
        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .incrementer(new RunIdIncrementer())
                .start(fileToTmpStep.fileToTmpStep())
                .build();
    }
}
