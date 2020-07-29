package com.imooc.o2o.test.dao;

import com.imooc.o2o.dao.ShopCategoryDao;
import com.imooc.o2o.entity.ShopCategory;
import com.imooc.o2o.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ShopCategoryDaoTest extends BaseTest {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void testQueryShopCategory(){
//        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(new ShopCategory());
//        assertEquals(21,shopCategoryList.size());

//        ShopCategory parent = new ShopCategory();
//        parent.setShopCategoryId(11L);
//        ShopCategory shopCategory = new ShopCategory();
//        shopCategory.setParent(parent);
        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(null);

        assertEquals(6,shopCategoryList.size());
    }
}
