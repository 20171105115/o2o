package com.imooc.o2o.test.dao;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.imooc.o2o.dao.ProductDao;
import com.imooc.o2o.entity.Product;
import com.imooc.o2o.entity.ProductCategory;
import com.imooc.o2o.entity.Shop;
import com.imooc.o2o.test.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public class ProductDaoTest extends BaseTest {

    @Autowired
    private ProductDao productDao;


    @Test
    public void testInsertProduct(){
        //48 39

        Product product = new Product();
        product.setProductName("测试添加");
        product.setProductDesc("测试商品添加");
        product.setPriority(10);
        product.setEnableStatus(0);
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProductCategoryId(48L);
        Shop shop = new Shop();
        shop.setShopId(39L);

        product.setProductCategory(productCategory);
        product.setShop(shop);
        assertEquals(1,productDao.insertProduct(product));
    }
}
