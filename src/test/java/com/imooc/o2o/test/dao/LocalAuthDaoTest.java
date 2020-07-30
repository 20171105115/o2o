package com.imooc.o2o.test.dao;

import com.imooc.o2o.dao.LocalAuthDao;
import com.imooc.o2o.entity.LocalAuth;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class LocalAuthDaoTest extends BaseTest {

    @Autowired
    private LocalAuthDao localAuthDao;

    @Test
    public void testQueryLocalAuthByUsernameAndPwd(){
        LocalAuth localAuth = localAuthDao.queryLocalAuthByUserAndPwd("test","test");
        assertEquals("super",localAuth.getPersonInfo().getName());
    }


    @Test
    public void testQueryLocalAuthByUserId(){
        LocalAuth localAuth = localAuthDao.queryLocalAuthByUserId(11L);
        assertEquals("super",localAuth.getPersonInfo().getName());
    }

    @Test
    public void testInsertLocalAuth(){
        LocalAuth localAuth = new LocalAuth();
        PersonInfo user = new PersonInfo();
        user.setUserId(11L);
        localAuth.setUsername("test");
        localAuth.setPassword("test");
        localAuth.setCreateTime(new Date());
        localAuth.setPersonInfo(user);
        int i = localAuthDao.insertLocalAuth(localAuth);
        assertEquals(1,i);
    }

    @Test
    public void testUpdateLocalAuth(){
        String username = "test";
        String password = "test";
        String newPassword = "test123";
        long userId = 11L;
        int i = localAuthDao.updateLocalAuth(username,password,newPassword,userId,new Date());
        assertEquals(1,i);
    }
}
