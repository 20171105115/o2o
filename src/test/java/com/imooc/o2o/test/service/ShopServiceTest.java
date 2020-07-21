package com.imooc.o2o.test.service;

import com.imooc.o2o.dto.ShopExecution;
import com.imooc.o2o.entity.Area;
import com.imooc.o2o.entity.PersonInfo;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.enums.ShopStateEnum;
import com.imooc.o2o.service.ShopService;
import com.imooc.o2o.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Test
    public void testAddShop(){
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
        shop.setShopName("测试测试111");
        shop.setPriority(0);
        shop.setShopDesc("测试测试");
        shop.setEnableStatus(0);
        File shopImg = new File("/Users/a20171105115/Desktop/image/xiaohuangren.jpg");
        ShopExecution se = shopService.addShop(shop,shopImg);
        assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
    }

}
