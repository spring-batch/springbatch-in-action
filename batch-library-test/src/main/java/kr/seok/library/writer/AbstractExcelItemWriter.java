package kr.seok.library.writer;

import kr.seok.common.excel.exception.ExcelInternalException;
import kr.seok.common.excel.resources.ExcelRenderLocation;
import kr.seok.common.excel.resources.ExcelRenderResource;
import kr.seok.library.domain.ReportDto;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.WritableResource;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static kr.seok.common.utils.SuperClassReflectionUtils.getField;

/**
 * [공통] 엑셀 작성 추상클래스
 * 1. validateData()
 * 2. renderHeadersWithNewSheet()
 * 3. renderBody()
 *  3.1 renderCellValue()
 * 4. write()
 */
public abstract class AbstractExcelItemWriter {

    /* POI Library */
    protected SXSSFWorkbook wb;
    protected SXSSFSheet sheet;
    protected ExcelRenderResource renderResource;
    protected WritableResource resource;

    /* SpreadSheet Version */
    protected static final SpreadsheetVersion supplyExcelVersion = SpreadsheetVersion.EXCEL2007;

    /* FileName Setting Parameter */
    protected static final String currDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").withZone(ZoneId.systemDefault()));

    /* Sheet Row Index */
    protected static final int ROW_START_INDEX = 0;
    protected static final int COLUMN_START_INDEX = 0;
    protected int currentRowIndex = ROW_START_INDEX;

    /* Sheet에 데이터 작성 시 데이터 유효성 검사 */
    protected void validateData(List<? extends ReportDto> data) {
        int maxRows = supplyExcelVersion.getMaxRows();
        if (data.size() > maxRows) {
            throw new IllegalArgumentException(
                    String.format("This concrete ExcelFile does not support over %s rows", maxRows));
        }
    }

    /* Excel Header Writer */
    protected void renderHeadersWithNewSheet(SXSSFSheet sheet, int rowIndex, int columnStartIndex) {
        Row row = sheet.createRow(rowIndex);

        int columnIndex = columnStartIndex;
        for (String dataFieldName : renderResource.getDataFieldNames()) {
            Cell cell = row.createCell(columnIndex++);
            cell.setCellStyle(renderResource.getCellStyle(dataFieldName, ExcelRenderLocation.HEADER));
            cell.setCellValue(renderResource.getExcelHeaderName(dataFieldName));
        }
    }

    /* Excel Body Writer */
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

    /* Sheet Row Data Rendering */
    private void renderCellValue(Cell cell, Object cellValue) {
        if (cellValue instanceof Number) {
            Number numberValue = (Number) cellValue;
            cell.setCellValue(numberValue.doubleValue());
            return;
        }
        cell.setCellValue(cellValue == null ? "" : cellValue.toString());
    }

    /* File OutputWriter */
    public void write(OutputStream stream) throws IOException {
        wb.write(stream);
        wb.close();
        wb.dispose();
        stream.close();
    }
}
