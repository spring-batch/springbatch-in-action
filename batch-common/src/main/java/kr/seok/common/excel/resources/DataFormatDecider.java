package kr.seok.common.excel.resources;

import org.apache.poi.ss.usermodel.DataFormat;

public interface DataFormatDecider {

    short getDataFormat(DataFormat dataFormat, Class<?> type);

}
