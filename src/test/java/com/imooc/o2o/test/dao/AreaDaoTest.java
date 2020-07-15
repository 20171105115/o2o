package com.imooc.o2o.test.dao;

import com.imooc.o2o.dao.AreaDao;
import com.imooc.o2o.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class AreaDaoTest extends BaseTest {

    @Autowired
    private AreaDao areaDao;

    @Test
    public void testQueryAreaList(){
        assertEquals("西苑",areaDao.queryArea().get(0).getAreaName());
    }
}
