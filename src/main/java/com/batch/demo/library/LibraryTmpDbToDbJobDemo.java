package com.batch.demo.library;

import com.batch.domain.oracle.Library;
import com.batch.domain.oracle.LibraryTmp;
import com.batch.writer.ConsoleItemWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LibraryTmpDbToDbJobDemo {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private static final String JOB_NAME = "tmpLibraryDbToLibraryDbJob";

    /**
     * JDBC 용 DataSource
     */
    @Resource(name = "oracleDataSource")
    private DataSource oracleDataSource;

    @Bean
    public Job tmpLibraryDbToLibraryDbJob() throws Exception {
        return this.jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(tmpLibDbToLibDbStep())
                .build();
    }

    @Bean
    public Step tmpLibDbToLibDbStep() throws Exception {
        return stepBuilderFactory.get(JOB_NAME + "_step")
                .<LibraryTmp, Library>chunk(1000)
                .reader(tmpToLibReader())
                .writer(new ConsoleItemWriter<>())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<? extends LibraryTmp> tmpToLibReader() throws Exception {
        return new JdbcPagingItemReader<LibraryTmp>() {{
            setFetchSize(1000);
            setDataSource(oracleDataSource);
            setQueryProvider(tmpQueryProvider());
            setRowMapper(new BeanPropertyRowMapper<>(LibraryTmp.class));
            setName(JOB_NAME + "_reader");
            afterPropertiesSet();
        }};
    }

    private OraclePagingQueryProvider tmpQueryProvider() {
        StringBuffer selectClause = new StringBuffer();

        selectClause.append(" B.CTPRVN_CODE, ");
        selectClause.append(" C.signgu_code ");
        for(String selectField : LibraryTmp.LibraryDBFields.getDBFieldArrays()) {
            if(selectClause.length() != 0) selectClause.append(", ");
            selectClause.append("A." + selectField);
        }

        StringBuffer fromClause = new StringBuffer();
        fromClause.append("FROM CSV_TABLE A ");
        fromClause.append("JOIN TB_SIDO B ");
        fromClause.append("ON A.ctprvn_nm = B.ctprvn_nm ");
        fromClause.append("JOIN TB_SIGNGU C ");
        fromClause.append("ON B.ctprvn_code = C.ctprvn_code ");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("LBRRY_CODE", Order.DESCENDING);

        return new OraclePagingQueryProvider() {{
            setSelectClause(selectClause.toString());
            setFromClause(fromClause.toString());
            setSortKeys(sortKeys);
        }};
    }

    @Bean
    public JdbcBatchItemWriter<Library> tmpToLbrryWriter() {
        StringBuffer selectSql = new StringBuffer();
        selectSql.append("INSERT INTO TB_LBRRY ( ");
        selectSql.append("LBRRY_CODE, ");
        selectSql.append("LBRRY_NM, ");
        selectSql.append("LBRRY_SE, ");
        selectSql.append("CLOSE_DAY, ");
        selectSql.append("WEEKDAY_OPER_OPEN_HHMM, ");
        selectSql.append("WEEKDAY_OPER_CLOSE_HHMM, ");
        selectSql.append("SAT_OPER_OPEN_HHMM, ");
        selectSql.append("SAT_OPER_CLOSE_HHMM, ");
        selectSql.append("HOLIDAY_OPER_OPEN_HHMM, ");
        selectSql.append("HOLIDAY_OPER_CLOSE_HHMM, ");
        selectSql.append("SEAT_CO, ");
        selectSql.append("BOOK_CO, ");
        selectSql.append("PBLICTN_CO, ");
        selectSql.append("NONEBOOK_CO, ");
        selectSql.append("LON_CO, ");
        selectSql.append("LONDAY_CNT, ");
        selectSql.append("RDNM_ADR, ");
        selectSql.append("OPERINSTITUTION_NM, ");
        selectSql.append("LBRRY_PHONENUMBER, ");
        selectSql.append("HOMEPAGEURL, ");
        selectSql.append("(SELECT CTPRVN_CODE FROM TB_SIDO WHERE A.CTPRVN_NM = CTPRVN_NM) AS CTRPVN_CODE, ");
        selectSql.append("(SELECT SIGNGU_CODE FROM TB_SIGNGU WHERE A.SIGNGU_NM = SIGNGU_NM) AS SIGNGU_CODE ");

        StringBuffer fromSql = new StringBuffer();
        fromSql.append(") VALUES ( ");
        fromSql.append(":lbrryCode, ");
        fromSql.append(":lbrryNm, ");
        fromSql.append(":lbrrySe, ");
        fromSql.append(":closeDay, ");
        fromSql.append(":weekdayOperOpenHhmm, ");
        fromSql.append(":weekdayOperCloseHhmm, ");
        fromSql.append(":satOperOpenHhmm, ");
        fromSql.append(":satOperCloseHhmm, ");
        fromSql.append(":holidayOperOpenHhmm, ");
        fromSql.append(":holidayOperCloseHhmm, ");
        fromSql.append(":seatCo, ");
        fromSql.append(":bookCo, ");
        fromSql.append(":pblictnCo, ");
        fromSql.append(":noneBookCo, ");
        fromSql.append(":lonCo, ");
        fromSql.append(":lonDayCnt, ");
        fromSql.append(":rdnmAdr, ");
        fromSql.append(":operInstitutionNm, ");
        fromSql.append(":lbrryPhoneNumber, ");
        fromSql.append(":homepageUrl ");
        fromSql.append(") ");

        System.out.println(selectSql.toString() + fromSql.toString());

        return new JdbcBatchItemWriter<Library>() {{
            setDataSource(oracleDataSource);
            setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
            setSql(selectSql.toString() + fromSql.toString());
            afterPropertiesSet();
        }};
    }

}
