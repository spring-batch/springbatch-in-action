package kr.seok.library.job;

import kr.seok.library.listener.TotalEntityJobListener;
import kr.seok.library.step.*;
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
    private final TmpToCityStep tmpToCityStep;
    private final TmpToCountryStep tmpToCountryStep;
    private final TmpToLibraryTypeStep tmpToLibraryTypeStep;
    private final TmpToLibraryStep tmpToLibraryStep;

    private final TotalEntityJobListener totalEntityJobListener;

    @Bean(name = JOB_NAME + "_JOB")
    public Job jpaLibraryJob() {
        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .listener(totalEntityJobListener)
                .incrementer(new RunIdIncrementer())
                .start(fileToTmpStep.fileToTmpStep())
                .next(tmpToCityStep.jpaTmpToCityStep())
                .next(tmpToCountryStep.jpaTmpToCountryStep())
                .next(tmpToLibraryTypeStep.jpaTmpToLibraryTypeStep())
                .next(tmpToLibraryStep.jpaTmpToLibraryStep())
                .build();
    }
}
