package com.imooc.o2o.test.service;

import com.imooc.o2o.service.AreaService;
import com.imooc.o2o.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class AreaServiceTest extends BaseTest {

    @Autowired
    private AreaService areaService;

    @Test
    public void testGetAreaList(){
        assertEquals(2,areaService.getAreaList().size());
    }


}
