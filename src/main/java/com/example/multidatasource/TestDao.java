package com.example.multidatasource;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface TestDao {
    @DynamicDataSource(name = LoadedDataSources.PRIMARY)
    @Insert({"INSERT INTO `table1` (`name`, `gmt_created`) VALUES (#{name}, #{gmtCreated});"})
    void insertIntoTest(Map<String, Object> params);

    @DynamicDataSource(name = LoadedDataSources.FIRST)
    @Insert("INSERT INTO `table1` (`name`, `gmt_created`) VALUES (#{name}, #{gmtCreated});")
    void insertIntoMybatis(Map<String, Object> params);

    @DynamicDataSource
    @Insert("INSERT INTO `table1` (`name`, `gmt_created`) VALUES (#{name}, #{gmtCreated});")
    void insertIntoDefault(Map<String, Object> params);
}
