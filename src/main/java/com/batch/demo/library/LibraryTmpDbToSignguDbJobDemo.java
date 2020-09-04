package com.batch.demo.library;

import com.batch.domain.batch.LibraryTmpEntity;
import com.batch.domain.batch.Sido;
import com.batch.listener.CustomItemReaderListener;
import com.batch.listener.CustomStepListener;
import com.batch.writer.ConsoleItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTmpDbToSignguDbJobDemo {

    private static final String JOB_NAME = "LIBRARY_TMP_TO_SIGNGU_JOB";
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    private static final int CHUNK_SIZE = 1000;

    @Bean(name = JOB_NAME)
    public Job libraryTmpDbToSignguDbJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(libraryTmpDbToSignguDbStep())
                .build();
    }

    @Bean(name = JOB_NAME + "_STEP")
    public Step libraryTmpDbToSignguDbStep() {
        return stepBuilderFactory.get(JOB_NAME + "_STEP")
                .<LibraryTmpEntity, Sido>chunk(CHUNK_SIZE)

                .listener(new CustomItemReaderListener())
                .reader(tmpDbToSidoReader())

                .writer(new ConsoleItemWriter<>())

                .listener(new CustomStepListener())
                .build();
    }

    @Bean(name = "tmpDb_reader")
    @StepScope
    public JdbcPagingItemReader<? extends LibraryTmpEntity> tmpDbToSidoReader() {
        return new JdbcPagingItemReader<LibraryTmpEntity>() {{
            setName("tmpDbReader");
            setFetchSize(1000);
            setDataSource(dataSource);
            setQueryProvider(dbToDbProvider());
            setRowMapper(new BeanPropertyRowMapper<>(LibraryTmpEntity.class));
        }};
    }

    private MySqlPagingQueryProvider dbToDbProvider() {

        StringBuffer selectClause = new StringBuffer();
        StringBuffer fromClause = new StringBuffer();

        selectClause.append("	A.LBRRY_NM,");
        selectClause.append("	A.CTPRVN_NM,");
        selectClause.append("	A.SIGNGU_NM,");
        selectClause.append("	A.LBRRY_SE,");
        selectClause.append("	A.CLOSE_DAY,");
        selectClause.append("	A.WEEKDAY_OPER_OPEN_HHMM,");
        selectClause.append("	A.WEEKDAY_OPER_CLOSE_HHMM,");
        selectClause.append("	A.SAT_OPER_OPEN_HHMM,");
        selectClause.append("	A.SAT_OPER_CLOSE_HHMM,");
        selectClause.append("	A.HOLIDAY_OPER_OPEN_HHMM,");
        selectClause.append("	A.HOLIDAY_OPER_CLOSE_HHMM,");
        selectClause.append("	A.SEAT_CO,");
        selectClause.append("	A.BOOK_CO,");
        selectClause.append("	A.PBLICTN_CO,");
        selectClause.append("	A.NONEBOOK_CO,");
        selectClause.append("	A.LON_CO,");
        selectClause.append("	A.LONDAY_CNT,");
        selectClause.append("	A.RDNM_ADR,");
        selectClause.append("	A.OPERINSTITUTION_NM,");
        selectClause.append("	A.LBRRY_PHONENUMBER,");
        selectClause.append("	A.PLOT_AR,");
        selectClause.append("	A.BULD_AR,");
        selectClause.append("	A.HOMEPAGEURL,");
        selectClause.append("	A.LATITUDE,");
        selectClause.append("	A.LONGITUDE,");
        selectClause.append("	A.REFERENCE_DATE,");
        selectClause.append("	A.INSTT_CODE,");
        selectClause.append("	A.INSTT_NM");

        fromClause.append("FROM CSV_TABLE A ");
        fromClause.append("JOIN TB_SIDO B ");
        fromClause.append("ON A.CTPRVN_NM = B.CTPRVN_NM ");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("A.LBRRY_NM", Order.DESCENDING);

        return new MySqlPagingQueryProvider() {{
            setSelectClause(selectClause.toString());
            setFromClause(fromClause.toString());
            setSortKeys(sortKeys);
        }};
    }
}
