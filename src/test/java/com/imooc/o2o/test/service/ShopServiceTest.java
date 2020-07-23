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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.acl.Owner;

import static org.junit.Assert.assertEquals;

public class ShopServiceTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Test
    public void testAddShop() throws FileNotFoundException {
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
        shop.setShopName("测试测试333");
        shop.setPriority(0);
        shop.setShopDesc("测试测试");
        shop.setEnableStatus(0);
        File shopImg = new File("/Users/a20171105115/Desktop/image/xiaohuangren.jpg");
        FileInputStream inputStream = new FileInputStream(shopImg);
        ShopExecution se = shopService.addShop(shop,inputStream,shopImg.getName());
        assertEquals(ShopStateEnum.CHECK.getState(),se.getState());
    }

    @Test
    public void testModifyShop() throws FileNotFoundException {
        long shopId = 1;
        Shop shop = new Shop();
        shop.setShopId(shopId);
        shop.setShopName("修改后的店铺名称");
        File file = new File("/Users/a20171105115/Desktop/image/1.jpg");
        FileInputStream input = new FileInputStream(file);
        String fileName = file.getName();
        ShopExecution se = shopService.modifyShop(shop,input,fileName);
        System.out.println("新的图片地址为" + se.getShop().getShopImg());
    }

    @Test
    public void testGetShopList(){
        Shop shopCondition = new Shop();
        PersonInfo owner = new PersonInfo();
        owner.setUserId(10L);
        shopCondition.setOwner(owner);
        ShopExecution se = shopService.getShopList(shopCondition,2,2);
        System.out.println("第二页的店铺有: "+se.getShopList().size());
        System.out.println("店铺总数有： "+se.getCount());
    }

}
