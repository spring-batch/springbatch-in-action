package kr.seok.library.writer;

import kr.seok.common.excel.exception.ExcelInternalException;
import kr.seok.common.excel.resources.DefaultDataFormatDecider;
import kr.seok.common.excel.resources.ExcelRenderLocation;
import kr.seok.common.excel.resources.ExcelRenderResource;
import kr.seok.common.excel.resources.ExcelRenderResourceFactory;
import kr.seok.library.domain.ReportDto;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static kr.seok.common.excel.utils.SuperClassReflectionUtils.getField;

/**
 * 엑셀 리포트 작업 시 사용할 클래스
 * Step의 Wrtier로 대치 가능한 클래스
 */
public class ExcelItemWriter<T> implements ItemStreamWriter<ReportDto> {

    private SXSSFWorkbook wb;
    private SXSSFSheet sheet;

    private WritableResource resource;
    private ExcelRenderResource renderResource;

    protected static final SpreadsheetVersion supplyExcelVersion = SpreadsheetVersion.EXCEL2007;

    private static final String currDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault()));
    private static final int ROW_START_INDEX = 0;
    private static final int COLUMN_START_INDEX = 0;
    private int currentRowIndex = ROW_START_INDEX;

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {

        /* data validation check */
        this.wb = new SXSSFWorkbook();
        this.sheet = wb.createSheet();

        this.resource = new FileSystemResource("libraryReport_" + currDate + ".xlsx");
        this.renderResource = ExcelRenderResourceFactory.prepareRenderResource(ReportDto.class, wb, new DefaultDataFormatDecider());
        renderHeadersWithNewSheet(sheet, currentRowIndex++, COLUMN_START_INDEX);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {

    }

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
        validateData((List<T>) items);
        for (ReportDto renderedData : items) {
            renderBody(renderedData, currentRowIndex++, COLUMN_START_INDEX);
        }
    }

    private void validateData(List<T> data) {
        int maxRows = supplyExcelVersion.getMaxRows();
        if (data.size() > maxRows) {
            throw new IllegalArgumentException(
                    String.format("This concrete ExcelFile does not support over %s rows", maxRows));
        }
    }

    protected void renderHeadersWithNewSheet(SXSSFSheet sheet, int rowIndex, int columnStartIndex) {
        Row row = sheet.createRow(rowIndex);
        int columnIndex = columnStartIndex;
        for (String dataFieldName : renderResource.getDataFieldNames()) {
            Cell cell = row.createCell(columnIndex++);
            cell.setCellStyle(renderResource.getCellStyle(dataFieldName, ExcelRenderLocation.HEADER));
            cell.setCellValue(renderResource.getExcelHeaderName(dataFieldName));
        }
    }

    /* Body 부븐 작성하기 */
    protected void renderBody(Object data, int rowIndex, int columnStartIndex) {
        Row row = sheet.createRow(rowIndex);
        int columnIndex = columnStartIndex;
        for (String dataFieldName : renderResource.getDataFieldNames()) {
            Cell cell = row.createCell(columnIndex++);
            try {
                Field field = getField(data.getClass(), (dataFieldName));
                field.setAccessible(true);
                cell.setCellStyle(renderResource.getCellStyle(dataFieldName, ExcelRenderLocation.BODY));
                Object cellValue = field.get(data);
                renderCellValue(cell, cellValue);
            } catch (Exception e) {
                throw new ExcelInternalException(e.getMessage(), e);
            }
        }
    }

    private void renderCellValue(Cell cell, Object cellValue) {
        if (cellValue instanceof Number) {
            Number numberValue = (Number) cellValue;
            cell.setCellValue(numberValue.doubleValue());
            return;
        }
        cell.setCellValue(cellValue == null ? "" : cellValue.toString());
    }

    public void write(OutputStream stream) throws IOException {
        wb.write(stream);
        wb.close();
        wb.dispose();
        stream.close();
    }
}
