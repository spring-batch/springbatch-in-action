package com.batch.domain.oracle;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CsvTableMapper {
    @Select("SELECT * FROM CSV_TABLE")
    LibraryEntity findAll();
}
