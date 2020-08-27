package com.batch.domain.mysql;

import com.batch.domain.oracle.LibraryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DbTableMapper {
    @Select("SELECT * FROM CSV_TABLE")
    LibraryEntity findAll();
}
