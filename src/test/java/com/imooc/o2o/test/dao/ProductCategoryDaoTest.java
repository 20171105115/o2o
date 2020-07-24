package com.imooc.o2o.test.dao;

import com.imooc.o2o.dao.ProductCategoryDao;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ProductCategoryDaoTest extends BaseTest {


    @Autowired
    private ProductCategoryDao productCategoryDao;

    @Test
    public void testQueryProductCategoryList(){
        List<ProductCategory> productCategoryList = productCategoryDao.queryProductCategoryList(28L);
        assertEquals(6,productCategoryList.size());
    }

    @Test
    public void testDeleteProductCategory(){
        int i = productCategoryDao.deleteProductCategory(7L,39L);
        assertEquals(1,i);
    }

    @Test
    public void testBatchInsertProductCategory(){
        List<ProductCategory> productCategoryList = new ArrayList<>();

        ProductCategory p1 = new ProductCategory();
        p1.setProductCategoryName("测试1");
        p1.setCreateTime(new Date());
        p1.setPriority(10);
        p1.setShopId(39L);

        ProductCategory p2 = new ProductCategory();
        p2.setProductCategoryName("测试2");
        p2.setCreateTime(new Date());
        p2.setPriority(10);
        p2.setShopId(39L);

        ProductCategory p3 = new ProductCategory();
        p3.setProductCategoryName("测试3");
        p3.setCreateTime(new Date());
        p3.setPriority(10);
        p3.setShopId(39L);

        productCategoryList.add(p1);
        productCategoryList.add(p2);
        productCategoryList.add(p3);

        int i = productCategoryDao.batchInsertProductCategory(productCategoryList);
        assertEquals(3,i);
    }
}
