package com.example.multidatasource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class MultiDataSourceApplicationTests {

    @Resource
    private TestDao testDao;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    public void testDynamicDataSource() {
        Map<String, Object> record = new HashMap<>();
        record.put("name", "张三");
        record.put("gmtCreated", new Date());
        testDao.insertIntoTest(record);;
        testDao.insertIntoMybatis(record);
        testDao.insertIntoDefault(record);
        //for transactional test
//        throw new RuntimeException("......");
    }
}
