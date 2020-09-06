package com.batch.writer;

import com.batch.domain.batch.LibraryTotalEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Custom ItemStreamWriter
 * SXSSF Workbook 기반
 */
@Configuration
public class CustomWriterExcel implements ItemStreamWriter<LibraryTotalEntity> {

    private SXSSFWorkbook wb;
    private WritableResource resource;
    private CellStyle cellStyle;
    private Cell cell;
    private int row;

    private static final String currDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss").withZone(ZoneId.systemDefault()));

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        wb = new SXSSFWorkbook();
        SXSSFSheet s = wb.createSheet();
        resource = new FileSystemResource("libraryReport_" + currDate + ".xlsx");
        row = 0;
        aggregateRow();
        createHeaderRow(s);
    }

    private void aggregateRow() {

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void write(List<? extends LibraryTotalEntity> items) {
        writeContents(items);
    }

    @Override
    public void close() throws ItemStreamException {
        if(wb == null) {
            return;
        }
        try(BufferedOutputStream bos = new BufferedOutputStream((resource.getOutputStream()))) {
            wb.write(bos);
            bos.flush();
            wb.close();
        } catch(IOException e) {
            throw new ItemStreamException("Error writing to output file", e);
        }
        row = 2;
    }

    /**
     * 공통 Cell 생성
     * @param workbook  Workbook (HSSF, XSSF, SXSSF)
     * @param row       Row (HSSFRow, XSSFRow, SXSSFRow)
     * @param value     Data Contents
     * @param column    Row Column Index
     * @param horizon   Align Horizon
     * @param valign    Align Vertical
     */
    public void createCell(Workbook workbook, Row row, String value, int column, HorizontalAlignment horizon, VerticalAlignment valign) {
        cell = row.createCell(column);
        cell.setCellValue(value);
        CellUtil.setAlignment(cell, horizon);
        CellUtil.setVerticalAlignment(cell, valign);

//        cell.setCellStyle(setCellStyle(workbook, horizon, valign));
    }

    /**
     * 공통 CellStyle 적용
     * @param workbook  Workbook (HSSF, XSSF, SXSSF)
     * @param horizon   Align Horizon
     * @param valign    Align Vertical
     * @return CellStyle
     */
    public CellStyle setCellStyle(Workbook workbook, HorizontalAlignment horizon, VerticalAlignment valign) {
        cellStyle = workbook.createCellStyle();
        /* 가로세로 */
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setAlignment(horizon);
        /* 상하좌우 테두리 설정 */
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setWrapText(true);

        return cellStyle;
    }

    private void createHeaderRow(SXSSFSheet sxssfSheet) {
        String[] fields = LibraryTotalEntity.LibraryTotalExcelFields.getFieldNmArrays();
        SXSSFRow sxssfRow = sxssfSheet.createRow(row);
        for (int i = 0; i < fields.length; i++) {
            sxssfSheet.setColumnWidth(i, sxssfSheet.getColumnWidth(i) * 17 / 10);
            createCell(wb, sxssfRow, fields[i], i, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        }
        row++;
    }
    /**
     * Settings Contents
     * @param items List Type Contents
     */
    private void writeContents(List<? extends LibraryTotalEntity> items) {
        SXSSFSheet sxssfSheet = wb.getSheetAt(0);

        for(LibraryTotalEntity libraryTotalEntity : items) {
            SXSSFRow sxssfRow = sxssfSheet.createRow(row++);
            createCell(wb, sxssfRow, String.valueOf(libraryTotalEntity.getLbrryCode()), 0, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);   /* 도서관 코드   */
            createCell(wb, sxssfRow, libraryTotalEntity.getLbrryNm(), 1, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);                     /* 도서관 명    */
//            sxssfSheet.setColumnWidth(1, libraryTotalEntity.getLbrryNm().length() * 1000);

            createCell(wb, sxssfRow, String.valueOf(libraryTotalEntity.getCtprvnCode()), 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER); /* 시도 코드     */
//            sxssfSheet.setColumnWidth(1, 200);

            createCell(wb, sxssfRow, libraryTotalEntity.getCtprvnNm(), 3, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);                    /* 시도 명     */
//            sxssfSheet.setColumnWidth(3, libraryTotalEntity.getCtprvnNm().length() * 200);

            createCell(wb, sxssfRow, String.valueOf(libraryTotalEntity.getSignguCode()), 4, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getSignguNm(), 5, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
//            sxssfSheet.setColumnWidth(5, historyToken.getAccessToken().length() * 255);
            createCell(wb, sxssfRow, libraryTotalEntity.getLbrrySe(), 6, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
//            sxssfSheet.setColumnWidth(6, historyToken.getRefreshToken().length() * 255);
            createCell(wb, sxssfRow, libraryTotalEntity.getWeekdayOperOpenHhmm(), 7, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
//            sxssfSheet.setColumnWidth(7, String.valueOf(historyToken.getCreatedAt()).length() * 250);
            createCell(wb, sxssfRow, libraryTotalEntity.getWeekdayOperCloseHhmm(), 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
//            sxssfSheet.setColumnWidth(8, libraryTotalEntity.getSatOperOpenHhmm().length() * 250);
            createCell(wb, sxssfRow, libraryTotalEntity.getSatOperOpenHhmm(), 9, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
//            sxssfSheet.setColumnWidth(9, String.valueOf(historyToken.getAccessToken_createdAt()).length() * 250);
            createCell(wb, sxssfRow, libraryTotalEntity.getSatOperCloseHhmm(), 10, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getHolidayOperCloseHhmm(), 11, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getHolidayOperOpenHhmm(), 12, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getHolidayOperCloseHhmm(), 13, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getSeatCo(), 14, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getBookCo(), 15, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getPblictnCo(), 16, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getNonebookCo(), 17, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getLonCo(), 18, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getLondayCnt(), 19, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getRdnmAdr(), 20, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getOperinstitutionNm(), 21, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getLbrryPhonenumber(), 22, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getHomepageUrl(), 23, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, String.valueOf(libraryTotalEntity.getPlotAr()), 24, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, String.valueOf(libraryTotalEntity.getBuldAr()), 25, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, String.valueOf(libraryTotalEntity.getLatitude()), 26, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, String.valueOf(libraryTotalEntity.getLongitude()), 27, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getReferenceDate(), 28, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getInsttCode(), 29, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(wb, sxssfRow, libraryTotalEntity.getInsttNm(), 30, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        }
    }

}
