package com.imooc.o2o.test.service;

import com.imooc.o2o.entity.HeadLine;
import com.imooc.o2o.service.HeadLineService;
import com.imooc.o2o.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class HeadLineServiceTest extends BaseTest {

    @Autowired
    private HeadLineService headLineService;

    @Test
    public void testGetHeadLineList(){
        HeadLine headLineCondition = new HeadLine();
        assertEquals(4,headLineService.getHeadLine(headLineCondition).size());
    }
}
