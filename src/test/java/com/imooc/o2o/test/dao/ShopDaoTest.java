package com.imooc.o2o.test.dao;

import com.imooc.o2o.dao.ShopDao;
import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopDaoTest extends BaseTest {


    @Autowired
    private ShopDao shopDao;

    @Test
    public void testInsertShop(){
        Shop shop = new Shop();
        PersonInfo user = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();
        user.setUserId(1L);
        area.setAreaId(2);
        shopCategory.setShopCategoryId(10L);


        shop.setOwner(user);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);
        shop.setShopName("测试测试121");
        shop.setEnableStatus(0);

        int i = shopDao.insertShop(shop);
        assertEquals(i,1);
    }

    @Test
    public void testUpdateShop(){
        Shop shop = new Shop();
        shop.setShopId(40L);
        shop.setShopName("测试测试111");
        shop.setShopDesc("hello");

        int i = shopDao.updateShop(shop);
        assertEquals(i,1);
    }

    @Test
    public void testQueryShopById(){
        long shopId = 1;
        Shop shop  = shopDao.queryShopById(shopId);
        System.out.println(shop.getShopName());
        System.out.println(shop.getArea().getAreaName());
    }

    @Test
    public void testQueryShopListAndCount(){
        Shop shopCondition = new Shop();
        shopCondition.setShopName("尼姑");
        ShopCategory shopCategory = new ShopCategory();
        shopCategory.setShopCategoryId(10L);
        shopCondition.setShopCategory(shopCategory);

        List<Shop> shopList = shopDao.queryShopList(shopCondition,0,20);
        System.out.println(shopList.size());
    }


}
