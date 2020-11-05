package kr.seok.library.demo;

import kr.seok.library.domain.entity.CommonEntity;
import kr.seok.library.domain.entity.CountryEntity;
import kr.seok.library.domain.entity.TmpEntity;
import kr.seok.library.repository.CityRepository;
import kr.seok.library.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kr.seok.library.common.Constants.CHUNK_SIZE;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TmpToCountryPrototype {

    /* batch */
    private static final String JOB_NAME = "batch_TMP_TO_COUNTRY";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static Set<String> countryKeySet = new HashSet<>();

    /* DB */
    private final DataSource datasource;
    private final EntityManagerFactory entityManagerFactory;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;

    @Bean(name = JOB_NAME)
    public Job tmpToCountryJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        countryRepository.deleteAllInBatch();
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {

                    }
                })
                .start(tmpToCountryStep())
                .build();
    }

    /* Step */
    private Step tmpToCountryStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<TmpEntity, CommonEntity>chunk(CHUNK_SIZE)
                /* One Reader: JdbcCursorItemReader */
                .reader(tmpDbJdbcCursorReader())
                /* Multi Processor: CompositeItemProcessor */
                .processor(compositeProcessor())
                /* Multi Writer: CompositeItemWriter */
                .writer(compositeWriter())
                .build();

    }

    /* One Reader: JdbcCursorItemReader Type */
    private ItemReader<? extends TmpEntity> tmpDbJdbcCursorReader() {
        /* TB_TMP_LIBRARY 테이블의 컬럼리스트를 작성 */
        StringBuilder sb = new StringBuilder();
        for(String fields : TmpEntity.TmpFields.getFields())
            sb.append(fields).append(", ");

        return new JdbcCursorItemReader<TmpEntity>() {{
            /* 실행 Reader 명 설정 */
            setName(JOB_NAME + "_READER");
            /* Jdbc 방식으로 DB 접근 */
            setDataSource(datasource);
            /* 조회 쿼리 */
            setSql("SELECT " + sb.substring(0, sb.toString().length() - 2) + " FROM TB_TMP_LIBRARY");
            /* 조회된 row 데이터 Bean으로 매핑 */
            setRowMapper(new BeanPropertyRowMapper<>(TmpEntity.class));
        }};
    }

    /* 임시 테이블로부터 읽어온 데이터를 City, Country, Library Entity로 저장하기 위한 Processor */
    private ItemProcessor<? super TmpEntity, ? extends CommonEntity> compositeProcessor() {
        CompositeItemProcessor<? super TmpEntity, ? extends CommonEntity> compositeProcessor = new CompositeItemProcessor<>();

        List<ItemProcessor<? super TmpEntity, ? extends CommonEntity>> delegates = new ArrayList<>();
        delegates.add(tmpToCountryProcessor());

        compositeProcessor.setDelegates(delegates);

        return compositeProcessor;
    }

    private ItemWriter<? super CommonEntity> compositeWriter() {
        /* Composite Multi Writer */
        List<ItemWriter<CountryEntity>> delegates = new ArrayList<>();
        delegates.add(countryWriter());

        CompositeItemWriter compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(delegates);

        return compositeItemWriter;
    }

    /* 임시 테이블에서 각 도시명의 유잉한 값으로 Filtering */
    private ItemProcessor<? super TmpEntity, ? extends CommonEntity> tmpToCountryProcessor() {
        return (ItemProcessor<TmpEntity, CountryEntity>) item -> {
            String countryKey = item.getCityNm() + " " + item.getCountryNm();

            /* Set에 키 값이 포함되어 있으면 넘어가기*/
            if(countryKeySet.contains(countryKey)) return null;
            /* 값이 포함되지 않은 경우 set에 설정 및 Entity에 저장 */
            countryKeySet.add(countryKey);

            /* TODO: Jpa로 처리시 깔끔하게 처리하는 방법이 있을 듯 */
            Long cityId = cityRepository.findByCityNm(item.getCityNm()).get().getId();
            return CountryEntity.builder()
                    .cityId(cityId)
                    .countryNm(item.getCountryNm())
                    .build();
        };
    }
    private ItemWriter<CountryEntity> countryWriter() {
        return new JpaItemWriter<CountryEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
