package com.batch.reader;

import com.batch.domain.Record;
import com.batch.mapper.LibraryMapper;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import sun.nio.cs.ext.EUC_KR;

import java.nio.file.Path;

public class CustomFlatFileReader extends FlatFileItemReader<Record> {

    public CustomFlatFileReader(Path path) {
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
        reader.setResource(new FileSystemResource(path.toString()));
        reader.setLineMapper(new DefaultLineMapper<Record>() {{
            setLineTokenizer(new DelimitedLineTokenizer(",") {{
                setFieldSetMapper(new LibraryMapper());
                setNames(
                        "도서관명",
                        "시도명",
                        "시군구명",
                        "도서관유형",
                        "휴관일",
                        "평일운영시작시각",
                        "평일운영종료시각",
                        "토요일운영시작시각",
                        "토요일운영종료시각",
                        "공휴일운영시작시각",
                        "공휴일운영종료시각",
                        "열람좌석수",
                        "자료수(도서)",
                        "자료수(연속간행물)",
                        "자료수(비도서)",
                        "대출가능권수",
                        "대출가능일수",
                        "소재지도로명주소",
                        "운영기관명",
                        "도서관전화번호",
                        "부지면적",
                        "건물면적",
                        "홈페이지주소",
                        "위도",
                        "경도",
                        "데이터기준일자",
                        "제공기관코드",
                        "제공기관명"
                );
            }});
        }});

        return reader;
    }

}
