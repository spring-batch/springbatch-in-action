package kr.seok.library.writer;

import kr.seok.common.excel.resources.DefaultDataFormatDecider;
import kr.seok.common.excel.resources.ExcelRenderResourceFactory;
import kr.seok.library.domain.ReportDto;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookType;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.core.io.FileSystemResource;

import java.io.IOException;
import java.util.List;

/**
 * 엑셀 리포트 작업 시 사용할 클래스
 * Step의 Wrtier로 대치 가능한 클래스
 */
public class ExcelItemWriter<T> extends AbstractExcelItemWriter implements ItemStreamWriter<ReportDto> {

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

        /* data validation check */
        super.wb = new SXSSFWorkbook();
        this.sheet = wb.createSheet();

        this.resource = new FileSystemResource("libraryReport_" + currDate + "." + XSSFWorkbookType.XLSX.getExtension());
        this.renderResource = ExcelRenderResourceFactory.prepareRenderResource(ReportDto.class, wb, new DefaultDataFormatDecider());
        renderHeadersWithNewSheet(sheet, currentRowIndex++, COLUMN_START_INDEX);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException { }

    @Override
    public void close() throws ItemStreamException {
        try {
            write(resource.getOutputStream());
        } catch (IOException e) {
            throw new ItemStreamException("Error writing to output file", e);
        }
    }

    @Override
    public void write(List<? extends ReportDto> items) {
        if(items.isEmpty()) {
            return;
        }
        validateData(items);
        for (ReportDto renderedData : items) {
            renderBody(renderedData, currentRowIndex++, COLUMN_START_INDEX);
        }
    }

}
