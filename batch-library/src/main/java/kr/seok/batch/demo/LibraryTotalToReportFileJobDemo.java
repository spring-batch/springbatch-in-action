package kr.seok.batch.demo;

import kr.seok.batch.demo.library.step.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 각 Job을 Step 별로 작성하고 그 Step들을 한번에 실행하는 Job 생성
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTotalToReportFileJobDemo {

    /* 전체 프로세스 Job */
    private static final String JOB_NAME = "LIBRARY_TO_REPORT_FILE_JOB";
    public static final Integer CHUNK_SIZE = 1000;
    private final JobBuilderFactory jobBuilderFactory;

    /* 단계별 Step */
    private final LibraryFileToTmpStep1 libraryFileToTmpStep1;
    private final LibraryTmpToSidoStep2 libraryTmpToSidoStep2;
    private final LibraryTmpToSignguStep3 libraryTmpToSignguStep3;
    private final LibraryTmpToLibraryStep4 libraryTmpToLibraryStep4;
    private final LibraryTmpToLibraryDetailStep5 libraryTmpToLibraryDetailStep5;
    private final LibraryToReportFileStep6 libraryToReportFileStep6;

    @Bean(name = JOB_NAME)
    public Job libraryTotalToReportFileJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(libraryFileToTmpStep1.fileToTmpStep1())
                .next(libraryTmpToSidoStep2.tmpToSidoStep2())
                .next(libraryTmpToSignguStep3.libraryTmpToSignguStep3())
                .next(libraryTmpToLibraryStep4.libraryTmpToLibraryStep4())
                .next(libraryTmpToLibraryDetailStep5.libraryTmpToLibraryDetailStep5())
                .next(libraryToReportFileStep6.libraryEntityToReportFileStep6())
                .build();

    }
}
