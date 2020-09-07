package com.batch.demo.library.writer;

import com.batch.demo.library.domain.LibraryTotalEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
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
 *
 * SXSSF Workbook 기반
 */
public class CustomExcelItemWriter implements ItemStreamWriter<LibraryTotalEntity> {

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
        createHeaderRow(s);
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
        if (wb == null) {
            return;
        }
        try (BufferedOutputStream bos = new BufferedOutputStream((resource.getOutputStream()))) {
            wb.write(bos);
            bos.flush();
            wb.close();
        } catch (IOException e) {
            throw new ItemStreamException("Error writing to output file", e);
        }
        row = 0;
    }

    /**
     * 공통 Cell 생성
     *
     * @param row     Row (HSSFRow, XSSFRow, SXSSFRow)
     * @param value   Data Contents
     * @param column  Row Column Index
     * @param horizon Align Horizon
     * @param valign  Align Vertical
     */
    public void createCell(Row row, String value, int column, HorizontalAlignment horizon, VerticalAlignment valign) {
        cell = row.createCell(column);
        cell.setCellValue(value);
    }

    /**
     * 엑셀 헤더 생성
     *
     * @param sxssfSheet Sheet
     */
    private void createHeaderRow(SXSSFSheet sxssfSheet) {
        String[] fields = LibraryTotalEntity.LibraryTotalExcelFields.getFieldNmArrays();
        SXSSFRow sxssfRow = sxssfSheet.createRow(row);
        for (int i = 0; i < fields.length; i++) {
            sxssfSheet.setColumnWidth(i, sxssfSheet.getColumnWidth(i) * 17 / 10);
            createCell(sxssfRow, fields[i], i, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        }
        row++;
    }

    /**
     * Settings Contents
     *
     * @param items List Type Contents
     */
    private void writeContents(List<? extends LibraryTotalEntity> items) {
        SXSSFSheet sxssfSheet = wb.getSheetAt(0);

        for (LibraryTotalEntity libraryTotalEntity : items) {
            SXSSFRow sxssfRow = sxssfSheet.createRow(row++);
            createCell(sxssfRow, String.valueOf(libraryTotalEntity.getLbrryCode()), 0, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);   /* 도서관 코드   */
            createCell(sxssfRow, libraryTotalEntity.getLbrryNm(), 1, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);                     /* 도서관 명    */
            createCell(sxssfRow, String.valueOf(libraryTotalEntity.getCtprvnCode()), 2, HorizontalAlignment.CENTER, VerticalAlignment.CENTER); /* 시도 코드     */
            createCell(sxssfRow, libraryTotalEntity.getCtprvnNm(), 3, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);                    /* 시도 명     */
            createCell(sxssfRow, String.valueOf(libraryTotalEntity.getSignguCode()), 4, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getSignguNm(), 5, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getLbrrySe(), 6, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getWeekdayOperOpenHhmm(), 7, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getWeekdayOperCloseHhmm(), 8, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getSatOperOpenHhmm(), 9, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getSatOperCloseHhmm(), 10, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getHolidayOperCloseHhmm(), 11, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getHolidayOperOpenHhmm(), 12, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getHolidayOperCloseHhmm(), 13, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getSeatCo(), 14, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getBookCo(), 15, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getPblictnCo(), 16, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getNonebookCo(), 17, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getLonCo(), 18, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getLondayCnt(), 19, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getRdnmAdr(), 20, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getOperinstitutionNm(), 21, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getLbrryPhonenumber(), 22, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getHomepageUrl(), 23, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, String.valueOf(libraryTotalEntity.getPlotAr()), 24, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, String.valueOf(libraryTotalEntity.getBuldAr()), 25, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, String.valueOf(libraryTotalEntity.getLatitude()), 26, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, String.valueOf(libraryTotalEntity.getLongitude()), 27, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getReferenceDate(), 28, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getInsttCode(), 29, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
            createCell(sxssfRow, libraryTotalEntity.getInsttNm(), 30, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        }
    }
}
