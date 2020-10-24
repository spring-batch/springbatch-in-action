package kr.seok.library.demo;

import kr.seok.library.domain.entity.*;
import kr.seok.library.repository.CityRepository;
import kr.seok.library.repository.CountryRepository;
import kr.seok.library.repository.LibraryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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

import javax.batch.runtime.context.StepContext;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static kr.seok.library.common.Constants.CHUNK_SIZE;

/**
 * TODO: composite를 잘못이해한 로직
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class TmpToMultiProcessorDemo {
    private static final String JOB_NAME = "TMP_TO_MULTI_DB_WRITER";
    /* Batch */
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /* Jpa Entity Repository */
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final LibraryRepository libraryRepository;

    /* Data Set */
    private static Set<String> cityKey = new HashSet<>();
    private static Set<String> countryKeySet = new HashSet<>();
    private static Set<String> libraryKeySet = new HashSet<>();

    @Bean(name  = JOB_NAME + "_JOB")
    public Job tmpToMultiJob() {
        return jobBuilderFactory.get(JOB_NAME + "_JOB")
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        cityRepository.deleteAllInBatch();
                        countryRepository.deleteAllInBatch();
                        libraryRepository.deleteAllInBatch();
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {

                    }
                })
                .start(tmpToMultiStep())
                .build();
    }

    private Step tmpToMultiStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<TmpEntity, CommonEntity>chunk(CHUNK_SIZE)
                .reader(tmpOneReader())
                .writer(multiEntityWriter())
                .build();
    }

    private ItemWriter<? super CommonEntity> multiEntityWriter() {
        /* 데이터 처리할 Processor를 리스트에 등록 */
        List<ItemWriter<? extends CommonEntity>> delegates = new ArrayList<>();
        delegates.add(cityWriter());
        delegates.add(countryWriter());
        delegates.add(libraryWriter());

        /* Writer 위임 */
        CompositeItemWriter compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(delegates);

        return compositeItemWriter;
    }

    private ItemWriter<CityEntity> cityWriter() {
        return new JpaItemWriter<CityEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }

    private ItemWriter<CountryEntity> countryWriter() {
        return new JpaItemWriter<CountryEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
    private ItemWriter<LibraryEntity> libraryWriter() {
        return new JpaItemWriter<LibraryEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }

    /* 임시 테이블에서 각 도시명의 유잉한 값으로 Filtering */
    private ItemProcessor<? super TmpEntity, ? extends CommonEntity> tmpToCityProcessor() {
        return new ItemProcessor<TmpEntity, CommonEntity>() {
            @Override
            public CommonEntity process(TmpEntity item) throws Exception {
                /* Set에 키 값이 포함되어 있으면 넘어가기*/
                if(cityKey.contains(item.getCityNm())) return null;
                /* 값이 포함되지 않은 경우 set에 설정 및 Entity에 저장 */
                cityKey.add(item.getCityNm());
                return CityEntity.builder().cityNm(item.getCityNm()).build();
            }
        };
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

    /* 임시 테이블에서 각 도시명의 유잉한 값으로 Filtering */
    private ItemProcessor<? super TmpEntity, ? extends CommonEntity> tmpToLibraryProcessor() {
        return (ItemProcessor<TmpEntity, LibraryEntity>) item -> {
            String libraryKey = item.getCityNm() + " " + item.getCountryNm() + " " + item.getLibraryNm();

            /* Set에 키 값이 포함되어 있으면 넘어가기*/
            if(libraryKeySet.contains(libraryKey)) return null;
            /* 값이 포함되지 않은 경우 set에 설정 및 Entity에 저장 */
            libraryKeySet.add(libraryKey);

            /* TODO: Jpa로 처리시 깔끔하게 처리하는 방법이 있을 듯 */
            Long cityId = cityRepository.findByCityNm(item.getCityNm()).get().getId();
            Long countryId = countryRepository.findByCityIdAndCountryNm(cityId, item.getCountryNm()).get().getId();
            String libraryNm = item.getLibraryNm();
            String libraryType = item.getLibraryType();

            return LibraryEntity.builder()
                    .cityId(cityId)
                    .countryId(countryId)
                    .libraryNm(libraryNm)
                    .libraryType(libraryType)
                    .build();
        };
    }
    /* One Reader: JdbcCursorItemReader Type */
    private ItemReader<? extends TmpEntity> tmpOneReader() {
        /* TB_TMP_LIBRARY 테이블의 컬럼리스트를 작성 */
        StringBuilder sb = new StringBuilder();
        for(String fields : TmpEntity.TmpFields.getFields())
            sb.append(fields).append(", ");

        return new JdbcCursorItemReader<TmpEntity>() {{
            /* 실행 Reader 명 설정 */
            setName(JOB_NAME + "_READER");
            /* Jdbc 방식으로 DB 접근 */
            setDataSource(dataSource);
            /* 조회 쿼리 */
            setSql("SELECT " + sb.substring(0, sb.toString().length() - 2) + " FROM TB_TMP_LIBRARY");
            /* 조회된 row 데이터 Bean으로 매핑 */
            setRowMapper(new BeanPropertyRowMapper<>(TmpEntity.class));
        }};
    }
}
