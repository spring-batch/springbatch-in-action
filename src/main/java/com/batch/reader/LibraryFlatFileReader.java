package com.batch.reader;

import com.batch.domain.file.Record;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import sun.nio.cs.ext.EUC_KR;

import java.nio.file.Path;

/**
 * CSV File Reader
 * TODO 공통 클래스 작성 필요
 */
public class LibraryFlatFileReader extends FlatFileItemReader<Record> {

    public LibraryFlatFileReader(Path path) {
        itemReader(path);
    }

    /* 데이터 제대로 읽는지 확인용 */
    private void readTest(FlatFileItemReader<Record> reader) {
        try {
            reader.open(new ExecutionContext());
            Record record;
            while ((record = reader.read()) != null) {
                System.out.println(record);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        reader.close();
    }

    public FlatFileItemReader<Record> itemReader(Path path) {
        FlatFileItemReader<Record> reader = new FlatFileItemReader<>();
        reader.setEncoding(new EUC_KR().historicalName());
        reader.setResource(new ClassPathResource(path.toString()));
        reader.setLineMapper(new DefaultLineMapper<Record>() {{
            setLineTokenizer(new DelimitedLineTokenizer(",") {{
                setNames(Record.RecordFields.getFieldNmArrays());
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<Record>() {{
                setTargetType(Record.class);
            }});
        }});

        return reader;
    }
}
