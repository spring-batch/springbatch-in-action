package kr.seok.common.excel.style.color;

import org.apache.poi.ss.usermodel.CellStyle;

public class NoExcelColor implements ExcelColor {

    @Override
    public void applyForeground(CellStyle cellStyle) {
        // Do nothing
    }

}
