package kr.seok.library.domain;


import kr.seok.common.excel.annotation.ExcelColumn;

public class ReportDto {

    @ExcelColumn(headerName = "도서관 명")
    private String libraryNm;
    @ExcelColumn(headerName = "시도군")
    private String cityNm;
    @ExcelColumn(headerName = "동읍면")
    private String countryNm;
    @ExcelColumn(headerName = "도서관 유형")
    private String libraryType;
    @ExcelColumn(headerName = "좌석 수")
    private String seatNum;

}
