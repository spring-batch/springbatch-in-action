package kr.seok.library.demo.prototype;

import kr.seok.library.domain.entity.CommonEntity;
import kr.seok.library.domain.entity.LibraryEntity;
import kr.seok.library.domain.entity.TmpEntity;
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
public class TmpToLibraryPrototype {

    /* batch */
    private static final String JOB_NAME = "batch_TMP_TO_LIBRARY";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private static Set<String> libraryKeySet = new HashSet<>();

    /* DB */
    private final DataSource datasource;
    private final EntityManagerFactory entityManagerFactory;
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final LibraryRepository libraryRepository;

    @Bean(name = JOB_NAME)
    public Job tmpToLibraryJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        libraryRepository.deleteAllInBatch();
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {

                    }
                })
                .start(tmpToLibraryStep())
                .build();
    }

    /* Step */
    private Step tmpToLibraryStep() {
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

        List<ItemProcessor<? super TmpEntity, ? extends CommonEntity>> delegates = new ArrayList<>();
        delegates.add(tmpToLibraryProcessor());

        CompositeItemProcessor<? super TmpEntity, ? extends CommonEntity> compositeProcessor = new CompositeItemProcessor<>();
        compositeProcessor.setDelegates(delegates);

        return compositeProcessor;
    }

    private ItemWriter<? super CommonEntity> compositeWriter() {
        /* Composite Multi Writer */
        List<ItemWriter<LibraryEntity>> delegates = new ArrayList<>();
        delegates.add(libraryWriter());

        CompositeItemWriter compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(delegates);

        return compositeItemWriter;
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
    private ItemWriter<LibraryEntity> libraryWriter() {
        return new JpaItemWriter<LibraryEntity>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
