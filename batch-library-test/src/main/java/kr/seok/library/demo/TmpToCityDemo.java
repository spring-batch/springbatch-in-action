package kr.seok.library.demo;

import kr.seok.common.listener.CustomItemReaderListener;
import kr.seok.domain.library.CityEntity;
import kr.seok.domain.library.TmpEntity;
import kr.seok.library.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

import static kr.seok.library.common.Constants.CHUNK_SIZE;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TmpToCityDemo {

    /* batch */
    private static final String JOB_NAME = "batch_TMP_TO_CITY";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /* jpa */
    private final EntityManagerFactory entityManagerFactory;
    private final CityRepository cityRepository;

    @Bean(name = JOB_NAME)
    public Job tmpToCityJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        cityRepository.deleteAllInBatch();
                        long cnt = cityRepository.count();
                        if(cnt < 1) {
                            log.debug("시도 테이블 내에 데이터 수 확인 : {}", cnt);
                        } else {
                            log.debug("테이블 비우기 실패: {}", cnt);
                        }
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {

                    }
                })
                .start(tmpToCityStep())
                .build();
    }

    private Step tmpToCityStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<TmpEntity, CityEntity>chunk(CHUNK_SIZE)

                .listener(new CustomItemReaderListener<>())
//                .reader(tmpDbReader())
//
//                .processor(tmpToCityProcessor())
//
//                .writer(items -> {
////                    System.out.println(items);
//                })
                .build();
    }
}
