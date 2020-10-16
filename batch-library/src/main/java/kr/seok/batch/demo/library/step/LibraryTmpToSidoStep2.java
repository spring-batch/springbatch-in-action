package kr.seok.batch.demo.library.step;

import kr.seok.batch.demo.library.domain.LibraryTmpEntity;
import kr.seok.batch.demo.library.domain.Sido;
import kr.seok.batch.demo.library.listener.SidoStep2Listener;
import kr.seok.batch.demo.library.repository.SidoEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static kr.seok.batch.demo.LibraryTotalToReportFileJobDemo.CHUNK_SIZE;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTmpToSidoStep2 {

    private static final String STEP_NAME = "LIBRARY_TMP_TO_SIDO_STEP1";

    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;
    private final SidoEntityRepository sidoEntityRepository;

    @JobScope
    @Bean(name = STEP_NAME)
    public Step tmpToSidoStep2() {
        return stepBuilderFactory.get(STEP_NAME)
                .<LibraryTmpEntity, Sido>chunk(CHUNK_SIZE)

                .reader(libraryTmpStep2Reader())
                .processor(libraryTmpToSidoStep2Processor())
                .writer(librarySidoStep2Writer())

                .listener(new SidoStep2Listener(sidoEntityRepository))
                .build();
    }

    @Bean(name = STEP_NAME + "_READER")
    @StepScope
    public JdbcPagingItemReader<? extends LibraryTmpEntity> libraryTmpStep2Reader() {
        return new JdbcPagingItemReader<LibraryTmpEntity>() {{
            setName("tmpDbReader");
            setPageSize(CHUNK_SIZE);
            setFetchSize(CHUNK_SIZE);
            setDataSource(dataSource);
            setQueryProvider(dbToDbProvider());
            setRowMapper(new BeanPropertyRowMapper<>(LibraryTmpEntity.class));
        }

        private MySqlPagingQueryProvider dbToDbProvider() {

            StringBuffer selectClause = new StringBuffer();
            StringBuffer fromClause = new StringBuffer();

            selectClause.append("	LBRRY_NM,");
            selectClause.append("	CTPRVN_NM,");
            selectClause.append("	SIGNGU_NM,");
            selectClause.append("	LBRRY_SE,");
            selectClause.append("	CLOSE_DAY,");
            selectClause.append("	WEEKDAY_OPER_OPEN_HHMM,");
            selectClause.append("	WEEKDAY_OPER_CLOSE_HHMM,");
            selectClause.append("	SAT_OPER_OPEN_HHMM,");
            selectClause.append("	SAT_OPER_CLOSE_HHMM,");
            selectClause.append("	HOLIDAY_OPER_OPEN_HHMM,");
            selectClause.append("	HOLIDAY_OPER_CLOSE_HHMM,");
            selectClause.append("	SEAT_CO,");
            selectClause.append("	BOOK_CO,");
            selectClause.append("	PBLICTN_CO,");
            selectClause.append("	NONEBOOK_CO,");
            selectClause.append("	LON_CO,");
            selectClause.append("	LONDAY_CNT,");
            selectClause.append("	RDNM_ADR,");
            selectClause.append("	OPERINSTITUTION_NM,");
            selectClause.append("	LBRRY_PHONENUMBER,");
            selectClause.append("	PLOT_AR,");
            selectClause.append("	BULD_AR,");
            selectClause.append("	HOMEPAGEURL,");
            selectClause.append("	LATITUDE,");
            selectClause.append("	LONGITUDE,");
            selectClause.append("	REFERENCE_DATE,");
            selectClause.append("	INSTT_CODE,");
            selectClause.append("	INSTT_NM");

            fromClause.append("FROM CSV_TABLE ");

            Map<String, Order> sortKeys = new HashMap<>(1);
            sortKeys.put("LBRRY_NM", Order.DESCENDING);

            return new MySqlPagingQueryProvider() {{
                setSelectClause(selectClause.toString());
                setFromClause(fromClause.toString());
                setSortKeys(sortKeys);
            }};
        }};
    }

    @Bean(name = STEP_NAME + "PROCESSOR")
    @StepScope
    public ItemProcessor<? super LibraryTmpEntity, Sido> libraryTmpToSidoStep2Processor() {
        return (ItemProcessor<LibraryTmpEntity, Sido>) LibraryTmpEntity::toSido;
    }

    @Bean(name = STEP_NAME + "_WRITER")
    @StepScope
    public JpaItemWriter<Sido> librarySidoStep2Writer() {
        return new JpaItemWriter<Sido>() {{
            setEntityManagerFactory(entityManagerFactory);
        }};
    }
}
