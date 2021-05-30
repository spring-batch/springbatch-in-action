package kr.seok.common.excel.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/* 엑셀 파일 작성 인터페이스 */
public interface ExcelFile<T> {

    /* 엑셀 파일 쓰기 메서드 */
    void write(OutputStream stream) throws IOException;

    /* 데이터가 너무 많은 경우 메모리 문제가 발생할 가능성이 있는경우 사용하는 메서드 */
    void addRows(List<T> data);

}
